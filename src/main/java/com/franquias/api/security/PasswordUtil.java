package com.franquias.api.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Responsável por gerar e conferir hashes de senha (BCrypt).
 * A senha em texto puro NUNCA é armazenada — apenas o hash.
 */
public final class PasswordUtil {

    private static final int ROUNDS = 10;

    private PasswordUtil() {
    }

    public static String hash(String senhaTextoPlano) {
        return BCrypt.hashpw(senhaTextoPlano, BCrypt.gensalt(ROUNDS));
    }

    public static boolean verificar(String senhaTextoPlano, String senhaHash) {
        return BCrypt.checkpw(senhaTextoPlano, senhaHash);
    }
}
