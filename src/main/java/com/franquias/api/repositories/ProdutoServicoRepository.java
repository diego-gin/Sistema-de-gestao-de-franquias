package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.models.ProdutoServico;
import com.franquias.api.models.enums.StatusProduto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoServicoRepository {

    /**
     * Assim como em UnidadeFranqueadaRepository, NÃO usamos em.getReference()
     * para a categoria: o Service já busca a Categoria completa antes de
     * chamar este método, e precisamos dela completa (com o nome) para montar
     * o ProdutoResponse depois.
     */
    public ProdutoServico salvar(ProdutoServico produto) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            em.getTransaction().begin();
            if (produto.getId() == null) {
                em.persist(produto);
            } else {
                produto = em.merge(produto);
            }
            em.getTransaction().commit();
            return produto;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Optional<ProdutoServico> buscarPorId(Long id) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return Optional.ofNullable(em.find(ProdutoServico.class, id));
        } finally {
            em.close();
        }
    }

    /**
     * Busca dinâmica por nome, categoria e/ou status — todos os filtros são
     * opcionais. Atende ao requisito "listar produtos/serviços por categoria
     * e status" e "consulta por nome, categoria e situação".
     */
    public List<ProdutoServico> buscar(String nome, Long categoriaId, StatusProduto status) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<ProdutoServico> cq = cb.createQuery(ProdutoServico.class);
            Root<ProdutoServico> raiz = cq.from(ProdutoServico.class);

            List<Predicate> predicados = new ArrayList<>();

            if (nome != null && !nome.isBlank()) {
                predicados.add(cb.like(cb.lower(raiz.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (categoriaId != null) {
                predicados.add(cb.equal(raiz.get("categoria").get("id"), categoriaId));
            }
            if (status != null) {
                predicados.add(cb.equal(raiz.get("status"), status));
            }

            cq.select(raiz)
                    .orderBy(cb.asc(raiz.get("nome")));

            if (!predicados.isEmpty()) {
                cq.where(predicados.toArray(new Predicate[0]));
            }

            return em.createQuery(cq).getResultList();
        } finally {
            em.close();
        }
    }
}
