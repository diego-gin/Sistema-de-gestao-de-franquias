package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Royalty;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.enums.SituacaoPagamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoyaltyRepository {

    public Optional<Royalty> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Royalty.class, id));
        } finally {
            em.close();
        }
    }

    // Busca dinâmica por unidade, período e/ou situação de pagamento
    public List<Royalty> buscar(Long unidadeId, String periodoReferencia, SituacaoPagamento situacao) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Royalty> cq = cb.createQuery(Royalty.class);
            Root<Royalty> raiz = cq.from(Royalty.class);

            List<Predicate> predicados = new ArrayList<>();
            if (unidadeId != null) {
                predicados.add(cb.equal(raiz.get("unidade").get("id"), unidadeId));
            }
            if (periodoReferencia != null && !periodoReferencia.isBlank()) {
                predicados.add(cb.equal(raiz.get("periodoReferencia"), periodoReferencia));
            }
            if (situacao != null) {
                predicados.add(cb.equal(raiz.get("situacaoPagamento"), situacao));
            }

            cq.select(raiz).orderBy(cb.desc(raiz.get("periodoReferencia")));
            if (!predicados.isEmpty()) {
                cq.where(predicados.toArray(new Predicate[0]));
            }

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    // Recalcula se já existir registro para essa unidade/período
    public Royalty calcular(Long unidadeId, String periodoReferencia, BigDecimal percentual) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            UnidadeFranqueada unidade = em.find(UnidadeFranqueada.class, unidadeId);
            if (unidade == null) {
                throw new NotFoundException("Unidade informada não existe.");
            }

            YearMonth periodo = YearMonth.parse(periodoReferencia);
            LocalDateTime inicio = periodo.atDay(1).atStartOfDay();
            LocalDateTime fim = periodo.plusMonths(1).atDay(1).atStartOfDay();

            BigDecimal faturamento = em.createQuery(
                            "SELECT COALESCE(SUM(v.valorTotal), 0) FROM Venda v " +
                                    "WHERE v.unidade.id = :unidadeId AND v.dataVenda >= :inicio AND v.dataVenda < :fim",
                            BigDecimal.class)
                    .setParameter("unidadeId", unidadeId)
                    .setParameter("inicio", inicio)
                    .setParameter("fim", fim)
                    .getSingleResult();

            Royalty royalty = em.createQuery(
                            "SELECT r FROM Royalty r WHERE r.unidade.id = :unidadeId AND r.periodoReferencia = :periodo",
                            Royalty.class)
                    .setParameter("unidadeId", unidadeId)
                    .setParameter("periodo", periodoReferencia)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            boolean novo = royalty == null;
            if (novo) {
                royalty = new Royalty();
                royalty.setUnidade(unidade);
                royalty.setPeriodoReferencia(periodoReferencia);
            }

            royalty.setPercentualAplicado(percentual);
            royalty.setFaturamentoBase(faturamento);
            royalty.calcularValor();

            if (novo) {
                em.persist(royalty);
            }

            em.getTransaction().commit();
            return royalty;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Royalty registrarPagamento(Long id, SituacaoPagamento situacao, LocalDate dataPagamento) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            Royalty royalty = em.find(Royalty.class, id);
            if (royalty == null) {
                throw new NotFoundException("Royalty não encontrado.");
            }
            royalty.setSituacaoPagamento(situacao);
            royalty.setDataPagamento(dataPagamento);
            em.getTransaction().commit();
            return royalty;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
