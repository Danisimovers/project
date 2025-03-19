package sfedu.danil.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

import static sfedu.danil.Constants.*;

public class DataProviderHibernate {

    private static final Logger logger = LogManager.getLogger(DataProviderHibernate.class);

    public static long getDatabaseSize() {


        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            long size = ((Number) session.createNativeQuery(SQL_DATABASE_SIZE).getSingleResult()).longValue();
            logger.info("Размер базы данных: {}", size);
            return size;
        } catch (Exception e) {
            logger.error("Ошибка при получении размера базы данных", e);
            throw e;
        }
    }

    public static List<String> getTableNames() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<String> tableNames = session.createNativeQuery(SQL_TABLE_NAMES).list();
            logger.info("Получены таблицы: {}", tableNames);
            return tableNames;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка таблиц", e);
            throw e;
        }
    }

    public static List<String> getUserList() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<String> userList = session.createNativeQuery(SQL_USER_LIST).list();
            logger.info("Получены пользователи: {}", userList);
            return userList;
        } catch (Exception e) {
            logger.error("Ошибка при получении списка пользователей", e);
            throw e;
        }
    }

    public static List<Object[]> getColumnTypes() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> columnTypes = session.createNativeQuery(SQL_COLUMN_TYPES).list();
            logger.info("Получены типы столбцов: {}", columnTypes.size());
            return columnTypes;
        } catch (Exception e) {
            logger.error("Ошибка при получении типов столбцов", e);
            throw e;
        }
    }
}
