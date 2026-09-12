package com.franquias.api.models.enums;

/**
 * Perfis de acesso do sistema.
 * ADMIN_FRANQUEADORA: acesso total, gerencia a rede inteira.
 * GESTOR_UNIDADE: gerencia uma unidade específica (vendas, estoque, chamados).
 * OPERADOR: acesso operacional restrito dentro de uma unidade (ex: registrar vendas).
 */
public enum Perfil {
    ADMIN_FRANQUEADORA,
    GESTOR_UNIDADE,
    OPERADOR
}
