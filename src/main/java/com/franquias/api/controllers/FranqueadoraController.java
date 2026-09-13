package com.franquias.api.controllers;

import com.franquias.api.dtos.FranqueadoraCreateRequest;
import com.franquias.api.dtos.FranqueadoraResponse;
import com.franquias.api.models.Franqueadora;
import com.franquias.api.services.FranqueadoraService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class FranqueadoraController {

    private final FranqueadoraService service = new FranqueadoraService();

    public void cadastrar(Context ctx) {
        FranqueadoraCreateRequest dto = ctx.bodyAsClass(FranqueadoraCreateRequest.class);
        Franqueadora franqueadora = service.cadastrar(dto);
        ctx.status(201).json(FranqueadoraResponse.fromEntity(franqueadora));
    }

    public void listar(Context ctx) {
        List<FranqueadoraResponse> lista = service.listarTodas().stream()
                .map(FranqueadoraResponse::fromEntity)
                .collect(Collectors.toList());
        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        ctx.json(FranqueadoraResponse.fromEntity(service.buscarPorId(id)));
    }
}
