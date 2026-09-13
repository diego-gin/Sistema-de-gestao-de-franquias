package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.models.Franqueado;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class FranqueadoRepository {

    public Franqueado salvar(Franqueado franqueado) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            if (franqueado.getId() == null) {
                em.persist(franqueado);
            } else {
                franqueado = em.merge(franqueado);
            }
            em.getTransaction().commit();
            return franqueado;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Franqueado> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Franqueado.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<Franqueado> buscarPorCpfCnpj(String cpfCnpj) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT f FROM Franqueado f WHERE f.cpfCnpj = :cpfCnpj", Franqueado.class)
                    .setParameter("cpfCnpj", cpfCnpj)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    public List<Franqueado> listarTodos() {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT f FROM Franqueado f ORDER BY f.nome", Franqueado.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
