-- =========================================================
-- V3 - Dados de exemplo (seed) para demonstrar o sistema
-- Rede fictícia "Sabor Express", com 3 unidades (1 inativa),
-- catálogo, fornecedores, estoque, vendas em 3 meses e chamados.
-- Senha de todos os usuários abaixo: "123456"
-- =========================================================

INSERT INTO franqueadora (razao_social, cnpj, data_fundacao, ativa) VALUES
('Sabor Express Franquias S.A.', '98765432000199', '2015-03-10', TRUE);

INSERT INTO franqueado (nome, cpf_cnpj, email, telefone) VALUES
('Ana Paula Ferreira', '22233344455', 'ana.ferreira@saborexpress.com', '11988887777'),
('Roberto Lima', '33344455566', 'roberto.lima@saborexpress.com', '21977776666'),
('Camila Rocha', '44455566677', 'camila.rocha@saborexpress.com', '31966665555');

INSERT INTO unidade_franqueada (franqueadora_id, franqueado_id, nome_fantasia, cnpj, endereco, cidade, estado, telefone, email, data_inicio, situacao) VALUES (
  (SELECT id FROM franqueadora WHERE cnpj = '98765432000199'),
  (SELECT id FROM franqueado WHERE cpf_cnpj = '22233344455'),
  'Sabor Express - Paulista', '11122233000144', 'Av. Paulista, 1500', 'São Paulo', 'SP', '1133221100', 'paulista@saborexpress.com', '2020-06-01', 'ATIVA'
);
INSERT INTO unidade_franqueada (franqueadora_id, franqueado_id, nome_fantasia, cnpj, endereco, cidade, estado, telefone, email, data_inicio, situacao) VALUES (
  (SELECT id FROM franqueadora WHERE cnpj = '98765432000199'),
  (SELECT id FROM franqueado WHERE cpf_cnpj = '33344455566'),
  'Sabor Express - Copacabana', '22233344000155', 'Av. Atlântica, 800', 'Rio de Janeiro', 'RJ', '2122221100', 'copacabana@saborexpress.com', '2021-02-15', 'ATIVA'
);
INSERT INTO unidade_franqueada (franqueadora_id, franqueado_id, nome_fantasia, cnpj, endereco, cidade, estado, telefone, email, data_inicio, situacao) VALUES (
  (SELECT id FROM franqueadora WHERE cnpj = '98765432000199'),
  (SELECT id FROM franqueado WHERE cpf_cnpj = '44455566677'),
  'Sabor Express - Savassi', '33344455000166', 'Rua Pernambuco, 300', 'Belo Horizonte', 'MG', '3133221100', 'savassi@saborexpress.com', '2019-11-20', 'INATIVA'
);

INSERT INTO usuario (nome, email, senha_hash, perfil, unidade_id, ativo, criado_em) VALUES
('Fernanda Souza', 'fernanda.souza@saborexpress.com', '$2a$10$xIk6RimNpMFaELUHANGXtOU/4I81ZEJXLegwW2rgWeWABao7KMZj2', 'GESTOR_UNIDADE', (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), TRUE, CURRENT_TIMESTAMP),
('Lucas Almeida', 'lucas.almeida@saborexpress.com', '$2a$10$xIk6RimNpMFaELUHANGXtOU/4I81ZEJXLegwW2rgWeWABao7KMZj2', 'OPERADOR', (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), TRUE, CURRENT_TIMESTAMP),
('Patrícia Nunes', 'patricia.nunes@saborexpress.com', '$2a$10$xIk6RimNpMFaELUHANGXtOU/4I81ZEJXLegwW2rgWeWABao7KMZj2', 'GESTOR_UNIDADE', (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), TRUE, CURRENT_TIMESTAMP);

INSERT INTO categoria (nome, descricao) VALUES
('Lanches', 'Sanduíches e lanches em geral'),
('Bebidas', 'Bebidas geladas e sucos'),
('Sobremesas', 'Doces e sobremesas'),
('Combos', 'Combinações promocionais');

