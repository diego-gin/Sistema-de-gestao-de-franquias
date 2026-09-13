package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.models.Franqueadora;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class FranqueadoraRepository {

    public Franqueadora salvar(Franqueadora franqueadora) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            if (franqueadora.getId() == null) {
                em.persist(franqueadora);
            } else {
                franqueadora = em.merge(franqueadora);
            }
            em.getTransaction().commit();
            return franqueadora;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Franqueadora> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Franqueadora.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<Franqueadora> buscarPorCnpj(String cnpj) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT f FROM Franqueadora f WHERE f.cnpj = :cnpj", Franqueadora.class)
                    .setParameter("cnpj", cnpj)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    public List<Franqueadora> listarTodas() {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT f FROM Franqueadora f ORDER BY f.razaoSocial", Franqueadora.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
