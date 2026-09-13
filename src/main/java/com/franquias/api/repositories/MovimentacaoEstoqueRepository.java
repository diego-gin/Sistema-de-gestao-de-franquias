package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.models.MovimentacaoEstoque;
import jakarta.persistence.EntityManager;

import java.util.List;

public class MovimentacaoEstoqueRepository {

    public List<MovimentacaoEstoque> listarPorEstoque(Long estoqueId) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT m FROM MovimentacaoEstoque m WHERE m.estoque.id = :estoqueId " +
                                    "ORDER BY m.dataMovimentacao DESC", MovimentacaoEstoque.class)
                    .setParameter("estoqueId", estoqueId)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
