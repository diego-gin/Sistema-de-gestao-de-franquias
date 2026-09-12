package com.franquias.api.config;

import com.franquias.api.controllers.AuthController;
import com.franquias.api.controllers.UsuarioController;
import com.franquias.api.data.DatabaseConfig;
import com.franquias.api.data.JpaUtil;
import com.franquias.api.exceptions.ApiException;
import com.franquias.api.security.ApiAccessManager;
import com.franquias.api.security.AppRole;
import io.javalin.Javalin;
import io.javalin.http.ContentType;
import jakarta.persistence.EntityManager;
import org.flywaydb.core.Flyway;

import java.util.Map;

/**
 * Ponto de entrada da aplicação.
 * Ordem de inicialização:
 *   1. Roda as migrations do Flyway (cria/atualiza as tabelas no H2).
 *   2. Inicializa o EntityManagerFactory (Hibernate/JPA).
 *   3. Sobe o servidor HTTP (Javalin), com controle de acesso por perfil
 *      e tratamento centralizado de exceções.
 */
public class Main {

    public static void main(String[] args) {

        migrarBancoDeDados();
        JpaUtil.getEntityManagerFactory();

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        // Verifica token/perfil em toda rota já identificada pelo roteador
        // (equivalente ao antigo AccessManager, removido no Javalin 6).
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
        // Exceções de negócio conhecidas (400, 401, 403, 404, 409...)
        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getStatusCode());
            ctx.json(Map.of("erro", e.getMessage()));
        });

        // Qualquer outra exceção não prevista vira 500, sem vazar detalhes internos.
        app.exception(Exception.class, (e, ctx) -> {
            e.printStackTrace();
            ctx.status(500);
            ctx.json(Map.of("erro", "Erro interno no servidor."));
        });
    }

    private static void registrarRotas(Javalin app) {

        // ---------- Health check ----------
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

        // ---------- Autenticação ----------
        AuthController authController = new AuthController();
        app.post("/api/auth/login", authController::login, AppRole.ANYONE);

        // ---------- Usuários ----------
        UsuarioController usuarioController = new UsuarioController();

        app.post("/api/usuarios", usuarioController::cadastrar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/usuarios", usuarioController::listar, AppRole.ADMIN_FRANQUEADORA);
        app.get("/api/usuarios/me", usuarioController::meuPerfil, AppRole.AUTHENTICATED);
        app.get("/api/usuarios/{id}", usuarioController::buscarPorId, AppRole.ADMIN_FRANQUEADORA);
        app.patch("/api/usuarios/{id}/ativar", usuarioController::ativar, AppRole.ADMIN_FRANQUEADORA);
        app.patch("/api/usuarios/{id}/inativar", usuarioController::inativar, AppRole.ADMIN_FRANQUEADORA);
    }
}
