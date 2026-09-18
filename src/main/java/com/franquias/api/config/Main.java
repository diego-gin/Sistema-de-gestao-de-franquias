package com.franquias.api.config;

import com.franquias.api.controllers.AuthController;
import com.franquias.api.controllers.CategoriaController;
import com.franquias.api.controllers.ChamadoController;
import com.franquias.api.controllers.EstoqueController;
import com.franquias.api.controllers.FornecedorController;
import com.franquias.api.controllers.FranqueadoController;
import com.franquias.api.controllers.FranqueadoraController;
import com.franquias.api.controllers.ProdutoController;
import com.franquias.api.controllers.RelatorioController;
import com.franquias.api.controllers.RoyaltyController;
import com.franquias.api.controllers.UnidadeController;
import com.franquias.api.controllers.UsuarioController;
import com.franquias.api.controllers.VendaController;
import com.franquias.api.data.DatabaseConfig;
import com.franquias.api.data.JpaUtil;
import com.franquias.api.exceptions.ApiException;
import com.franquias.api.security.ApiAccessManager;
import com.franquias.api.security.AppRole;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// migrations -> JPA -> servidor HTTP
public class Main {

    public static void main(String[] args) {

        migrarBancoDeDados();
        JpaUtil.getEntityManagerFactory();

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        // substitui o AccessManager removido no Javalin 6
        app.beforeMatched(ApiAccessManager::checarAcesso);

        registrarTratamentoDeErros(app);
        registrarRotas(app);

        Runtime.getRuntime().addShutdownHook(new Thread(JpaUtil::close));

        int port = 8080;
        app.start(port);

        System.out.println("=======================================");
        System.out.println(" Franquias API iniciada em http://localhost:" + port);
        System.out.println(" Login: POST http://localhost:" + port + "/api/auth/login");
        System.out.println("   body: {\"email\":\"admin@franquias.com\",\"senha\":\"admin123\"}");
        System.out.println("=======================================");
    }

    private static void migrarBancoDeDados() {
        Flyway flyway = Flyway.configure()
                .dataSource(DatabaseConfig.JDBC_URL, DatabaseConfig.USER, DatabaseConfig.PASSWORD)
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();
    }

