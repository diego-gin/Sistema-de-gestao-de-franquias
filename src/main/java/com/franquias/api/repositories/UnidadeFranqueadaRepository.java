package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.models.Franqueado;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.enums.SituacaoUnidade;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UnidadeFranqueadaRepository {

    public UnidadeFranqueada salvar(UnidadeFranqueada unidade) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            if (unidade.getId() == null) {
                em.persist(unidade);
            } else {
                unidade = em.merge(unidade);
            }

            em.getTransaction().commit();
            return unidade;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<UnidadeFranqueada> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(UnidadeFranqueada.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<UnidadeFranqueada> buscarPorCnpj(String cnpj) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM UnidadeFranqueada u WHERE u.cnpj = :cnpj", UnidadeFranqueada.class)
                    .setParameter("cnpj", cnpj)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    public List<UnidadeFranqueada> buscar(String nome, String cidade, String cnpj,
                                           String responsavelNome, SituacaoUnidade situacao) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<UnidadeFranqueada> cq = cb.createQuery(UnidadeFranqueada.class);
            Root<UnidadeFranqueada> raiz = cq.from(UnidadeFranqueada.class);
            Join<UnidadeFranqueada, Franqueado> franqueadoJoin = raiz.join("franqueado", JoinType.LEFT);

            List<Predicate> predicados = new ArrayList<>();

            if (nome != null && !nome.isBlank()) {
                predicados.add(cb.like(cb.lower(raiz.get("nomeFantasia")), "%" + nome.toLowerCase() + "%"));
            }
            if (cidade != null && !cidade.isBlank()) {
                predicados.add(cb.like(cb.lower(raiz.get("cidade")), "%" + cidade.toLowerCase() + "%"));
            }
            if (cnpj != null && !cnpj.isBlank()) {
                predicados.add(cb.equal(raiz.get("cnpj"), cnpj));
            }
            if (responsavelNome != null && !responsavelNome.isBlank()) {
                predicados.add(cb.like(cb.lower(franqueadoJoin.get("nome")), "%" + responsavelNome.toLowerCase() + "%"));
            }
            if (situacao != null) {
                predicados.add(cb.equal(raiz.get("situacao"), situacao));
            }

            cq.select(raiz)
                    .orderBy(cb.asc(raiz.get("nomeFantasia")));

            if (!predicados.isEmpty()) {
                cq.where(predicados.toArray(new Predicate[0]));
            }

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
