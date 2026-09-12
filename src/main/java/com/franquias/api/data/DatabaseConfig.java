package com.franquias.api.data;

/**
 * Constantes de conexão com o banco de dados.
 * Usadas tanto pelo Flyway (migrations) quanto, indiretamente, pelo
 * persistence.xml (o valor da URL deve ser mantido idêntico nos dois lugares).
 */
public final class DatabaseConfig {

    public static final String JDBC_URL = "jdbc:h2:file:./data/franquias;AUTO_SERVER=TRUE";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    private DatabaseConfig() {
    }
}
