package com.franquias.api.security;

import com.franquias.api.exceptions.ForbiddenException;
import com.franquias.api.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.jsonwebtoken.Claims;

public final class ApiAccessManager {

    private ApiAccessManager() {
    }

    public static void checarAcesso(Context ctx) {
        var routeRoles = ctx.routeRoles();

        if (routeRoles.isEmpty() || routeRoles.contains(AppRole.ANYONE)) {
            return;
        }

        String header = ctx.header("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token de acesso ausente. Faça login em POST /api/auth/login.");
        }

        String token = header.substring("Bearer ".length()).trim();

        Claims claims;
        try {
            claims = JwtUtil.validarToken(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Token inválido ou expirado. Faça login novamente.");
        }

        Long usuarioId = Long.valueOf(claims.getSubject());
        String perfilStr = claims.get("perfil", String.class);
        AppRole perfilUsuario = AppRole.valueOf(perfilStr);

        ctx.attribute("usuarioId", usuarioId);
        ctx.attribute("perfil", perfilUsuario);

        Object unidadeIdClaim = claims.get("unidadeId");
        if (unidadeIdClaim != null) {
            ctx.attribute("unidadeId", ((Number) unidadeIdClaim).longValue());
        }

        boolean autorizado = routeRoles.contains(AppRole.AUTHENTICATED) || routeRoles.contains(perfilUsuario);

        if (!autorizado) {
            throw new ForbiddenException(
                    "Seu perfil (" + perfilUsuario + ") não tem permissão para acessar este recurso.");
        }
    }
}
