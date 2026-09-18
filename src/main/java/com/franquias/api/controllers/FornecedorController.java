package com.franquias.api.controllers;

import com.franquias.api.dtos.AssociarProdutosRequest;
import com.franquias.api.dtos.FornecedorCreateRequest;
import com.franquias.api.dtos.FornecedorResponse;
import com.franquias.api.dtos.FornecedorStatusRequest;
import com.franquias.api.dtos.FornecedorUpdateRequest;
import com.franquias.api.models.Fornecedor;
import com.franquias.api.models.enums.StatusFornecedor;
import com.franquias.api.services.FornecedorService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class FornecedorController {

    private final FornecedorService service = new FornecedorService();

    public void cadastrar(Context ctx) {
        FornecedorCreateRequest dto = ctx.bodyAsClass(FornecedorCreateRequest.class);
        Fornecedor fornecedor = service.cadastrar(dto);
        ctx.status(201).json(FornecedorResponse.fromEntity(fornecedor));
    }

    public void listar(Context ctx) {
        String nome = ctx.queryParam("nome");
        String cnpj = ctx.queryParam("cnpj");
        String statusParam = ctx.queryParam("status");
        StatusFornecedor status = statusParam != null && !statusParam.isBlank()
                ? StatusFornecedor.valueOf(statusParam.toUpperCase())
                : null;

        List<FornecedorResponse> lista = service.buscar(nome, cnpj, status).stream()
                .map(FornecedorResponse::fromEntity)
                .collect(Collectors.toList());

        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(FornecedorResponse.fromEntity(service.buscarPorId(id)));
    }

    public void atualizar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        FornecedorUpdateRequest dto = ctx.bodyAsClass(FornecedorUpdateRequest.class);
        Fornecedor fornecedor = service.atualizar(id, dto);
        ctx.json(FornecedorResponse.fromEntity(fornecedor));
    }

    public void alterarStatus(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        FornecedorStatusRequest dto = ctx.bodyAsClass(FornecedorStatusRequest.class);
        Fornecedor fornecedor = service.alterarStatus(id, dto.getStatus());
        ctx.json(FornecedorResponse.fromEntity(fornecedor));
    }

    public void associarProdutos(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        AssociarProdutosRequest dto = ctx.bodyAsClass(AssociarProdutosRequest.class);
        Fornecedor fornecedor = service.associarProdutos(id, dto.getProdutoIds());
        ctx.json(FornecedorResponse.fromEntity(fornecedor));
    }
}
