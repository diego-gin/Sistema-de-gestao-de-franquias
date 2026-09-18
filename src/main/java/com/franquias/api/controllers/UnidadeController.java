package com.franquias.api.controllers;

import com.franquias.api.dtos.UnidadeCreateRequest;
import com.franquias.api.dtos.UnidadeResponse;
import com.franquias.api.dtos.UnidadeSituacaoRequest;
import com.franquias.api.dtos.UnidadeUpdateRequest;
import com.franquias.api.models.UnidadeFranqueada;
import com.franquias.api.models.enums.SituacaoUnidade;
import com.franquias.api.security.AutorizacaoUnidadeUtil;
import com.franquias.api.services.UnidadeFranqueadaService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class UnidadeController {

    private final UnidadeFranqueadaService service = new UnidadeFranqueadaService();

    public void cadastrar(Context ctx) {
        UnidadeCreateRequest dto = ctx.bodyAsClass(UnidadeCreateRequest.class);
        UnidadeFranqueada unidade = service.cadastrar(dto);
        ctx.status(201).json(UnidadeResponse.fromEntity(unidade));
    }

    public void listar(Context ctx) {
        String nome = ctx.queryParam("nome");
        String cidade = ctx.queryParam("cidade");
        String cnpj = ctx.queryParam("cnpj");
        String responsavel = ctx.queryParam("responsavel");
        String situacaoParam = ctx.queryParam("situacao");

        SituacaoUnidade situacao = situacaoParam != null && !situacaoParam.isBlank()
                ? SituacaoUnidade.valueOf(situacaoParam.toUpperCase())
                : null;

        List<UnidadeResponse> lista = service.buscar(nome, cidade, cnpj, responsavel, situacao).stream()
                .map(UnidadeResponse::fromEntity)
                .collect(Collectors.toList());

        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, id);
        ctx.json(UnidadeResponse.fromEntity(service.buscarPorId(id)));
    }

    public void atualizar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        UnidadeUpdateRequest dto = ctx.bodyAsClass(UnidadeUpdateRequest.class);
        UnidadeFranqueada unidade = service.atualizar(id, dto);
        ctx.json(UnidadeResponse.fromEntity(unidade));
    }

    public void alterarSituacao(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        UnidadeSituacaoRequest dto = ctx.bodyAsClass(UnidadeSituacaoRequest.class);
        UnidadeFranqueada unidade = service.alterarSituacao(id, dto.getSituacao());
        ctx.json(UnidadeResponse.fromEntity(unidade));
    }

    private void garantirAcessoAUnidade(Context ctx, Long unidadeIdSolicitada) {
        AutorizacaoUnidadeUtil.garantirAcessoAUnidade(ctx, unidadeIdSolicitada);
    }
}
