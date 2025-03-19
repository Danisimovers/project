package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.PsqlDBConnection;
import sfedu.danil.dao.CatchDao;
import sfedu.danil.models.Catch;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CatchDaoNegativeTest {

    private static Connection connection;
    private static CatchDao catchDao;

    @BeforeAll
    public static void setUpBeforeClass() throws SQLException, IOException {
        connection = PsqlDBConnection.getConnection();
        catchDao = new CatchDao();
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM catches");
            statement.executeUpdate("DELETE FROM competitions");
            statement.executeUpdate("DELETE FROM users");
        }
    }

    @Test
    public void testReadNonExistentCatch() throws SQLException {
        Optional<Catch> result = catchDao.read("non-existent-id");
        assertFalse(result.isPresent(), "Не должно быть записи с несуществующим ID");
    }

    @Test
    public void testUpdateCatch_Negative() throws SQLException {
        Catch invalidCatch = new Catch("InvalidFish", 5.0, 10.0, "invalid-user-id", "invalid-competition-id");
        invalidCatch.setId("non-existent-id");
        catchDao.update(invalidCatch);
        Optional<Catch> result = catchDao.read(invalidCatch.getId());
        assertFalse(result.isPresent(), "Обновление несуществующей записи не должно быть успешным");
    }

    @Test
    public void testDeleteCatch_Negative() throws SQLException {
        String invalidId = "non-existent-id";
        catchDao.delete(invalidId);

        Optional<Catch> result = catchDao.read(invalidId);
        assertFalse(result.isPresent(), "Удаление несуществующей записи не должно быть успешным");
    }

    @Test
    public void testGetAllCatches_Negative() throws SQLException {
        List<Catch> catches = catchDao.getAll();
        assertTrue(catches.isEmpty(), "Список должен быть пустым, так как база данных пуста");
    }

    @AfterAll
    public static void tearDownAfterClass() throws SQLException {
        connection.close();
    }
}