package com.franquias.api.security;

import com.franquias.api.models.enums.Perfil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public final class JwtUtil {

    private static final SecretKey CHAVE = Keys.hmacShaKeyFor(
            "franquias-api-chave-secreta-para-assinatura-jwt-2026-troque-em-producao"
                    .getBytes(StandardCharsets.UTF_8)
    );

    private static final long VALIDADE_MS = 8 * 60 * 60 * 1000L; // 8 horas

    private JwtUtil() {
    }

    public static String gerarToken(Long usuarioId, Perfil perfil, Long unidadeId) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + VALIDADE_MS);

        JwtBuilder builder = Jwts.builder()
                .subject(String.valueOf(usuarioId))
                .claim("perfil", perfil.name())
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(CHAVE);

        if (unidadeId != null) {
            builder.claim("unidadeId", unidadeId);
        }

        return builder.compact();
    }

    public static Claims validarToken(String token) {
        Jws<Claims> jws = Jwts.parser()
                .verifyWith(CHAVE)
                .build()
                .parseSignedClaims(token);
        return jws.getPayload();
    }
}
