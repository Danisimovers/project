package sfedu.danil;

import org.junit.jupiter.api.Test;
import sfedu.danil.api.PsqlDBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PsqlDBConnectionTest {

    @Test
    void testGetConnection() {
        try {
            Connection connection = PsqlDBConnection.getConnection();
            assertNotNull(connection, "Соединение не должно быть null");
            connection.close();
        } catch (SQLException | IOException e) {
            fail("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }
}
