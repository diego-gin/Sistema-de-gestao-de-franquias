# Franquias API

API REST em Java para gestão de uma rede de franquias: unidades, produtos, estoque,
vendas, royalties, fornecedores e chamados de suporte.

## Tecnologias utilizadas

- **Java 17**
- **Javalin** — servidor HTTP e roteamento
- **Hibernate/JPA** — persistência (ORM), sem Spring Data
- **H2 Database** — banco relacional em arquivo local (sem instalação)
- **Flyway** — migrations do banco de dados
- **Jackson** — serialização JSON
- **Hibernate Validator (Bean Validation)** — validação de dados de entrada
- **JJWT** — autenticação via JSON Web Token
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

Teste rápido:
```bash
curl http://localhost:8080/api/health
```

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
├── src/main/resources/  # Configurações, migrations Flyway
└── pom.xml
```

## Status do desenvolvimento

- [x] Setup inicial do projeto
- [ ] Modelagem de dados e banco
- [ ] Autenticação e perfis de usuário
- [ ] Franqueadora, unidades e franqueados
- [ ] Catálogo de produtos/serviços
- [ ] Fornecedores
- [ ] Estoque
- [ ] Vendas
- [ ] Royalties/financeiro
- [ ] Chamados de suporte
- [ ] Relatórios e indicadores
