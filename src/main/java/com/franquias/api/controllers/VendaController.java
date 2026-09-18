package com.franquias.api.controllers;

import com.franquias.api.dtos.VendaCreateRequest;
import com.franquias.api.dtos.VendaResponse;
import com.franquias.api.models.Venda;
import com.franquias.api.security.AutorizacaoUnidadeUtil;
import com.franquias.api.services.VendaService;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class VendaController {

    private final VendaService service = new VendaService();

    public void registrar(Context ctx) {
        VendaCreateRequest dto = ctx.bodyAsClass(VendaCreateRequest.class);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, dto.getUnidadeId());

        Long usuarioId = ctx.attribute("usuarioId");
        Venda venda = service.registrarVenda(dto, usuarioId);
        ctx.status(201).json(VendaResponse.fromEntity(venda));
    }

    // ADMIN vê qualquer unidade; demais perfis só a própria
    public void listar(Context ctx) {
        Long unidadeId;
        if (AutorizacaoUnidadeUtil.isAdmin(ctx)) {
            String unidadeIdParam = ctx.queryParam("unidadeId");
            unidadeId = unidadeIdParam != null && !unidadeIdParam.isBlank() ? Long.valueOf(unidadeIdParam) : null;
        } else {
            unidadeId = AutorizacaoUnidadeUtil.unidadeIdDoToken(ctx);
        }

        String dataInicioParam = ctx.queryParam("dataInicio");
        String dataFimParam = ctx.queryParam("dataFim");
        LocalDate dataInicio = dataInicioParam != null && !dataInicioParam.isBlank()
                ? LocalDate.parse(dataInicioParam) : null;
        LocalDate dataFim = dataFimParam != null && !dataFimParam.isBlank()
                ? LocalDate.parse(dataFimParam) : null;

        List<VendaResponse> lista = service.buscar(unidadeId, dataInicio, dataFim).stream()
                .map(VendaResponse::fromEntity)
                .collect(Collectors.toList());

        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Venda venda = service.buscarPorId(id);
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, venda.getUnidade().getId());
        ctx.json(VendaResponse.fromEntity(venda));
    }
}
