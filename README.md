# Franquias API

API REST em Java para gestão de uma rede de franquias: unidades, produtos, estoque,
vendas, royalties, fornecedores e chamados de suporte.

## Tecnologias utilizadas

- **Java 17**
- **Javalin** — servidor HTTP e roteamento
- **Hibernate/JPA** — persistência (ORM), sem Spring Data
- **H2 Database** — banco relacional em arquivo local (`./data/franquias.mv.db`), sem instalação
- **Flyway** — migrations do banco de dados (schema versionado em `src/main/resources/db/migration`)
- **Jackson** — serialização JSON
- **Hibernate Validator (Bean Validation)** — validação de dados de entrada
- **Hash de senha** com BCrypt (biblioteca `jBCrypt`)
- **JJWT** — geração e validação de tokens JWT
- **Maven** — build e gerenciamento de dependências

> Projeto construído sem frameworks de alto nível (como Spring Boot): a injeção de
> dependência, o roteamento e a fiação entre camadas são feitos manualmente, para
> reforçar o entendimento dos conceitos da disciplina.

## Requisitos para execução

- JDK 17 ou superior
- Maven 3.9+

## Como rodar

```bash
# compilar e rodar
mvn compile exec:java

# ou gerar o JAR executável e rodar
mvn clean package
java -jar target/franquias-api.jar
```

A API sobe em `http://localhost:8080`.

Ao iniciar, a aplicação roda automaticamente as migrations do Flyway, criando o
arquivo do banco H2 em `./data/franquias.mv.db` na primeira execução.

Teste rápido (PowerShell):
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/health"
Invoke-RestMethod -Uri "http://localhost:8080/api/health/db"
```

O segundo endpoint confirma que a conexão JPA/Hibernate com o banco está
funcionando (retorna a contagem de usuários cadastrados, 0 neste ponto).

## Estrutura do projeto

```
franquias-api/
├── src/main/java/com/franquias/api/
│   ├── controllers/     # Rotas HTTP (Javalin)
│   ├── models/          # Entidades (JPA)
│   ├── dtos/            # Objetos de entrada/saída da API
│   ├── services/        # Regras de negócio
│   ├── repositories/    # Acesso a dados (JPA/Hibernate)
│   ├── data/            # Configuração de conexão com o banco
│   └── config/          # Bootstrap da aplicação (Main.java)
├── src/main/resources/
│   ├── META-INF/persistence.xml   # Configuração do Hibernate/JPA
│   └── db/migration/              # Migrations Flyway (versionamento do schema)
└── pom.xml
```

## Modelo de dados

Entidades principais: `Usuario`, `Franqueadora`, `Franqueado`, `UnidadeFranqueada`,
`Categoria`, `ProdutoServico`, `Fornecedor`, `Estoque`, `MovimentacaoEstoque`,
`Venda`, `ItemVenda`, `Royalty`, `ChamadoSuporte`.

Decisões de modelagem:
- Fornecedores são globais à rede (podem atender várias unidades), associados a
  produtos/serviços via relação N—N.
- O catálogo de produtos/serviços é único para toda a rede (padronizado pela
  franqueadora), e não por unidade.
- O schema do banco é controlado exclusivamente pelo Flyway; o Hibernate está
  configurado em modo `validate` (nunca gera/altera tabelas sozinho), garantindo
  que entidades e schema fiquem sempre sincronizados de forma rastreável.

## Status do desenvolvimento

- [x] Setup inicial do projeto
- [x] Modelagem de dados e banco (entidades JPA + migration Flyway)
- [x] Usuários, autenticação e perfis (login JWT, controle de acesso por perfil)
- [x] Franqueadora, unidades e franqueados
- [x] Catálogo de produtos/serviços
- [x] Estoque por unidade (entrada/saída, saldo mínimo, impedimento de negativo)
- [x] Fornecedores
- [ ] Vendas
- [ ] Royalties/financeiro
- [ ] Chamados de suporte
- [ ] Relatórios e indicadores

## Usuário administrador inicial (seed)

Criado automaticamente pela migration `V2__seed_admin.sql`, pois é necessário
para conseguir logar e cadastrar os demais usuários:

- **E-mail:** `admin@franquias.com`
- **Senha:** `admin123`

## Autenticação

Todas as rotas (exceto `/api/health*` e `/api/auth/login`) exigem um token JWT
no header:
```
Authorization: Bearer <token>
```

### Endpoints disponíveis nesta parte

| Método | Rota                          | Perfil exigido        | Descrição                          |
|--------|-------------------------------|------------------------|-------------------------------------|
| POST   | `/api/auth/login`             | público                | Login, retorna o token JWT          |
| POST   | `/api/usuarios`                | ADMIN_FRANQUEADORA      | Cadastra um novo usuário            |
| GET    | `/api/usuarios`                | ADMIN_FRANQUEADORA      | Lista todos os usuários             |
| GET    | `/api/usuarios/me`             | qualquer autenticado    | Dados do usuário logado             |
| GET    | `/api/usuarios/{id}`           | ADMIN_FRANQUEADORA      | Busca usuário por ID                |
| PATCH  | `/api/usuarios/{id}/ativar`    | ADMIN_FRANQUEADORA      | Reativa um usuário                  |
| PATCH  | `/api/usuarios/{id}/inativar`  | ADMIN_FRANQUEADORA      | Inativa um usuário                  |

### Testando no PowerShell

```powershell
# 1. Login — guarda o token numa variável
$resposta = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
    -Method Post -ContentType "application/json" `
    -Body '{"email":"admin@franquias.com","senha":"admin123"}'
$token = $resposta.token
$resposta

# 2. Cadastrar um novo usuário (usando o token do admin)
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" `
    -Method Post -ContentType "application/json" `
    -Headers @{ Authorization = "Bearer $token" } `
    -Body '{"nome":"Maria Souza","email":"maria@franquias.com","senha":"123456","perfil":"ADMIN_FRANQUEADORA"}'

