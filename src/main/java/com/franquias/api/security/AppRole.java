package com.franquias.api.security;

import io.javalin.security.RouteRole;

// ANYONE = rota pública; AUTHENTICATED = qualquer perfil logado; os demais espelham Perfil
public enum AppRole implements RouteRole {
    ANYONE,
    AUTHENTICATED,
    ADMIN_FRANQUEADORA,
    GESTOR_UNIDADE,
    OPERADOR
}
