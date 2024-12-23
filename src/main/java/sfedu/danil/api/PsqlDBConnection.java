package sfedu.danil.api;

import java.sql.*;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfedu.danil.Constants;
import sfedu.danil.utils.ConfigurationUtil;

public class PsqlDBConnection {

    private static final Logger logger = LogManager.getLogger(PsqlDBConnection.class);


    public static Connection getConnection() throws SQLException, IOException {
        String filePath = Constants.DB_PROPERTIES_PATH;

        String url = ConfigurationUtil.getConfigurationEntry("db.url", filePath);
        String user = ConfigurationUtil.getConfigurationEntry("db.user", filePath);
        String password = ConfigurationUtil.getConfigurationEntry("db.password", filePath);

        try {
            Class.forName("org.postgresql.Driver");
            logger.info("Драйвер PostgreSQL успешно загружен");
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка: Драйвер PostgreSQL не найден", e);
            throw new SQLException("Драйвер PostgreSQL не найден", e);
        }

        Connection connection = DriverManager.getConnection(url, user, password);
        logger.info("Успешное подключение к базе данных: {}", url);
        return connection;
    }
}
