package com.franquias.api.controllers;

import com.franquias.api.dtos.ProdutoCreateRequest;
import com.franquias.api.dtos.ProdutoResponse;
import com.franquias.api.dtos.ProdutoStatusRequest;
import com.franquias.api.dtos.ProdutoUpdateRequest;
import com.franquias.api.models.ProdutoServico;
import com.franquias.api.models.enums.StatusProduto;
import com.franquias.api.services.ProdutoServicoService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class ProdutoController {

    private final ProdutoServicoService service = new ProdutoServicoService();

    public void cadastrar(Context ctx) {
        ProdutoCreateRequest dto = ctx.bodyAsClass(ProdutoCreateRequest.class);
        ProdutoServico produto = service.cadastrar(dto);
        ctx.status(201).json(ProdutoResponse.fromEntity(produto));
    }

    /**
     * Lista/filtra produtos. Aceita filtros opcionais via query string:
     * ?nome=&categoriaId=&status=ATIVO|INATIVO
     */
    public void listar(Context ctx) {
        String nome = ctx.queryParam("nome");
        String categoriaIdParam = ctx.queryParam("categoriaId");
        String statusParam = ctx.queryParam("status");

        Long categoriaId = categoriaIdParam != null && !categoriaIdParam.isBlank()
                ? Long.valueOf(categoriaIdParam)
                : null;
        StatusProduto status = statusParam != null && !statusParam.isBlank()
                ? StatusProduto.valueOf(statusParam.toUpperCase())
                : null;

        List<ProdutoResponse> lista = service.buscar(nome, categoriaId, status).stream()
                .map(ProdutoResponse::fromEntity)
                .collect(Collectors.toList());

        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(ProdutoResponse.fromEntity(service.buscarPorId(id)));
    }

    public void atualizar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ProdutoUpdateRequest dto = ctx.bodyAsClass(ProdutoUpdateRequest.class);
        ProdutoServico produto = service.atualizar(id, dto);
        ctx.json(ProdutoResponse.fromEntity(produto));
    }

    public void alterarStatus(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ProdutoStatusRequest dto = ctx.bodyAsClass(ProdutoStatusRequest.class);
        ProdutoServico produto = service.alterarStatus(id, dto.getStatus());
        ctx.json(ProdutoResponse.fromEntity(produto));
    }
}
