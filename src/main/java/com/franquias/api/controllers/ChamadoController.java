package com.franquias.api.controllers;

import com.franquias.api.dtos.ChamadoCreateRequest;
import com.franquias.api.dtos.ChamadoResponse;
import com.franquias.api.dtos.ChamadoStatusRequest;
import com.franquias.api.models.ChamadoSuporte;
import com.franquias.api.models.enums.PrioridadeChamado;
import com.franquias.api.models.enums.StatusChamado;
import com.franquias.api.security.AutorizacaoUnidadeUtil;
import com.franquias.api.services.ChamadoSuporteService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class ChamadoController {

    private final ChamadoSuporteService service = new ChamadoSuporteService();

    public void abrir(Context ctx) {
        ChamadoCreateRequest dto = ctx.bodyAsClass(ChamadoCreateRequest.class);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, dto.getUnidadeId());

        Long usuarioId = ctx.attribute("usuarioId");
        ChamadoSuporte chamado = service.abrir(dto, usuarioId);
        ctx.status(201).json(ChamadoResponse.fromEntity(chamado));
    }

    public void listar(Context ctx) {
        Long unidadeId;
        if (AutorizacaoUnidadeUtil.isAdmin(ctx)) {
            String unidadeIdParam = ctx.queryParam("unidadeId");
            unidadeId = unidadeIdParam != null && !unidadeIdParam.isBlank() ? Long.valueOf(unidadeIdParam) : null;
        } else {
            unidadeId = AutorizacaoUnidadeUtil.unidadeIdDoToken(ctx);
        }

        String statusParam = ctx.queryParam("status");
        String prioridadeParam = ctx.queryParam("prioridade");
        StatusChamado status = statusParam != null && !statusParam.isBlank()
                ? StatusChamado.valueOf(statusParam.toUpperCase()) : null;
        PrioridadeChamado prioridade = prioridadeParam != null && !prioridadeParam.isBlank()
                ? PrioridadeChamado.valueOf(prioridadeParam.toUpperCase()) : null;

        List<ChamadoResponse> lista = service.buscar(unidadeId, status, prioridade).stream()
                .map(ChamadoResponse::fromEntity)
                .collect(Collectors.toList());

        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ChamadoSuporte chamado = service.buscarPorId(id);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, chamado.getUnidade().getId());
        ctx.json(ChamadoResponse.fromEntity(chamado));
    }

    public void atualizarStatus(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ChamadoStatusRequest dto = ctx.bodyAsClass(ChamadoStatusRequest.class);
        ChamadoSuporte chamado = service.atualizarStatus(id, dto.getStatus());
        ctx.json(ChamadoResponse.fromEntity(chamado));
    }
}
