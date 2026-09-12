-- =========================================================
-- V1 - Criação do schema inicial do sistema de franquias
-- =========================================================

CREATE TABLE franqueadora (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    razao_social    VARCHAR(150) NOT NULL,
    cnpj            VARCHAR(18)  NOT NULL UNIQUE,
    data_fundacao   DATE,
    ativa           BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE franqueado (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(150) NOT NULL,
    cpf_cnpj    VARCHAR(18)  NOT NULL UNIQUE,
    email       VARCHAR(150),
    telefone    VARCHAR(20)
);

CREATE TABLE unidade_franqueada (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    franqueadora_id BIGINT       NOT NULL REFERENCES franqueadora(id),
    franqueado_id   BIGINT       NOT NULL REFERENCES franqueado(id),
    nome_fantasia   VARCHAR(150) NOT NULL,
    cnpj            VARCHAR(18)  NOT NULL UNIQUE,
    endereco        VARCHAR(200),
    cidade          VARCHAR(100),
    estado          VARCHAR(2),
    telefone        VARCHAR(20),
    email           VARCHAR(150),
    data_inicio     DATE         NOT NULL,
    situacao        VARCHAR(20)  NOT NULL DEFAULT 'ATIVA'
);

CREATE TABLE usuario (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(150) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    senha_hash  VARCHAR(255) NOT NULL,
    perfil      VARCHAR(30)  NOT NULL,
    unidade_id  BIGINT       REFERENCES unidade_franqueada(id),
    ativo       BOOLEAN      NOT NULL DEFAULT TRUE,
    criado_em   TIMESTAMP    NOT NULL
);

CREATE TABLE categoria (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL UNIQUE,
    descricao   VARCHAR(255)
);

CREATE TABLE produto_servico (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome            VARCHAR(150)   NOT NULL,
    descricao       VARCHAR(255),
    categoria_id    BIGINT         NOT NULL REFERENCES categoria(id),
    preco_base      DECIMAL(12,2)  NOT NULL,
    status          VARCHAR(20)    NOT NULL DEFAULT 'ATIVO'
);

CREATE TABLE fornecedor (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(150) NOT NULL,
    cnpj        VARCHAR(18)  NOT NULL UNIQUE,
    contato     VARCHAR(150),
    telefone    VARCHAR(20),
    email       VARCHAR(150),
    status      VARCHAR(20)  NOT NULL DEFAULT 'ATIVO'
);

CREATE TABLE fornecedor_produto (
    fornecedor_id       BIGINT NOT NULL REFERENCES fornecedor(id),
    produto_servico_id  BIGINT NOT NULL REFERENCES produto_servico(id),
    PRIMARY KEY (fornecedor_id, produto_servico_id)
);

CREATE TABLE estoque (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    unidade_id          BIGINT  NOT NULL REFERENCES unidade_franqueada(id),
    produto_servico_id  BIGINT  NOT NULL REFERENCES produto_servico(id),
    quantidade_atual    INT     NOT NULL DEFAULT 0,
    quantidade_minima   INT     NOT NULL DEFAULT 0,
    UNIQUE (unidade_id, produto_servico_id)
);

CREATE TABLE movimentacao_estoque (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    estoque_id          BIGINT      NOT NULL REFERENCES estoque(id),
    tipo                VARCHAR(10) NOT NULL,
    quantidade          INT         NOT NULL,
    data_movimentacao   TIMESTAMP   NOT NULL,
    observacao          VARCHAR(255),
    fornecedor_id       BIGINT      REFERENCES fornecedor(id)
);

CREATE TABLE venda (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    unidade_id      BIGINT          NOT NULL REFERENCES unidade_franqueada(id),
    usuario_id      BIGINT          NOT NULL REFERENCES usuario(id),
    data_venda      TIMESTAMP       NOT NULL,
    valor_total     DECIMAL(12,2)   NOT NULL DEFAULT 0
);

CREATE TABLE item_venda (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    venda_id            BIGINT          NOT NULL REFERENCES venda(id),
    produto_servico_id  BIGINT          NOT NULL REFERENCES produto_servico(id),
    quantidade          INT             NOT NULL,
    preco_unitario      DECIMAL(12,2)   NOT NULL,
    subtotal            DECIMAL(12,2)   NOT NULL
);

CREATE TABLE royalty (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    unidade_id              BIGINT          NOT NULL REFERENCES unidade_franqueada(id),
    periodo_referencia      VARCHAR(7)      NOT NULL,
    percentual_aplicado     DECIMAL(5,2)    NOT NULL,
    faturamento_base        DECIMAL(12,2)   NOT NULL,
    valor_calculado         DECIMAL(12,2)   NOT NULL,
    situacao_pagamento      VARCHAR(20)     NOT NULL DEFAULT 'PENDENTE',
    data_pagamento          DATE,
    UNIQUE (unidade_id, periodo_referencia)
);

CREATE TABLE chamado_suporte (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    unidade_id              BIGINT          NOT NULL REFERENCES unidade_franqueada(id),
    usuario_abertura_id     BIGINT          NOT NULL REFERENCES usuario(id),
    categoria               VARCHAR(100)    NOT NULL,
    prioridade              VARCHAR(10)     NOT NULL,
    descricao               VARCHAR(1000)   NOT NULL,
    status                  VARCHAR(20)     NOT NULL DEFAULT 'ABERTO',
    data_abertura           TIMESTAMP       NOT NULL,
    data_fechamento         TIMESTAMP
);

-- Índices úteis para os filtros e relatórios exigidos
CREATE INDEX idx_unidade_situacao ON unidade_franqueada(situacao);
CREATE INDEX idx_venda_unidade_data ON venda(unidade_id, data_venda);
CREATE INDEX idx_chamado_status ON chamado_suporte(status);
CREATE INDEX idx_chamado_prioridade ON chamado_suporte(prioridade);