INSERT INTO produto_servico (nome, descricao, categoria_id, preco_base, status) VALUES
('X-Burger Clássico', 'Pão, hambúrguer, queijo e salada', (SELECT id FROM categoria WHERE nome = 'Lanches'), 18.90, 'ATIVO'),
('X-Salada', 'Pão, hambúrguer, queijo, alface e tomate', (SELECT id FROM categoria WHERE nome = 'Lanches'), 19.90, 'ATIVO'),
('X-Bacon', 'Pão, hambúrguer, queijo e bacon crocante', (SELECT id FROM categoria WHERE nome = 'Lanches'), 21.90, 'ATIVO'),
('Refrigerante Lata 350ml', 'Diversos sabores', (SELECT id FROM categoria WHERE nome = 'Bebidas'), 6.50, 'ATIVO'),
('Suco Natural 400ml', 'Sucos de frutas naturais', (SELECT id FROM categoria WHERE nome = 'Bebidas'), 8.90, 'ATIVO'),
('Água Mineral 500ml', 'Com ou sem gás', (SELECT id FROM categoria WHERE nome = 'Bebidas'), 4.00, 'ATIVO'),
('Milk-shake 400ml', 'Diversos sabores', (SELECT id FROM categoria WHERE nome = 'Sobremesas'), 12.90, 'ATIVO'),
('Brownie com Sorvete', 'Brownie quente com sorvete de creme', (SELECT id FROM categoria WHERE nome = 'Sobremesas'), 14.50, 'ATIVO'),
('Combo X-Burger + Batata + Refri', 'Combo completo', (SELECT id FROM categoria WHERE nome = 'Combos'), 32.90, 'ATIVO'),
('Combo Kids', 'Combo infantil', (SELECT id FROM categoria WHERE nome = 'Combos'), 24.90, 'ATIVO');

INSERT INTO fornecedor (nome, cnpj, contato, telefone, email, status) VALUES
('Distribuidora Carnes Premium', '55566677000188', 'José Martins', '1140028922', 'contato@carnespremium.com', 'ATIVO'),
('Bebidas & Cia Distribuidora', '66677788000199', 'Renata Alves', '1140028933', 'vendas@bebidasecia.com', 'ATIVO');

INSERT INTO fornecedor_produto (fornecedor_id, produto_servico_id) VALUES
((SELECT id FROM fornecedor WHERE cnpj = '55566677000188'), (SELECT id FROM produto_servico WHERE nome = 'X-Burger Clássico')),
((SELECT id FROM fornecedor WHERE cnpj = '55566677000188'), (SELECT id FROM produto_servico WHERE nome = 'X-Salada')),
((SELECT id FROM fornecedor WHERE cnpj = '55566677000188'), (SELECT id FROM produto_servico WHERE nome = 'X-Bacon')),
((SELECT id FROM fornecedor WHERE cnpj = '66677788000199'), (SELECT id FROM produto_servico WHERE nome = 'Refrigerante Lata 350ml')),
((SELECT id FROM fornecedor WHERE cnpj = '66677788000199'), (SELECT id FROM produto_servico WHERE nome = 'Suco Natural 400ml')),
((SELECT id FROM fornecedor WHERE cnpj = '66677788000199'), (SELECT id FROM produto_servico WHERE nome = 'Água Mineral 500ml'));

INSERT INTO estoque (unidade_id, produto_servico_id, quantidade_atual, quantidade_minima) VALUES
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'X-Burger Clássico'), 40, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'X-Salada'), 35, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'X-Bacon'), 8, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'Refrigerante Lata 350ml'), 100, 20),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'Suco Natural 400ml'), 15, 15),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'Água Mineral 500ml'), 5, 15),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'Milk-shake 400ml'), 20, 8),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'Brownie com Sorvete'), 18, 8),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'Combo X-Burger + Batata + Refri'), 25, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM produto_servico WHERE nome = 'Combo Kids'), 22, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'X-Burger Clássico'), 40, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'X-Salada'), 35, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'X-Bacon'), 8, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'Refrigerante Lata 350ml'), 100, 20),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'Suco Natural 400ml'), 15, 15),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'Água Mineral 500ml'), 5, 15),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'Milk-shake 400ml'), 20, 8),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'Brownie com Sorvete'), 18, 8),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'Combo X-Burger + Batata + Refri'), 25, 10),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM produto_servico WHERE nome = 'Combo Kids'), 22, 10);

