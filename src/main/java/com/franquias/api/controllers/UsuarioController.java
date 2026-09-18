package com.franquias.api.controllers;

import com.franquias.api.dtos.UsuarioCreateRequest;
import com.franquias.api.dtos.UsuarioResponse;
import com.franquias.api.models.Usuario;
import com.franquias.api.services.UsuarioService;
import io.javalin.http.Context;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioController {

    private final UsuarioService usuarioService = new UsuarioService();

    public void cadastrar(Context ctx) {
        UsuarioCreateRequest dto = ctx.bodyAsClass(UsuarioCreateRequest.class);
        Usuario usuario = usuarioService.cadastrar(dto);
        ctx.status(201).json(UsuarioResponse.fromEntity(usuario));
    }

    public void listar(Context ctx) {
        List<UsuarioResponse> lista = usuarioService.listarTodos().stream()
                .map(UsuarioResponse::fromEntity)
                .collect(Collectors.toList());
        ctx.json(lista);
    }

    public void buscarPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Usuario usuario = usuarioService.buscarPorId(id);
        ctx.json(UsuarioResponse.fromEntity(usuario));
    }

    public void ativar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        usuarioService.alterarStatus(id, true);
        ctx.status(204);
    }

    public void inativar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        usuarioService.alterarStatus(id, false);
        ctx.status(204);
    }

    public void meuPerfil(Context ctx) {
        Long usuarioId = ctx.attribute("usuarioId");
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        ctx.json(UsuarioResponse.fromEntity(usuario));
    }
}