    private static void registrarTratamentoDeErros(Javalin app) {
        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode());
            ctx.json(Map.of("erro", e.getMessage()));
        });

        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500);
            ctx.json(Map.of("erro", "Erro interno no servidor."));
        });
    }

    private static void registrarRotas(Javalin app) {

        app.get("/api/health", ctx -> {
            ctx.contentType(ContentType.JSON);
            ctx.result("{\"status\":\"ok\",\"mensagem\":\"Franquias API rodando com sucesso\"}");
        }, AppRole.ANYONE);

        app.get("/api/health/db", ctx -> {
            try (EntityManager em = JpaUtil.createEntityManager()) {
                Long totalUsuarios = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class)
                        .getSingleResult();
                ctx.json(Map.of("status", "ok", "totalUsuarios", totalUsuarios));
            }
        }, AppRole.ANYONE);

        // Documentação
        app.get("/openapi.yaml", ctx -> {
            ctx.contentType("text/yaml; charset=utf-8");
            ctx.result(lerRecurso("/openapi.yaml"));
        }, AppRole.ANYONE);

        app.get("/swagger", ctx -> {
            ctx.contentType("text/html; charset=utf-8");
            ctx.result(lerRecurso("/swagger.html"));
        }, AppRole.ANYONE);

        // Auth
        AuthController authController = new AuthController();
        app.post("/api/auth/login", authController::login, AppRole.ANYONE);

        // Usuários
        UsuarioController usuarioController = new UsuarioController();

        app.post("/api/usuarios", usuarioController::cadastrar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/usuarios", usuarioController::listar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/usuarios/me", usuarioController::meuPerfil, AppRole.AUTHENTICATED);
        app.get("/api/usuarios/{id}", usuarioController::buscarPorId, AppRole.ADMIN_FRANQUEADORA);
        app.patch("/api/usuarios/{id}/ativar", usuarioController::ativar, AppRole.ADMIN_FRANQUEADORA);
        app.patch("/api/usuarios/{id}/inativar", usuarioController::inativar, AppRole.ADMIN_FRANQUEADORA);

        // Franqueadora
        FranqueadoraController franqueadoraController = new FranqueadoraController();
        app.post("/api/franqueadoras", franqueadoraController::cadastrar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/franqueadoras", franqueadoraController::listar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/franqueadoras/{id}", franqueadoraController::buscarPorId, AppRole.ADMIN_FRANQUEADORA);

        // franqueado
        FranqueadoController franqueadoController = new FranqueadoController();
        app.post("/api/franqueados", franqueadoController::cadastrar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/franqueados", franqueadoController::listar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/franqueados/{id}", franqueadoController::buscarPorId, AppRole.ADMIN_FRANQUEADORA);

        // Unidades
        UnidadeController unidadeController = new UnidadeController();
        app.post("/api/unidades", unidadeController::cadastrar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/unidades", unidadeController::listar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/unidades/{id}", unidadeController::buscarPorId,
                AppRole.ADMIN_FRANQUEADORA, AppRole.GESTOR_UNIDADE, AppRole.OPERADOR);
        app.put("/api/unidades/{id}", unidadeController::atualizar, AppRole.ADMIN_FRANQUEADORA);
        app.patch("/api/unidades/{id}/situacao", unidadeController::alterarSituacao, AppRole.ADMIN_FRANQUEADORA);

        // Categorias
        CategoriaController categoriaController = new CategoriaController();
        app.post("/api/categorias", categoriaController::cadastrar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/categorias", categoriaController::listar, AppRole.AUTHENTICATED);
        app.get("/api/categorias/{id}", categoriaController::buscarPorId, AppRole.AUTHENTICATED);

        // Produtos
        ProdutoController produtoController = new ProdutoController();
        app.post("/api/produtos", produtoController::cadastrar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/produtos", produtoController::listar, AppRole.AUTHENTICATED);
        app.get("/api/produtos/{id}", produtoController::buscarPorId, AppRole.AUTHENTICATED);
        app.put("/api/produtos/{id}", produtoController::atualizar, AppRole.ADMIN_FRANQUEADORA);
        app.patch("/api/produtos/{id}/status", produtoController::alterarStatus, AppRole.ADMIN_FRANQUEADORA);

        // Estoque
        EstoqueController estoqueController = new EstoqueController();
        app.post("/api/estoques/movimentacoes", estoqueController::registrarMovimentacao,
                AppRole.ADMIN_FRANQUEADORA, AppRole.GESTOR_UNIDADE, AppRole.OPERADOR);
        app.get("/api/estoques", estoqueController::listar, AppRole.AUTHENTICATED);
        app.get("/api/estoques/{id}", estoqueController::buscarPorId, AppRole.AUTHENTICATED);
        app.get("/api/estoques/{id}/movimentacoes", estoqueController::listarMovimentacoes, AppRole.AUTHENTICATED);
        app.patch("/api/estoques/{id}/minimo", estoqueController::atualizarMinimo,
                AppRole.ADMIN_FRANQUEADORA, AppRole.GESTOR_UNIDADE);

        // Fornecedores
        FornecedorController fornecedorController = new FornecedorController();
        app.post("/api/fornecedores", fornecedorController::cadastrar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/fornecedores", fornecedorController::listar, AppRole.AUTHENTICATED);
        app.get("/api/fornecedores/{id}", fornecedorController::buscarPorId, AppRole.AUTHENTICATED);
        app.put("/api/fornecedores/{id}", fornecedorController::atualizar, AppRole.ADMIN_FRANQUEADORA);
        app.patch("/api/fornecedores/{id}/status", fornecedorController::alterarStatus, AppRole.ADMIN_FRANQUEADORA);
        app.put("/api/fornecedores/{id}/produtos", fornecedorController::associarProdutos, AppRole.ADMIN_FRANQUEADORA);

        // Vendas
        VendaController vendaController = new VendaController();
        app.post("/api/vendas", vendaController::registrar,
                AppRole.ADMIN_FRANQUEADORA, AppRole.GESTOR_UNIDADE, AppRole.OPERADOR);
        app.get("/api/vendas", vendaController::listar, AppRole.AUTHENTICATED);
        app.get("/api/vendas/{id}", vendaController::buscarPorId, AppRole.AUTHENTICATED);

        // Royalties
        RoyaltyController royaltyController = new RoyaltyController();
        app.post("/api/royalties/calcular", royaltyController::calcular, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/royalties", royaltyController::listar, AppRole.AUTHENTICATED);
        app.get("/api/royalties/{id}", royaltyController::buscarPorId, AppRole.AUTHENTICATED);
        app.patch("/api/royalties/{id}/pagamento", royaltyController::registrarPagamento, AppRole.ADMIN_FRANQUEADORA);

        // Chamados
        ChamadoController chamadoController = new ChamadoController();
        app.post("/api/chamados", chamadoController::abrir,
                AppRole.ADMIN_FRANQUEADORA, AppRole.GESTOR_UNIDADE, AppRole.OPERADOR);
        app.get("/api/chamados", chamadoController::listar, AppRole.AUTHENTICATED);
        app.get("/api/chamados/{id}", chamadoController::buscarPorId, AppRole.AUTHENTICATED);
        app.patch("/api/chamados/{id}/status", chamadoController::atualizarStatus, AppRole.ADMIN_FRANQUEADORA);

        // Relatórios
        RelatorioController relatorioController = new RelatorioController();
        app.get("/api/relatorios/faturamento", relatorioController::faturamentoPorUnidade, AppRole.AUTHENTICATED);
        app.get("/api/relatorios/ranking-unidades", relatorioController::rankingUnidades, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/relatorios/royalties-totais", relatorioController::royaltiesTotais, AppRole.AUTHENTICATED);
        app.get("/api/relatorios/produtos-mais-vendidos", relatorioController::produtosMaisVendidos, AppRole.AUTHENTICATED);
        app.get("/api/relatorios/estoque-critico", relatorioController::estoqueCritico, AppRole.AUTHENTICATED);
        app.get("/api/relatorios/chamados-por-status", relatorioController::chamadosPorStatus, AppRole.AUTHENTICATED);
    }

    private static String lerRecurso(String caminho) {
        try (InputStream is = Main.class.getResourceAsStream(caminho)) {
            if (is == null) {
                throw new IllegalStateException("Recurso não encontrado: " + caminho);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler recurso: " + caminho, e);
        }
    }
}
