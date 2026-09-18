# Franquias API

API REST para gerenciamento de redes de franquias. O projeto adota uma abordagem minimalista sem o ecossistema Spring Boot, realizando a injeção de dependências, o roteamento HTTP e a ligação entre camadas de forma manual. A persistência é feita com Hibernate/JPA sobre banco H2 local, com versionamento de banco automatizado via Flyway e autenticação segura via JWT.

## Stack

- Java 17
- Javalin (servidor HTTP e roteamento)
- Hibernate/JPA (persistência), sem Spring Data
- H2 (arquivo local, `./data/franquias.mv.db`)
- Flyway (migrations em `src/main/resources/db/migration`)
- Hibernate Validator (Bean Validation)
- jBCrypt (hash de senha) + JJWT (tokens JWT)
- Maven

## Requisitos

- JDK 17+
- Maven 3.9+

## Estrutura

```
src/main/java/com/franquias/api/
├── controllers/    rotas HTTP
├── models/         entidades JPA
├── dtos/           entrada/saída da API
├── services/       regras de negócio
├── repositories/   acesso a dados
├── security/       JWT, hash de senha, controle de acesso por perfil
├── exceptions/      exceções de negócio -> códigos HTTP
├── validation/      validação de DTOs
└── config/         Main.java
```

## Instalação e Execução

Clone o repositório e acesse a pasta do projeto:
```bash
git clone <url-do-seu-repositorio>
cd franquias-api
```

Para compilar e rodar diretamente via Maven:
```bash
mvn compile exec:java
```

Ou para gerar o pacote e executar o JAR:
```bash
mvn clean package
java -jar target/franquias-api.jar
```

A API estará disponível em: `http://localhost:8080`. Para Checar a saúde da aplicação:

```bash
curl http://localhost:8080/api/health
```

## Autenticação e perfis

A segurança utiliza tokens JWT no header `Authorization: Bearer <token>`.
* **Perfis:** `ADMIN_FRANQUEADORA`, `GESTOR_UNIDADE`, `OPERADOR`.
* **Isolamento:** Usuários de unidade acessam apenas dados da própria unidade (validado via `AutorizacaoUnidadeUtil`).

## Credenciais para testes (seed)

A migration `V3__seed_dados_exemplo.sql` popula o banco automaticamente com dados fictícios para testes rápidos.

* **Admin inicial:** `admin@franquias.com` / `admin123`

* **Usuários de teste:** `fernanda.souza@saborexpress.com` (GESTOR) / `lucas.almeida@saborexpress.com` (OPERADOR)

* **Senha padrão do seed:** `123456`

## Modelo de Dados

**Entidades:** `Usuario`, `Franqueadora`, `Franqueado`, `UnidadeFranqueada`, `Categoria`, `ProdutoServico`, `Fornecedor`, `Estoque`, `MovimentacaoEstoque`, `Venda`, `ItemVenda`, `Royalty` e `ChamadoSuporte`.

**Decisões de arquitetura:**

* Fornecedores e o catálogo de produtos são globais para toda a rede.

* O schema do banco é controlado exclusivamente pelo Flyway (Hibernate configurado em modo `validate`).

## Endpoints principais

* Auth: `POST /api/auth/login`

* Usuários: `POST, GET /api/usuarios` | `GET /api/usuarios/me` | `PATCH /api/usuarios/{id}/ativar`

* Unidades: `POST, GET /api/unidades` | `GET, PUT /api/unidades/{id}`

* Catálogo: `POST, GET /api/categorias` | `POST, GET /api/produtos`

* Estoque: `POST /api/estoques/movimentacoes` | `GET /api/estoques`

* Fornecedores: `POST, GET /api/fornecedores` | `PUT /api/fornecedores/{id}/produtos`

* Vendas: `POST, GET /api/vendas`

* Royalties: `POST /api/royalties/calcular` | `GET /api/royalties`

* Chamados: `POST, GET /api/chamados` | `PATCH /api/chamados/{id}/status`

* Relatórios: `GET /api/relatorios/faturamento` | `ranking-unidades` | `estoque-critico`

## Documentação (Swagger)

Com o servidor rodando, acesse a documentação interativa em `http://localhost:8080/swagger` ou visualize o arquivo bruto OpenAPI 3.0 em `http://localhost:8080/openapi.yaml`.

## Licença

Projeto desenvolvido exclusivamente para fins acadêmicos.