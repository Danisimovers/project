package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.dao.CompetitionSingleTableDao;
import sfedu.danil.models.mappedSingletable.Competition;
import sfedu.danil.models.mappedSingletable.DayCompetition;
import sfedu.danil.models.mappedSingletable.NightCompetition;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CompetitionSingleTableDaoTest {

    private static CompetitionSingleTableDao competitionDao;

    @BeforeAll
    public static void setUp() {
        competitionDao = new CompetitionSingleTableDao();
    }

    @AfterAll
    public static void tearDown() {
        HibernateUtil.getSessionFactory().close();
    }

    @Test
    public void testCreateCompetition() {
        Competition competition = new DayCompetition("Соревнование по рыбалке", LocalDateTime.now());
        competitionDao.create(competition);

        Competition retrievedCompetition = competitionDao.read(competition.getId());
        assertNotNull(retrievedCompetition, "Соревнование должно быть сохранено и доступно для получения.");
        assertEquals(competition.getName(), retrievedCompetition.getName(), "Названия соревнований должны совпадать.");
    }

    @Test
    public void testReadCompetition() {
        Competition competition = new NightCompetition("Ночное соревнование по рыбалке", LocalDateTime.now());
        competitionDao.create(competition);

        Competition retrievedCompetition = competitionDao.read(competition.getId());
        assertNotNull(retrievedCompetition, "Соревнование должно быть доступно для получения.");
        assertEquals(competition.getName(), retrievedCompetition.getName(), "Названия соревнований должны совпадать.");
    }

    @Test
    public void testUpdateCompetition() {
        Competition competition = new DayCompetition("Соревнование по рыбалке", LocalDateTime.now());
        competitionDao.create(competition);

        competition.setName("Обновленное соревнование по рыбалке");
        competitionDao.update(competition);

        Competition updatedCompetition = competitionDao.read(competition.getId());
        assertNotNull(updatedCompetition, "Соревнование должно быть доступно для получения после обновления.");
        assertEquals("Обновленное соревнование по рыбалке", updatedCompetition.getName(), "Название соревнования должно быть обновлено.");
    }

    @Test
    public void testDeleteCompetition() {
        Competition competition = new NightCompetition("Ночное соревнование по рыбалке", LocalDateTime.now());
        competitionDao.create(competition);

        competitionDao.delete(competition.getId());

        Competition deletedCompetition = competitionDao.read(competition.getId());
        assertNull(deletedCompetition, "Соревнование должно быть удалено и недоступно для получения.");
    }

    @Test
    public void testGetAllCompetitions() throws SQLException {
        competitionDao.create(new DayCompetition("Соревнование по рыбалке 1", LocalDateTime.now()));
        competitionDao.create(new NightCompetition("Соревнование по рыбалке 2", LocalDateTime.now()));

        List<Competition> competitions = competitionDao.getAll();
        assertTrue(competitions.size() > 0, "Должны быть получены соревнования из базы данных.");
    }
}
