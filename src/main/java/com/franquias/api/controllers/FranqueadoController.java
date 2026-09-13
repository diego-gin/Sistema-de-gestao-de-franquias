package com.franquias.api.controllers;

import com.franquias.api.dtos.FranqueadoCreateRequest;
import com.franquias.api.dtos.FranqueadoResponse;
import com.franquias.api.models.Franqueado;
import com.franquias.api.services.FranqueadoService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class FranqueadoController {

    private final FranqueadoService service = new FranqueadoService();

    public void cadastrar(Context ctx) {
        FranqueadoCreateRequest dto = ctx.bodyAsClass(FranqueadoCreateRequest.class);
        Franqueado franqueado = service.cadastrar(dto);
        ctx.status(201).json(FranqueadoResponse.fromEntity(franqueado));
    }

    public void listar(Context ctx) {
        List<FranqueadoResponse> lista = service.listarTodos().stream()
                .map(FranqueadoResponse::fromEntity)
                .collect(Collectors.toList());
        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(FranqueadoResponse.fromEntity(service.buscarPorId(id)));
    }
}
