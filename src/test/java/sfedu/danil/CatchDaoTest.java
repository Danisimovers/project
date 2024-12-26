package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.PsqlDBConnection;
import sfedu.danil.dao.CatchDao;
import sfedu.danil.dao.CompetitionDao;
import sfedu.danil.dao.UserDao;
import sfedu.danil.models.Catch;
import sfedu.danil.models.Competition;
import sfedu.danil.models.Role;
import sfedu.danil.models.User;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CatchDaoTest {

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
    public void testCreateAndReadCatch() throws SQLException {
        Competition competition = new Competition("Karas Tournament", LocalDateTime.of(2023, 6, 13, 6, 30));
        CompetitionDao competitionDao = new CompetitionDao();
        competitionDao.create(competition);
        String competitionId = competition.getId();

        User user = new User("Ivan Petrov", "ivan.petrov@example.com", "1234567890", Role.ORGANIZER, "2.2", competitionId);
        UserDao userDao = new UserDao();
        userDao.create(user);
        String userId = user.getId();


        Catch catchEntry = new Catch("Sudak", 5.0, 10.0, userId, competitionId);
        catchDao.create(catchEntry);

        Optional<Catch> result = catchDao.read(catchEntry.getId());
        assertTrue(result.isPresent());
        assertEquals(catchEntry.getFishType(), result.get().getFishType());
        assertEquals(catchEntry.getWeight(), result.get().getWeight());
        assertEquals(catchEntry.getPoints(), result.get().getPoints());
        assertEquals(catchEntry.getUserId(), result.get().getUserId());
        assertEquals(catchEntry.getCompetitionId(), result.get().getCompetitionId());
    }

    @Test
    public void testUpdateCatch() throws SQLException {

        Competition competition = new Competition("Lesh Tournament", LocalDateTime.of(2024, 12, 11, 18, 30));
        CompetitionDao competitionDao = new CompetitionDao();
        competitionDao.create(competition);
        String competitionId = competition.getId();

        User user = new User("Sergey Egorov", "sergey.egorov@example.com", "5555555555", Role.PARTICIPANT, "7.2", competitionId);
        UserDao userDao = new UserDao();
        userDao.create(user);
        String userId = user.getId();


        Catch catchEntry = new Catch("Trofeyny Lesch", 4.5, 8.0, userId, competitionId);
        catchDao.create(catchEntry);
        catchEntry.setWeight(6.0);
        catchEntry.setPoints(12.0);
        catchDao.update(catchEntry);
        Optional<Catch> result = catchDao.read(catchEntry.getId());
        assertTrue(result.isPresent());
        assertEquals(6.0, result.get().getWeight());
        assertEquals(12.0, result.get().getPoints());
    }

    @Test
    public void testDeleteCatch() throws SQLException {

        Competition competition = new Competition("Sudak Tournament", LocalDateTime.of(2024, 8, 22, 20, 30));
        CompetitionDao competitionDao = new CompetitionDao();
        competitionDao.create(competition);
        String competitionId = competition.getId();

        User user = new User("Oleg Plotvich", "oleg.plotvisch@example.com", "8888888888", Role.PARTICIPANT, "9.9", competitionId);
        UserDao userDao = new UserDao();
        userDao.create(user);
        String userId = user.getId();

        Catch catchEntry = new Catch("Okun", 3.5, 7.0, userId, competitionId);
        catchDao.create(catchEntry);
        String catchId = catchEntry.getId();
        catchDao.delete(catchId);
        Optional<Catch> result = catchDao.read(catchId);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllCatches() throws SQLException {

        Competition competition = new Competition("Som Tournament", LocalDateTime.of(2024, 2, 3, 11, 30));
        CompetitionDao competitionDao = new CompetitionDao();
        competitionDao.create(competition);
        String competitionId = competition.getId();

        User user = new User("Viktor Volkov", "viktor.volkov@example.com", "7777777777", Role.ORGANIZER, "4.9", competitionId);        UserDao userDao = new UserDao();
        userDao.create(user);
        String userId = user.getId();


        Catch catch1 = new Catch("Karp", 2.0, 5.0, userId, competitionId);
        Catch catch2 = new Catch("Shchuka", 3.5, 6.0, userId, competitionId);
        catchDao.create(catch1);
        catchDao.create(catch2);
        List<Catch> catches = catchDao.getAll();
        assertEquals(2, catches.size());
        assertTrue(catches.stream().anyMatch(c -> c.getId().equals(catch1.getId())));
        assertTrue(catches.stream().anyMatch(c -> c.getId().equals(catch2.getId())));
    }

    @AfterAll
    public static void tearDownAfterClass() throws SQLException {
        connection.close();
    }
}
