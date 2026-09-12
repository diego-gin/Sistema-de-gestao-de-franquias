package com.franquias.api.config;

import io.javalin.Javalin;
import io.javalin.http.ContentType;

/**
 * Ponto de entrada da aplicação.
 * Por enquanto sobe apenas o servidor HTTP com uma rota de health-check,
 * para confirmarmos que o setup do projeto está funcionando.
 * O banco de dados e as demais rotas serão adicionados nas próximas partes.
 */
public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            // Habilita logs de requisição simples no console
            config.bundledPlugins.enableDevLogging();
        });

        // Rota de teste: confirma que a API está no ar
        app.get("/api/health", ctx -> {
            ctx.contentType(ContentType.JSON);
            ctx.result("{\"status\":\"ok\",\"mensagem\":\"Franquias API rodando com sucesso\"}");
        });

        int port = 8080;
        app.start(port);

        System.out.println("=======================================");
        System.out.println(" Franquias API iniciada em http://localhost:" + port);
        System.out.println(" Teste em: http://localhost:" + port + "/api/health");
        System.out.println("=======================================");
    }
}
