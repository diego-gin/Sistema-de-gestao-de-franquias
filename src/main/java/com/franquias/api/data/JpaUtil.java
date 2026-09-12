package com.franquias.api.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Ponto único de acesso ao EntityManagerFactory (Hibernate/JPA).
 * Como não usamos um framework de DI, esta classe funciona como o
 * "container" manual que fornece EntityManagers para os Repositories.
 */
public final class JpaUtil {

    private static final String PERSISTENCE_UNIT_NAME = "franquiasPU";
    private static volatile EntityManagerFactory entityManagerFactory;

    private JpaUtil() {
        // classe utilitária, não deve ser instanciada
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            synchronized (JpaUtil.class) {
                if (entityManagerFactory == null) {
                    entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                }
            }
        }
        return entityManagerFactory;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