INSERT INTO movimentacao_estoque (estoque_id, tipo, quantidade, data_movimentacao, observacao, fornecedor_id) VALUES
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'X-Burger Clássico')), 'ENTRADA', 40, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '55566677000188')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'X-Salada')), 'ENTRADA', 35, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '55566677000188')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'X-Bacon')), 'ENTRADA', 8, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '55566677000188')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Refrigerante Lata 350ml')), 'ENTRADA', 100, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '66677788000199')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Suco Natural 400ml')), 'ENTRADA', 15, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '66677788000199')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Água Mineral 500ml')), 'ENTRADA', 5, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '66677788000199')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Milk-shake 400ml')), 'ENTRADA', 20, '2026-07-01 09:00:00', 'Estoque inicial', NULL),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Brownie com Sorvete')), 'ENTRADA', 18, '2026-07-01 09:00:00', 'Estoque inicial', NULL),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Combo X-Burger + Batata + Refri')), 'ENTRADA', 25, '2026-07-01 09:00:00', 'Estoque inicial', NULL),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Combo Kids')), 'ENTRADA', 22, '2026-07-01 09:00:00', 'Estoque inicial', NULL),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'X-Burger Clássico')), 'ENTRADA', 40, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '55566677000188')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'X-Salada')), 'ENTRADA', 35, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '55566677000188')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'X-Bacon')), 'ENTRADA', 8, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '55566677000188')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Refrigerante Lata 350ml')), 'ENTRADA', 100, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '66677788000199')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Suco Natural 400ml')), 'ENTRADA', 15, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '66677788000199')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Água Mineral 500ml')), 'ENTRADA', 5, '2026-07-01 09:00:00', 'Estoque inicial', (SELECT id FROM fornecedor WHERE cnpj = '66677788000199')),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Milk-shake 400ml')), 'ENTRADA', 20, '2026-07-01 09:00:00', 'Estoque inicial', NULL),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Brownie com Sorvete')), 'ENTRADA', 18, '2026-07-01 09:00:00', 'Estoque inicial', NULL),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Combo X-Burger + Batata + Refri')), 'ENTRADA', 25, '2026-07-01 09:00:00', 'Estoque inicial', NULL),
((SELECT id FROM estoque WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND produto_servico_id = (SELECT id FROM produto_servico WHERE nome = 'Combo Kids')), 'ENTRADA', 22, '2026-07-01 09:00:00', 'Estoque inicial', NULL);
-- Vendas e itens
INSERT INTO venda (unidade_id, usuario_id, data_venda, valor_total) VALUES (
  (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'),
  (SELECT id FROM usuario WHERE email = 'fernanda.souza@saborexpress.com'),
  '2026-07-15 12:30:00', 50.80
);
INSERT INTO item_venda (venda_id, produto_servico_id, quantidade, preco_unitario, subtotal) VALUES
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND data_venda = '2026-07-15 12:30:00'), (SELECT id FROM produto_servico WHERE nome = 'X-Burger Clássico'), 2, 18.90, 37.80),
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND data_venda = '2026-07-15 12:30:00'), (SELECT id FROM produto_servico WHERE nome = 'Refrigerante Lata 350ml'), 2, 6.50, 13.00);

INSERT INTO venda (unidade_id, usuario_id, data_venda, valor_total) VALUES (
  (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'),
  (SELECT id FROM usuario WHERE email = 'lucas.almeida@saborexpress.com'),
  '2026-08-02 13:15:00', 98.70
);
INSERT INTO item_venda (venda_id, produto_servico_id, quantidade, preco_unitario, subtotal) VALUES
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND data_venda = '2026-08-02 13:15:00'), (SELECT id FROM produto_servico WHERE nome = 'Combo X-Burger + Batata + Refri'), 3, 32.90, 98.70);

INSERT INTO venda (unidade_id, usuario_id, data_venda, valor_total) VALUES (
  (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'),
  (SELECT id FROM usuario WHERE email = 'fernanda.souza@saborexpress.com'),
  '2026-08-20 19:45:00', 45.30
);
INSERT INTO item_venda (venda_id, produto_servico_id, quantidade, preco_unitario, subtotal) VALUES
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND data_venda = '2026-08-20 19:45:00'), (SELECT id FROM produto_servico WHERE nome = 'X-Bacon'), 1, 21.90, 21.90),
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND data_venda = '2026-08-20 19:45:00'), (SELECT id FROM produto_servico WHERE nome = 'Suco Natural 400ml'), 1, 8.90, 8.90),
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144') AND data_venda = '2026-08-20 19:45:00'), (SELECT id FROM produto_servico WHERE nome = 'Brownie com Sorvete'), 1, 14.50, 14.50);

