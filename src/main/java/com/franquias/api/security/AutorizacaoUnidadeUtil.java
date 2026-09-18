package com.franquias.api.security;

import com.franquias.api.exceptions.ForbiddenException;
import io.javalin.http.Context;

public final class AutorizacaoUnidadeUtil {

    private AutorizacaoUnidadeUtil() {
    }

    public static void garantirAcessoAUnidade(Context ctx, Long unidadeIdSolicitada) {
        AppRole perfil = ctx.attribute("perfil");
        if (perfil == AppRole.ADMIN_FRANQUEADORA) {
            return;
        }
        Long unidadeIdDoToken = ctx.attribute("unidadeId");
        if (unidadeIdDoToken == null || !unidadeIdDoToken.equals(unidadeIdSolicitada)) {
            throw new ForbiddenException("Você só pode acessar dados da sua própria unidade.");
        }
    }

    public static boolean isAdmin(Context ctx) {
        AppRole perfil = ctx.attribute("perfil");
        return perfil == AppRole.ADMIN_FRANQUEADORA;
    }

    public static Long unidadeIdDoToken(Context ctx) {
        return ctx.attribute("unidadeId");
    }
}