# 3. Listar usuários
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" `
    -Headers @{ Authorization = "Bearer $token" }

# 4. Ver o próprio perfil
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/me" `
    -Headers @{ Authorization = "Bearer $token" }
```

## Parte 4 — Franqueadora, Unidades e Franqueados

| Método | Rota                              | Perfil exigido                          | Descrição                    |
|--------|------------------------------------|-------------------------------------------|--------------------------------|
| POST   | `/api/franqueadoras`               | ADMIN_FRANQUEADORA                        | Cadastra a franqueadora (rede) |
| GET    | `/api/franqueadoras`               | ADMIN_FRANQUEADORA                        | Lista franqueadoras            |
| GET    | `/api/franqueadoras/{id}`          | ADMIN_FRANQUEADORA                        | Busca por ID                   |
| POST   | `/api/franqueados`                 | ADMIN_FRANQUEADORA                        | Cadastra um responsável        |
| GET    | `/api/franqueados`                 | ADMIN_FRANQUEADORA                        | Lista franqueados               |
| GET    | `/api/franqueados/{id}`            | ADMIN_FRANQUEADORA                        | Busca por ID                   |
| POST   | `/api/unidades`                    | ADMIN_FRANQUEADORA                        | Cadastra uma unidade            |
| GET    | `/api/unidades`                    | ADMIN_FRANQUEADORA                        | Lista/filtra unidades           |
| GET    | `/api/unidades/{id}`               | ADMIN, GESTOR_UNIDADE\*, OPERADOR\*       | Busca por ID                   |
| PUT    | `/api/unidades/{id}`               | ADMIN_FRANQUEADORA                        | Atualiza dados da unidade       |
| PATCH  | `/api/unidades/{id}/situacao`      | ADMIN_FRANQUEADORA                        | Ativa/inativa/suspende          |

\* GESTOR_UNIDADE e OPERADOR só conseguem ver a **própria** unidade (a vinculada no seu token) — tentar ver outra retorna 403.

Filtros disponíveis em `GET /api/unidades` (todos opcionais, combináveis):
```
?nome=&cidade=&cnpj=&responsavel=&situacao=ATIVA|INATIVA|SUSPENSA
```

### Testando no PowerShell (sequência completa)

```powershell
# (Re)faça o login se precisar
$resposta = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method Post -ContentType "application/json" -Body '{"email":"admin@franquias.com","senha":"admin123"}'
$token = $resposta.token
$headers = @{ Authorization = "Bearer $token" }

# 1. Cadastrar a franqueadora
$franqueadora = Invoke-RestMethod -Uri "http://localhost:8080/api/franqueadoras" -Method Post -ContentType "application/json" -Headers $headers -Body '{"razaoSocial":"Rede Exemplo Franquias LTDA","cnpj":"12345678000190","dataFundacao":"2010-05-20"}'
$franqueadora

# 2. Cadastrar um franqueado (responsável pela unidade)
$franqueado = Invoke-RestMethod -Uri "http://localhost:8080/api/franqueados" -Method Post -ContentType "application/json" -Headers $headers -Body '{"nome":"João da Silva","cpfCnpj":"12345678901","email":"joao@exemplo.com","telefone":"11999990000"}'
$franqueado

