package sfedu.danil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.dao.CompetitionImageSingleTableDao;
import sfedu.danil.dao.CompetitionSingleTableDao;
import sfedu.danil.models.mappedSingletable.Competition;
import sfedu.danil.models.mappedSingletable.DayCompetition;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class CompetitionImageSingleTableDaoTest {

    private static CompetitionSingleTableDao competitionDao;
    private static CompetitionImageSingleTableDao imageDao;
    private static String testCompetitionId;

    @BeforeAll
    public static void setUp() {
        competitionDao = new CompetitionSingleTableDao();
        Competition competition = new DayCompetition(
                "Рыболовный турнир",
                LocalDateTime.now()
        );
        competitionDao.create(competition);
        testCompetitionId = competition.getId();
    }

    @BeforeEach
    public void init() {
        imageDao = new CompetitionImageSingleTableDao(testCompetitionId);
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        List<String> images = imageDao.read(testCompetitionId).orElse(List.of());
        for (String filename : images) {
            imageDao.delete(filename);
        }
    }

    @AfterAll
    public static void tearDown() {
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var tx = session.beginTransaction();
            Competition competition = session.get(Competition.class, testCompetitionId);
            if (competition != null) {
                session.remove(competition);
            }
            tx.commit();
        }
        HibernateUtil.getSessionFactory().close();
    }

    @Test
    public void testCreateImages() throws SQLException {
        List<String> filenames = List.of("photo1.jpg", "photo2.jpg");
        imageDao.create(filenames);

        List<String> images = imageDao.read(testCompetitionId).orElse(List.of());
        assertEquals(2, images.size(), "Должно быть 2 изображения");
        assertTrue(images.containsAll(filenames), "Коллекция должна содержать все добавленные файлы");
    }

    @Test
    public void testReadImages() throws SQLException {
        List<String> filenames = List.of("banner.jpg");
        imageDao.create(filenames);

        List<String> retrieved = imageDao.read(testCompetitionId).orElse(List.of());
        assertEquals(filenames, retrieved, "Полученные изображения должны совпадать с добавленными");
    }

    @Test
    public void testUpdateImages() throws SQLException {
        imageDao.create(List.of("old1.jpg", "old2.jpg"));

        List<String> newFilenames = List.of("new1.jpg", "new2.jpg");
        imageDao.update(newFilenames);

        List<String> updated = imageDao.read(testCompetitionId).orElse(List.of());
        assertEquals(newFilenames, updated, "Коллекция должна быть полностью обновлена");
    }

    @Test
    public void testDeleteImage() throws SQLException {
        String filenameToDelete = "delete_me.jpg";
        imageDao.create(List.of(filenameToDelete, "keep.jpg"));

        imageDao.delete(filenameToDelete);

        List<String> remaining = imageDao.read(testCompetitionId).orElse(List.of());
        assertFalse(remaining.contains(filenameToDelete), "Изображение должно быть удалено");
        assertEquals(1, remaining.size(), "Должно остаться 1 изображение");
    }


    @Test
    public void testGetAllImages() throws SQLException {
        List<String> filenames = List.of("img1.jpg", "img2.jpg");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Competition competition = session.get(Competition.class, testCompetitionId);
            competition.getImageFilenames().addAll(filenames);
            session.merge(competition);
            tx.commit();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<List<String>> allImages = imageDao.getAll();
            assertEquals(1, allImages.size(), "Должен вернуться список с одной коллекцией");

            Competition competition = session.get(Competition.class, testCompetitionId);
            List<String> loadedImages = competition.getImageFilenames();
            assertEquals(filenames, loadedImages, "Коллекция изображений должна совпадать");
        }
    }
}