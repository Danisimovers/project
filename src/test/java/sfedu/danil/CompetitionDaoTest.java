package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.PsqlDBConnection;
import sfedu.danil.dao.CompetitionDao;
import sfedu.danil.models.Competition;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CompetitionDaoTest {

    private static Connection connection;
    private static CompetitionDao competitionDao;

    @BeforeAll
    public static void setUpBeforeClass() throws SQLException, IOException {
        connection = PsqlDBConnection.getConnection();
        competitionDao = new CompetitionDao();
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM competitions");
        }
    }

    @Test
    public void testCreateAndReadCompetition() throws SQLException {
        Competition competition = new Competition("Fishing Contest", LocalDateTime.of(2024, 12, 25, 10, 0));
        competitionDao.create(competition);
        Optional<Competition> result = competitionDao.read(competition.getId());
        assertTrue(result.isPresent());
        assertEquals(competition.getName(), result.get().getName());
        assertEquals(competition.getDate(), result.get().getDate());
    }

    @Test
    public void testUpdateCompetition() throws SQLException {
        Competition competition = new Competition("Fishing Contest", LocalDateTime.of(2021, 12, 25, 10, 0));
        competitionDao.create(competition);
        competition.setName("Updated Fishing Contest");
        competition.setDate(LocalDateTime.of(2024, 12, 26, 10, 0));
        competitionDao.update(competition);
        Optional<Competition> result = competitionDao.read(competition.getId());
        assertTrue(result.isPresent());
        assertEquals("Updated Fishing Contest", result.get().getName());
        assertEquals(LocalDateTime.of(2024, 12, 26, 10, 0), result.get().getDate());
    }

    @Test
    public void testDeleteCompetition() throws SQLException {
        Competition competition = new Competition("Fishing Contest", LocalDateTime.of(2023, 12, 25, 10, 0));
        competitionDao.create(competition);
        competitionDao.delete(competition.getId());
        Optional<Competition> result = competitionDao.read(competition.getId());
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllCompetitions() throws SQLException {
        Competition competition1 = new Competition("Fishing Contest 1", LocalDateTime.of(2022, 12, 25, 10, 0));
        Competition competition2 = new Competition("Fishing Contest 2", LocalDateTime.of(2024, 12, 26, 10, 0));
        competitionDao.create(competition1);
        competitionDao.create(competition2);

        List<Competition> competitions = competitionDao.getAll();
        assertEquals(2, competitions.size());
        assertTrue(competitions.stream().anyMatch(c -> c.getId().equals(competition1.getId())));
        assertTrue(competitions.stream().anyMatch(c -> c.getId().equals(competition2.getId())));
    }

    @AfterAll
    public static void tearDownAfterClass() throws SQLException {
        connection.close();
    }
}
