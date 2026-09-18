package com.franquias.api.repositories;

import com.franquias.api.data.JpaUtil;
import com.franquias.api.dtos.ChamadosPorStatusResponse;
import com.franquias.api.dtos.FaturamentoPorUnidadeResponse;
import com.franquias.api.dtos.ProdutoMaisVendidoResponse;
import com.franquias.api.dtos.RoyaltiesTotaisResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RelatorioRepository {

    // Faturamento agrupado por unidade, com filtros opcionais de unidade e período
    public List<FaturamentoPorUnidadeResponse> faturamentoPorUnidade(Long unidadeId, LocalDate dataInicio,
                                                                       LocalDate dataFim, Integer limite) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT new com.franquias.api.dtos.FaturamentoPorUnidadeResponse(" +
                            "v.unidade.id, v.unidade.nomeFantasia, SUM(v.valorTotal)) " +
                            "FROM Venda v WHERE 1 = 1");

            if (unidadeId != null) jpql.append(" AND v.unidade.id = :unidadeId");
            if (dataInicio != null) jpql.append(" AND v.dataVenda >= :inicio");
            if (dataFim != null) jpql.append(" AND v.dataVenda < :fim");
            jpql.append(" GROUP BY v.unidade.id, v.unidade.nomeFantasia ORDER BY SUM(v.valorTotal) DESC");

            TypedQuery<FaturamentoPorUnidadeResponse> query =
                    em.createQuery(jpql.toString(), FaturamentoPorUnidadeResponse.class);

            if (unidadeId != null) query.setParameter("unidadeId", unidadeId);
            if (dataInicio != null) query.setParameter("inicio", dataInicio.atStartOfDay());
            if (dataFim != null) query.setParameter("fim", dataFim.plusDays(1).atStartOfDay());
            if (limite != null) query.setMaxResults(limite);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Soma dos royalties calculados, agregada por situação de pagamento
    public RoyaltiesTotaisResponse royaltiesTotais(Long unidadeId, String periodoReferencia) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT new com.franquias.api.dtos.RoyaltiesTotaisResponse(" +
                            "COALESCE(SUM(r.valorCalculado), 0), " +
                            "COALESCE(SUM(CASE WHEN r.situacaoPagamento = com.franquias.api.models.enums.SituacaoPagamento.PAGO THEN r.valorCalculado ELSE 0 END), 0), " +
                            "COALESCE(SUM(CASE WHEN r.situacaoPagamento = com.franquias.api.models.enums.SituacaoPagamento.PENDENTE THEN r.valorCalculado ELSE 0 END), 0), " +
                            "COALESCE(SUM(CASE WHEN r.situacaoPagamento = com.franquias.api.models.enums.SituacaoPagamento.ATRASADO THEN r.valorCalculado ELSE 0 END), 0)) " +
                            "FROM Royalty r WHERE 1 = 1");

            if (unidadeId != null) jpql.append(" AND r.unidade.id = :unidadeId");
            if (periodoReferencia != null && !periodoReferencia.isBlank()) jpql.append(" AND r.periodoReferencia = :periodo");

            TypedQuery<RoyaltiesTotaisResponse> query =
                    em.createQuery(jpql.toString(), RoyaltiesTotaisResponse.class);

            if (unidadeId != null) query.setParameter("unidadeId", unidadeId);
            if (periodoReferencia != null && !periodoReferencia.isBlank()) query.setParameter("periodo", periodoReferencia);

            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Produtos/serviços mais vendidos (soma de quantidade)
    public List<ProdutoMaisVendidoResponse> produtosMaisVendidos(Long unidadeId, LocalDate dataInicio,
                                                                   LocalDate dataFim, Integer limite) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT new com.franquias.api.dtos.ProdutoMaisVendidoResponse(" +
                            "i.produtoServico.id, i.produtoServico.nome, SUM(i.quantidade)) " +
                            "FROM ItemVenda i WHERE 1 = 1");

            if (unidadeId != null) jpql.append(" AND i.venda.unidade.id = :unidadeId");
            if (dataInicio != null) jpql.append(" AND i.venda.dataVenda >= :inicio");
            if (dataFim != null) jpql.append(" AND i.venda.dataVenda < :fim");
            jpql.append(" GROUP BY i.produtoServico.id, i.produtoServico.nome ORDER BY SUM(i.quantidade) DESC");

            TypedQuery<ProdutoMaisVendidoResponse> query =
                    em.createQuery(jpql.toString(), ProdutoMaisVendidoResponse.class);

            if (unidadeId != null) query.setParameter("unidadeId", unidadeId);
            if (dataInicio != null) query.setParameter("inicio", dataInicio.atStartOfDay());
            if (dataFim != null) query.setParameter("fim", dataFim.plusDays(1).atStartOfDay());
            if (limite != null) query.setMaxResults(limite);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Contagem de chamados agrupada por status
    public List<ChamadosPorStatusResponse> chamadosPorStatus(Long unidadeId) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT new com.franquias.api.dtos.ChamadosPorStatusResponse(c.status, COUNT(c)) " +
                            "FROM ChamadoSuporte c WHERE 1 = 1");

            if (unidadeId != null) jpql.append(" AND c.unidade.id = :unidadeId");
            jpql.append(" GROUP BY c.status");

            TypedQuery<ChamadosPorStatusResponse> query =
                    em.createQuery(jpql.toString(), ChamadosPorStatusResponse.class);

            if (unidadeId != null) query.setParameter("unidadeId", unidadeId);

            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
