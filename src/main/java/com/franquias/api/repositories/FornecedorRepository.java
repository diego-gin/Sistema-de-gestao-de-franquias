package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.exceptions.NotFoundException;
import com.franquias.api.models.Fornecedor;
import com.franquias.api.models.ProdutoServico;
import com.franquias.api.models.enums.StatusFornecedor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class FornecedorRepository {

    public Fornecedor salvar(Fornecedor fornecedor) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            if (fornecedor.getId() == null) {
                em.persist(fornecedor);
            } else {
                fornecedor = em.merge(fornecedor);
            }
            em.getTransaction().commit();
            return fornecedor;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<Fornecedor> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Fornecedor.class, id));
        } finally {
            em.close();
        }
    }

    public Optional<Fornecedor> buscarPorCnpj(String cnpj) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery("SELECT f FROM Fornecedor f WHERE f.cnpj = :cnpj", Fornecedor.class)
                    .setParameter("cnpj", cnpj)
                    .getResultStream()
                    .findFirst();
        } finally {
            em.close();
        }
    }

    // Busca dinâmica por nome, CNPJ e/ou status
    public List<Fornecedor> buscar(String nome, String cnpj, StatusFornecedor status) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Fornecedor> cq = cb.createQuery(Fornecedor.class);
            Root<Fornecedor> raiz = cq.from(Fornecedor.class);

            List<Predicate> predicados = new ArrayList<>();
            if (nome != null && !nome.isBlank()) {
                predicados.add(cb.like(cb.lower(raiz.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (cnpj != null && !cnpj.isBlank()) {
                predicados.add(cb.equal(raiz.get("cnpj"), cnpj));
            }
            if (status != null) {
                predicados.add(cb.equal(raiz.get("status"), status));
            }

            cq.select(raiz).distinct(true).orderBy(cb.asc(raiz.get("nome")));
            if (!predicados.isEmpty()) {
                cq.where(predicados.toArray(new Predicate[0]));
            }

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }

    // Lista vazia remove todas as associações
    public Fornecedor associarProdutos(Long fornecedorId, List<Long> produtoIds) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();

            Fornecedor fornecedor = em.find(Fornecedor.class, fornecedorId);
            if (fornecedor == null) {
                throw new NotFoundException("Fornecedor não encontrado.");
            }

            List<ProdutoServico> produtos = produtoIds.isEmpty()
                    ? new ArrayList<>()
                    : em.createQuery("SELECT p FROM ProdutoServico p WHERE p.id IN :ids", ProdutoServico.class)
                        .setParameter("ids", produtoIds)
                        .getResultList();

            fornecedor.setProdutos(new HashSet<>(produtos));

            em.getTransaction().commit();
            return fornecedor;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
