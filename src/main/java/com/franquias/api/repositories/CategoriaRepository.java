package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.models.Categoria;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CategoriaRepository {

    public Categoria salvar(Categoria categoria) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            if (categoria.getId() == null) {
                em.persist(categoria);
            } else {
                categoria = em.merge(categoria);
            }
            em.getTransaction().commit();
            return categoria;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Categoria> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Categoria.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<Categoria> buscarPorNome(String nome) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categoria c WHERE c.nome = :nome", Categoria.class)
                    .setParameter("nome", nome)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    public List<Categoria> listarTodas() {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Categoria c ORDER BY c.nome", Categoria.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
