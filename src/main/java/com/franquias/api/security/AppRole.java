package com.franquias.api.security;

import io.javalin.security.RouteRole;

/**
 * Papéis usados pelo AccessManager do Javalin para proteger rotas.
 *
 * ANYONE:        rota pública, não exige token (ex: login).
 * AUTHENTICATED: exige um token válido, mas de qualquer perfil.
 * Os três últimos espelham o enum Perfil do domínio (Usuario).
 */
public enum AppRole implements RouteRole {
    ANYONE,
    AUTHENTICATED,
    ADMIN_FRANQUEADORA,
    GESTOR_UNIDADE,
    OPERADOR
}
