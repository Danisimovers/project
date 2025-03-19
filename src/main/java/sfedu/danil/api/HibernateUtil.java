package sfedu.danil.api;


import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import static sfedu.danil.Constants.HBN_FILE_PATH;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    static {
        try {
            String configPath;
            if (isUnitTest()) {
                configPath = HBN_FILE_PATH;
            } else {
                configPath = System.getProperty("hibernate.config", HBN_FILE_PATH);
            }
            Configuration configuration = new Configuration().configure(HBN_FILE_PATH);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка инициализации Hibernate: " + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    private static boolean isUnitTest() {
        try {
            Class.forName("org.junit.jupiter.api.Test");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
