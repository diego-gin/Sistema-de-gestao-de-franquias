package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.models.ChamadoSuporte;
import com.franquias.api.models.enums.PrioridadeChamado;
import com.franquias.api.models.enums.StatusChamado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChamadoSuporteRepository {

    public ChamadoSuporte salvar(ChamadoSuporte chamado) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            if (chamado.getId() == null) {
                em.persist(chamado);
            } else {
                chamado = em.merge(chamado);
            }
            em.getTransaction().commit();
            return chamado;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<ChamadoSuporte> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(ChamadoSuporte.class, id));
        } finally {
            em.close();
        }
    }

    // Busca dinâmica por unidade, status e/ou prioridade
    public List<ChamadoSuporte> buscar(Long unidadeId, StatusChamado status, PrioridadeChamado prioridade) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ChamadoSuporte> cq = cb.createQuery(ChamadoSuporte.class);
            Root<ChamadoSuporte> raiz = cq.from(ChamadoSuporte.class);

            List<Predicate> predicados = new ArrayList<>();
            if (unidadeId != null) {
                predicados.add(cb.equal(raiz.get("unidade").get("id"), unidadeId));
            }
            if (status != null) {
                predicados.add(cb.equal(raiz.get("status"), status));
            }
            if (prioridade != null) {
                predicados.add(cb.equal(raiz.get("prioridade"), prioridade));
            }

            cq.select(raiz).orderBy(cb.desc(raiz.get("dataAbertura")));
            if (!predicados.isEmpty()) {
                cq.where(predicados.toArray(new Predicate[0]));
            }

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