# 3. Cadastrar a unidade (usando os IDs retornados acima)
$body = @{
    franqueadoraId = $franqueadora.id
    franqueadoId   = $franqueado.id
    nomeFantasia   = "Unidade Centro"
    cnpj           = "98765432000110"
    endereco       = "Rua Principal, 100"
    cidade         = "São Paulo"
    estado         = "SP"
    telefone       = "1133334444"
    email          = "centro@exemplo.com"
    dataInicio     = "2024-01-15"
} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades" -Method Post -ContentType "application/json" -Headers $headers -Body $body

# 4. Listar unidades filtrando por cidade
Invoke-RestMethod -Uri "http://localhost:8080/api/unidades?cidade=Paulo" -Headers $headers
```

## Parte 5 — Catálogo de Produtos/Serviços

| Método | Rota                          | Perfil exigido        | Descrição                          |
|--------|--------------------------------|-------------------------|--------------------------------------|
| POST   | `/api/categorias`               | ADMIN_FRANQUEADORA       | Cadastra categoria                   |
| GET    | `/api/categorias`               | qualquer autenticado     | Lista categorias                     |
| GET    | `/api/categorias/{id}`          | qualquer autenticado     | Busca por ID                         |
| POST   | `/api/produtos`                 | ADMIN_FRANQUEADORA       | Cadastra produto/serviço             |
| GET    | `/api/produtos`                 | qualquer autenticado     | Lista/filtra produtos                |
| GET    | `/api/produtos/{id}`            | qualquer autenticado     | Busca por ID                         |
| PUT    | `/api/produtos/{id}`            | ADMIN_FRANQUEADORA       | Atualiza produto                     |
| PATCH  | `/api/produtos/{id}/status`     | ADMIN_FRANQUEADORA       | Ativa/inativa produto                |

Filtros em `GET /api/produtos` (opcionais, combináveis):
```
?nome=&categoriaId=&status=ATIVO|INATIVO
```

### Testando no PowerShell

```powershell
# Reaproveita $headers da sessão de login já feita antes

# 1. Criar categoria
$categoria = Invoke-RestMethod -Uri "http://localhost:8080/api/categorias" -Method Post -ContentType "application/json" -Headers $headers -Body '{"nome":"Bebidas","descricao":"Bebidas em geral"}'
$categoria

# 2. Criar produto
$bodyProduto = @{
    nome        = "Refrigerante Lata 350ml"
    descricao   = "Refrigerante padrão da rede"
    categoriaId = $categoria.id
    precoBase   = 6.50
} | ConvertTo-Json
$bytesProduto = [System.Text.Encoding]::UTF8.GetBytes($bodyProduto)
$produto = Invoke-RestMethod -Uri "http://localhost:8080/api/produtos" -Method Post -ContentType "application/json; charset=utf-8" -Headers $headers -Body $bytesProduto
$produto

# 3. Listar produtos da categoria
Invoke-RestMethod -Uri "http://localhost:8080/api/produtos?categoriaId=$($categoria.id)" -Headers $headers
```

## Parte 6 — Estoque por Unidade

| Método | Rota                                   | Perfil exigido                             | Descrição                             |
|--------|------------------------------------------|-----------------------------------------------|------------------------------------------|
| POST   | `/api/estoques/movimentacoes`             | ADMIN, GESTOR_UNIDADE\*, OPERADOR\*           | Registra ENTRADA ou SAÍDA               |
| GET    | `/api/estoques`                           | qualquer autenticado                          | Lista/filtra estoques                    |
| GET    | `/api/estoques/{id}`                      | ADMIN, GESTOR_UNIDADE\*, OPERADOR\*           | Busca um registro de estoque             |
| GET    | `/api/estoques/{id}/movimentacoes`        | ADMIN, GESTOR_UNIDADE\*, OPERADOR\*           | Histórico de movimentações               |
| PATCH  | `/api/estoques/{id}/minimo`               | ADMIN, GESTOR_UNIDADE\*                       | Define o estoque mínimo                  |

\* Sempre restrito à própria unidade (vinculada no token).

Regras de negócio implementadas:
- Se a unidade nunca teve o produto em estoque, a primeira **ENTRADA** cria o registro automaticamente com saldo inicial 0.
- **SAÍDA** não pode deixar o saldo negativo — retorna `400` com o saldo atual na mensagem.
- Saldo e histórico são gravados na mesma transação (não existe cenário onde o saldo muda sem deixar rastro).

Filtros em `GET /api/estoques` (opcionais, combináveis):
```
?unidadeId=&produtoId=&abaixoMinimo=true
```

### Testando no PowerShell

```powershell
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
# Reaproveita $headers, $categoria.id e $produto.id da Parte 5,
# e o ID da unidade criada na Parte 4 (vamos chamar de $unidadeId)
$unidadeId = 1  # ajuste se o seu ID for diferente

