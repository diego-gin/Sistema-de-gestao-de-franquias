package com.franquias.api.controllers;

import com.franquias.api.dtos.CategoriaCreateRequest;
import com.franquias.api.dtos.CategoriaResponse;
import com.franquias.api.models.Categoria;
import com.franquias.api.services.CategoriaService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class CategoriaController {

    private final CategoriaService service = new CategoriaService();

    public void cadastrar(Context ctx) {
        CategoriaCreateRequest dto = ctx.bodyAsClass(CategoriaCreateRequest.class);
        Categoria categoria = service.cadastrar(dto);
        ctx.status(201).json(CategoriaResponse.fromEntity(categoria));
    }

    public void listar(Context ctx) {
        List<CategoriaResponse> lista = service.listarTodas().stream()
                .map(CategoriaResponse::fromEntity)
                .collect(Collectors.toList());
        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(CategoriaResponse.fromEntity(service.buscarPorId(id)));
    }
}
