package com.franquias.api.data;

public final class DatabaseConfig {

    public static final String JDBC_URL = "jdbc:h2:file:./data/franquias;AUTO_SERVER=TRUE";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    private DatabaseConfig() {
    }
}
