package com.franquias.api.controllers;

import com.franquias.api.dtos.LoginRequest;
import com.franquias.api.dtos.LoginResponse;
import com.franquias.api.services.AuthService;
import io.javalin.http.Context;

public class AuthController {

    private final AuthService authService = new AuthService();

    public void login(Context ctx) {
        LoginRequest dto = ctx.bodyAsClass(LoginRequest.class);
        LoginResponse resposta = authService.autenticar(dto);
        ctx.json(resposta);
    }
}
