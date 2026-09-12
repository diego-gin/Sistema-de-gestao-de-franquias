package com.franquias.api.security;

import com.franquias.api.exceptions.ForbiddenException;
import com.franquias.api.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.jsonwebtoken.Claims;

/**
 * Verifica autenticação (JWT) e autorização (perfil) para cada rota já
 * identificada pelo roteador.
 *
 * Registrado em Main.java via app.beforeMatched(ApiAccessManager::checarAcesso).
 * As roles exigidas por cada rota são anexadas no momento do registro
 * (ex: app.get(path, handler, AppRole.ADMIN_FRANQUEADORA)) e recuperadas
 * aqui através de ctx.routeRoles().
 *
 * Nota técnica: na versão 5 do Javalin isso era feito via uma interface
 * chamada AccessManager, removida na versão 6 em favor deste hook.
 */
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
