package com.franquias.api.controllers;

import com.franquias.api.dtos.EstoqueMinimoRequest;
import com.franquias.api.dtos.EstoqueResponse;
import com.franquias.api.dtos.MovimentacaoRequest;
import com.franquias.api.dtos.MovimentacaoResponse;
import com.franquias.api.models.Estoque;
import com.franquias.api.models.MovimentacaoEstoque;
import com.franquias.api.security.AutorizacaoUnidadeUtil;
import com.franquias.api.services.EstoqueService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class EstoqueController {

    private final EstoqueService service = new EstoqueService();

    public void registrarMovimentacao(Context ctx) {
        MovimentacaoRequest dto = ctx.bodyAsClass(MovimentacaoRequest.class);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, dto.getUnidadeId());

        MovimentacaoEstoque movimentacao = service.registrarMovimentacao(dto);
        ctx.status(201).json(MovimentacaoResponse.fromEntity(movimentacao));
    }

    /**
     * Lista/filtra estoques. ADMIN_FRANQUEADORA pode ver de qualquer unidade
     * (filtro opcional via query); GESTOR_UNIDADE/OPERADOR só veem a própria,
     * independentemente do que for passado na query.
     */
    public void listar(Context ctx) {
        Long unidadeId;
        if (AutorizacaoUnidadeUtil.isAdmin(ctx)) {
            String unidadeIdParam = ctx.queryParam("unidadeId");
            unidadeId = unidadeIdParam != null && !unidadeIdParam.isBlank() ? Long.valueOf(unidadeIdParam) : null;
        } else {
            unidadeId = AutorizacaoUnidadeUtil.unidadeIdDoToken(ctx);
        }

        String produtoIdParam = ctx.queryParam("produtoId");
        Long produtoId = produtoIdParam != null && !produtoIdParam.isBlank() ? Long.valueOf(produtoIdParam) : null;

        String abaixoMinimoParam = ctx.queryParam("abaixoMinimo");
        Boolean abaixoMinimo = abaixoMinimoParam != null ? Boolean.valueOf(abaixoMinimoParam) : null;

        List<EstoqueResponse> lista = service.buscar(unidadeId, produtoId, abaixoMinimo).stream()
                .map(EstoqueResponse::fromEntity)
                .collect(Collectors.toList());

        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Estoque estoque = service.buscarPorId(id);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, estoque.getUnidade().getId());
        ctx.json(EstoqueResponse.fromEntity(estoque));
    }

    public void atualizarMinimo(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Estoque estoque = service.buscarPorId(id);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, estoque.getUnidade().getId());

        EstoqueMinimoRequest dto = ctx.bodyAsClass(EstoqueMinimoRequest.class);
        Estoque atualizado = service.atualizarMinimo(id, dto.getQuantidadeMinima());
        ctx.json(EstoqueResponse.fromEntity(atualizado));
    }

    public void listarMovimentacoes(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Estoque estoque = service.buscarPorId(id);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, estoque.getUnidade().getId());

        List<MovimentacaoResponse> lista = service.listarMovimentacoes(id).stream()
                .map(MovimentacaoResponse::fromEntity)
                .collect(Collectors.toList());

        ctx.json(lista);
    }
}