INSERT INTO venda (unidade_id, usuario_id, data_venda, valor_total) VALUES (
  (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'),
  (SELECT id FROM usuario WHERE email = 'patricia.nunes@saborexpress.com'),
  '2026-07-22 12:00:00', 95.60
);
INSERT INTO item_venda (venda_id, produto_servico_id, quantidade, preco_unitario, subtotal) VALUES
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND data_venda = '2026-07-22 12:00:00'), (SELECT id FROM produto_servico WHERE nome = 'X-Salada'), 4, 19.90, 79.60),
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND data_venda = '2026-07-22 12:00:00'), (SELECT id FROM produto_servico WHERE nome = 'Água Mineral 500ml'), 4, 4.00, 16.00);

INSERT INTO venda (unidade_id, usuario_id, data_venda, valor_total) VALUES (
  (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'),
  (SELECT id FROM usuario WHERE email = 'patricia.nunes@saborexpress.com'),
  '2026-08-10 20:10:00', 75.60
);
INSERT INTO item_venda (venda_id, produto_servico_id, quantidade, preco_unitario, subtotal) VALUES
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND data_venda = '2026-08-10 20:10:00'), (SELECT id FROM produto_servico WHERE nome = 'Combo Kids'), 2, 24.90, 49.80),
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND data_venda = '2026-08-10 20:10:00'), (SELECT id FROM produto_servico WHERE nome = 'Milk-shake 400ml'), 2, 12.90, 25.80);

INSERT INTO venda (unidade_id, usuario_id, data_venda, valor_total) VALUES (
  (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'),
  (SELECT id FROM usuario WHERE email = 'patricia.nunes@saborexpress.com'),
  '2026-09-05 18:30:00', 32.50
);
INSERT INTO item_venda (venda_id, produto_servico_id, quantidade, preco_unitario, subtotal) VALUES
((SELECT id FROM venda WHERE unidade_id = (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155') AND data_venda = '2026-09-05 18:30:00'), (SELECT id FROM produto_servico WHERE nome = 'Refrigerante Lata 350ml'), 5, 6.50, 32.50);

-- Royalties (agosto/2026), percentual 5%
INSERT INTO royalty (unidade_id, periodo_referencia, percentual_aplicado, faturamento_base, valor_calculado, situacao_pagamento, data_pagamento) VALUES (
  (SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'),
  '2026-08', 5.00, 144.00, 7.20, 'PAGO', '2026-09-05'
);

INSERT INTO royalty (unidade_id, periodo_referencia, percentual_aplicado, faturamento_base, valor_calculado, situacao_pagamento, data_pagamento) VALUES (
  (SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'),
  '2026-08', 5.00, 75.60, 3.78, 'PENDENTE', NULL
);

-- Chamados de suporte
INSERT INTO chamado_suporte (unidade_id, usuario_abertura_id, categoria, prioridade, descricao, status, data_abertura, data_fechamento) VALUES
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM usuario WHERE email = 'fernanda.souza@saborexpress.com'), 'Estoque', 'ALTA', 'Estoque de X-Bacon abaixo do minimo, favor providenciar reposicao urgente.', 'ABERTO', '2026-09-10 09:00:00', NULL),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM usuario WHERE email = 'patricia.nunes@saborexpress.com'), 'Financeiro', 'MEDIA', 'Duvida sobre o calculo do royalty de agosto.', 'EM_ANDAMENTO', '2026-09-06 10:00:00', NULL),
((SELECT id FROM unidade_franqueada WHERE cnpj = '11122233000144'), (SELECT id FROM usuario WHERE email = 'lucas.almeida@saborexpress.com'), 'Sistema', 'BAIXA', 'Impressora de comandas apresentando lentidao ocasional.', 'RESOLVIDO', '2026-08-25 14:00:00', NULL),
((SELECT id FROM unidade_franqueada WHERE cnpj = '22233344000155'), (SELECT id FROM usuario WHERE email = 'patricia.nunes@saborexpress.com'), 'Equipamento', 'URGENTE', 'Freezer da unidade parou de funcionar durante a madrugada.', 'FECHADO', '2026-08-18 03:00:00', '2026-08-18 10:00:00');
