-- =========================================================
-- V2 - Usuário administrador inicial da franqueadora
--
-- Necessário porque não existe outro jeito de criar o primeiro
-- usuário: o endpoint de cadastro de usuários exige autenticação
-- de um ADMIN_FRANQUEADORA, e login exige um usuário existente.
--
-- Credenciais (documentadas também no README):
--   e-mail: admin@franquias.com
--   senha:  admin123
--
-- O hash abaixo foi gerado com BCrypt (custo 10) para a senha "admin123".
-- =========================================================

INSERT INTO usuario (nome, email, senha_hash, perfil, unidade_id, ativo, criado_em)
VALUES (
    'Administrador da Franqueadora',
    'admin@franquias.com',
    '$2a$10$qqmhcmpP7SLwmxbrE127OuUDgp/tOecern22GCr43xAlwMWwSLs52',
    'ADMIN_FRANQUEADORA',
    NULL,
    TRUE,
    CURRENT_TIMESTAMP
);
