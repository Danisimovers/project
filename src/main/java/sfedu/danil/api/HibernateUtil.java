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

            configuration.addAnnotatedClass(sfedu.danil.models.TestEntity.class);
            configuration.addAnnotatedClass(sfedu.danil.models.mappedSuperclass.Organizer.class);
            configuration.addAnnotatedClass(sfedu.danil.models.mappedSuperclass.Participant.class);
            configuration.addAnnotatedClass(sfedu.danil.models.mappedSingletable.DayCompetition.class);
            configuration.addAnnotatedClass(sfedu.danil.models.mappedSingletable.NightCompetition.class);
            configuration.addAnnotatedClass(sfedu.danil.models.mappedJoined.FeederCatch.class);
            configuration.addAnnotatedClass(sfedu.danil.models.mappedJoined.SpinningCatch.class);
            configuration.addAnnotatedClass(sfedu.danil.models.mappedTableperclass.DayCatch.class);
            configuration.addAnnotatedClass(sfedu.danil.models.mappedTableperclass.NightCatch.class);


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