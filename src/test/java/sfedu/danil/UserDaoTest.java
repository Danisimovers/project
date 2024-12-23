package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.PsqlDBConnection;
import sfedu.danil.dao.CompetitionDao;
import sfedu.danil.dao.UserDao;
import sfedu.danil.models.Competition;
import sfedu.danil.models.Role;
import sfedu.danil.models.User;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTest {

    private static Connection connection;
    private static UserDao userDao;

    @BeforeAll
    public static void setUpBeforeClass() throws SQLException, IOException {
        connection = PsqlDBConnection.getConnection();
        userDao = new UserDao();
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM users");
            statement.executeUpdate("DELETE FROM competitions");
        }
    }

    @Test
    public void testCreateAndReadUserWithCompetitionId() throws SQLException {
        Competition competition = new Competition("Karas Tournament", LocalDateTime.of(2023, 6, 13, 6, 30));
        CompetitionDao competitionDao = new CompetitionDao();
        competitionDao.create(competition);
        String competitionId = competition.getId();

        // Создаем User, используя competitionId из объекта Competition
        User user = new User("Ivan Petrov", "ivan.petrov@example.com", "1234567890", Role.ORGANIZER, "2.2", competitionId);
        userDao.create(user);

        // Чтение пользователя из базы и проверка
        Optional<User> result = userDao.read(user.getId());
        assertTrue(result.isPresent());
        assertEquals(user.getName(), result.get().getName());
        assertEquals(user.getEmail(), result.get().getEmail());
        assertEquals(user.getRole(), result.get().getRole());
        assertEquals(user.getCompetitionId(), result.get().getCompetitionId()); // Проверка правильности competitionId
    }

    @Test
    public void testUpdateUserWithCompetitionId() throws SQLException {

        Competition competition = new Competition("Okun Tournament", LocalDateTime.of(2022, 11, 22, 15, 30));
        CompetitionDao competitionDao = new CompetitionDao();
        competitionDao.create(competition);
        String competitionId = competition.getId();

        User user = new User("Sergey Egorov", "sergey.egorov@example.com", "5555555555", Role.PARTICIPANT, "7.2", competitionId);
        userDao.create(user);

        user.setName("Andrei Karpov");
        user.setEmail("andrei.karpov@example.com");
        userDao.update(user);

        Optional<User> result = userDao.read(user.getId());
        assertTrue(result.isPresent());
        assertEquals("Andrei Karpov", result.get().getName());
        assertEquals("andrei.karpov@example.com", result.get().getEmail());
        assertEquals(competitionId, result.get().getCompetitionId()); // Проверка, что competitionId не изменился
    }

    @Test
    public void testDeleteUserWithCompetitionId() throws SQLException {

        Competition competition = new Competition("Sudak Tournament", LocalDateTime.of(2024, 8, 22, 20, 30));
        CompetitionDao competitionDao = new CompetitionDao();
        competitionDao.create(competition);
        String competitionId = competition.getId();


        User user = new User("Frank Ocean", "frank.ocean@example.com", "6666666666", Role.PARTICIPANT, "5.0", competitionId);
        userDao.create(user);


        userDao.delete(user.getId());

        Optional<User> result = userDao.read(user.getId());
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllUsersWithCompetitionId() throws SQLException {

        Competition competition1 = new Competition("Som Tournament", LocalDateTime.of(2024, 2, 3, 11, 30));
        Competition competition2 = new Competition("Lesh Tournament", LocalDateTime.of(2024, 12, 11, 18, 30));
        CompetitionDao competitionDao = new CompetitionDao();
        competitionDao.create(competition1);
        competitionDao.create(competition2);
        String competitionId1 = competition1.getId();
        String competitionId2 = competition2.getId();


        User user1 = new User("Viktor Volkov", "viktor.volkov@example.com", "7777777777", Role.ORGANIZER, "4.9", competitionId1);
        User user2 = new User("Oleg Plotvich", "oleg.plotvisch@example.com", "8888888888", Role.PARTICIPANT, "9.9", competitionId2);
        userDao.create(user1);
        userDao.create(user2);


        List<User> users = userDao.getAll();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(user1.getId()) && u.getCompetitionId().equals(competitionId1)));
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(user2.getId()) && u.getCompetitionId().equals(competitionId2)));
    }

    @AfterAll
    public static void tearDownAfterClass() throws SQLException {
        connection.close();
    }
}