# 1. Entrada de estoque (cria o registro automaticamente)
$bodyEntrada = @{
    unidadeId        = $unidadeId
    produtoServicoId = $produto.id
    tipo             = "ENTRADA"
    quantidade       = 50
    observacao       = "Estoque inicial"
} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/estoques/movimentacoes" -Method Post -ContentType "application/json" -Headers $headers -Body $bodyEntrada

# 2. Consultar o estoque da unidade
Invoke-RestMethod -Uri "http://localhost:8080/api/estoques?unidadeId=$unidadeId" -Headers $headers

# 3. Tentar uma saída maior que o saldo (deve dar erro 400)
$bodySaidaExcessiva = @{
    unidadeId        = $unidadeId
    produtoServicoId = $produto.id
    tipo             = "SAIDA"
    quantidade       = 999
} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/estoques/movimentacoes" -Method Post -ContentType "application/json" -Headers $headers -Body $bodySaidaExcessiva
```

## Parte 7 — Fornecedores

| Método | Rota                                | Perfil exigido        | Descrição                              |
|--------|---------------------------------------|-------------------------|-------------------------------------------|
| POST   | `/api/fornecedores`                    | ADMIN_FRANQUEADORA       | Cadastra fornecedor                       |
| GET    | `/api/fornecedores`                    | qualquer autenticado     | Lista/filtra fornecedores                 |
| GET    | `/api/fornecedores/{id}`               | qualquer autenticado     | Busca por ID                              |
| PUT    | `/api/fornecedores/{id}`               | ADMIN_FRANQUEADORA       | Atualiza dados                            |
| PATCH  | `/api/fornecedores/{id}/status`        | ADMIN_FRANQUEADORA       | Ativa/inativa                             |
| PUT    | `/api/fornecedores/{id}/produtos`      | ADMIN_FRANQUEADORA       | Substitui a lista de produtos associados  |

Filtros em `GET /api/fornecedores` (opcionais):
```
?nome=&cnpj=&status=ATIVO|INATIVO
```

### Testando no PowerShell

```powershell
# Reaproveita $headers e $produto da sessão

# 1. Cadastrar fornecedor
$fornecedor = Invoke-RestMethod -Uri "http://localhost:8080/api/fornecedores" -Method Post -ContentType "application/json" -Headers $headers -Body '{"nome":"Distribuidora ABC","cnpj":"11222333000144","contato":"Carlos","telefone":"1144445555","email":"contato@abc.com"}'
$fornecedor

# 2. Associar produtos a esse fornecedor
$bodyAssociar = @{ produtoIds = @($produto.id) } | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/fornecedores/$($fornecedor.id)/produtos" -Method Put -ContentType "application/json" -Headers $headers -Body $bodyAssociar

# 3. Agora dá pra fazer uma entrada de estoque informando o fornecedor
$bodyEntradaComFornecedor = @{
    unidadeId        = $unidade.id
    produtoServicoId = $produto.id
    tipo             = "ENTRADA"
    quantidade       = 20
    observacao       = "Reposição"
    fornecedorId     = $fornecedor.id
} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/estoques/movimentacoes" -Method Post -ContentType "application/json" -Headers $headers -Body $bodyEntradaComFornecedor
```

## Script de apoio: bootstrap-sessao.ps1

Toda vez que você abre um **novo terminal** do PowerShell, as variáveis (`$token`, `$produto`, `$unidade` etc.) se perdem — é assim que o PowerShell funciona, cada janela tem sua própria memória.

Para não ter que ficar recriando dados de teste toda hora, use o script `bootstrap-sessao.ps1` (na raiz do projeto): ele **busca** os dados que já existem no banco (não cria nada novo) e te devolve todas as variáveis prontas para uso.

```powershell
# Só na primeira vez, se der erro de "execução de scripts desabilitada":
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass

# Rode isso no início de qualquer sessão nova (com o servidor já rodando):
.\bootstrap-sessao.ps1
```

Ao final, ele mostra algo como:
```
Login OK. Usuario: Administrador da Franqueadora (ADMIN_FRANQUEADORA)
Categoria: Bebidas (id 1)
Produto: Refrigerante Lata 350ml (id 33)
Franqueadora: Rede Exemplo Franquias LTDA (id 1)
Franqueado: João da Silva (id 1)
Unidade: Unidade Centro (id 1)
Fornecedor: Distribuidora ABC (id 1)

Pronto! Variaveis disponiveis: $token, $headers, $categoria, $produto, $franqueadora, $franqueadoResp, $unidade, $fornecedor
```

Depois disso, é só usar `$produto.id`, `$unidade.id` etc. normalmente nos comandos de teste — sem precisar recriar nada.
