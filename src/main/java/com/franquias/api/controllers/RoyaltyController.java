package com.franquias.api.controllers;

import com.franquias.api.dtos.RoyaltyCalcularRequest;
import com.franquias.api.dtos.RoyaltyPagamentoRequest;
import com.franquias.api.dtos.RoyaltyResponse;
import com.franquias.api.models.Royalty;
import com.franquias.api.models.enums.SituacaoPagamento;
import com.franquias.api.security.AutorizacaoUnidadeUtil;
import com.franquias.api.services.RoyaltyService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class RoyaltyController {

    private final RoyaltyService service = new RoyaltyService();

    public void calcular(Context ctx) {
        RoyaltyCalcularRequest dto = ctx.bodyAsClass(RoyaltyCalcularRequest.class);
        Royalty royalty = service.calcular(dto);
        ctx.status(201).json(RoyaltyResponse.fromEntity(royalty));
    }

    public void listar(Context ctx) {
        Long unidadeId;
        if (AutorizacaoUnidadeUtil.isAdmin(ctx)) {
            String unidadeIdParam = ctx.queryParam("unidadeId");
            unidadeId = unidadeIdParam != null && !unidadeIdParam.isBlank() ? Long.valueOf(unidadeIdParam) : null;
        } else {
            unidadeId = AutorizacaoUnidadeUtil.unidadeIdDoToken(ctx);
        }

        String periodo = ctx.queryParam("periodo");
        String situacaoParam = ctx.queryParam("situacaoPagamento");
        SituacaoPagamento situacao = situacaoParam != null && !situacaoParam.isBlank()
                ? SituacaoPagamento.valueOf(situacaoParam.toUpperCase())
                : null;

        List<RoyaltyResponse> lista = service.buscar(unidadeId, periodo, situacao).stream()
                .map(RoyaltyResponse::fromEntity)
                .collect(Collectors.toList());

        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Royalty royalty = service.buscarPorId(id);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, royalty.getUnidade().getId());
        ctx.json(RoyaltyResponse.fromEntity(royalty));
    }

    public void registrarPagamento(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        RoyaltyPagamentoRequest dto = ctx.bodyAsClass(RoyaltyPagamentoRequest.class);
        Royalty royalty = service.registrarPagamento(id, dto);
        ctx.json(RoyaltyResponse.fromEntity(royalty));
    }
}
