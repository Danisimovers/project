package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.dao.CatchImageJoinedDao;
import sfedu.danil.dao.CatchJoinedDao;
import sfedu.danil.models.mappedJoined.Catch;
import sfedu.danil.models.mappedJoined.FeederCatch;
import java.sql.SQLException;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class CatchImageJoinedDaoTest {

    private static CatchJoinedDao catchDao;
    private static CatchImageJoinedDao imageDao;
    private static String testCatchId;

    @BeforeAll
    public static void setUp() {
        catchDao = new CatchJoinedDao();
        FeederCatch catchEntity = new FeederCatch("Карп", 5.5, 8.0,
                "user1", "comp1", "Черви", "Фидер");
        catchDao.create(catchEntity);
        testCatchId = catchEntity.getId();
    }

    @BeforeEach
    public void init() {
        imageDao = new CatchImageJoinedDao(testCatchId);
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        // Очищаем все изображения после каждого теста
        Set<String> images = imageDao.read(testCatchId).orElse(Set.of());
        if (!images.isEmpty()) {
            for (String filename : images) {
                imageDao.delete(filename);
            }
        }
    }

    @AfterAll
    public static void tearDown() {
        // Удаляем тестовый улов
        try (var session = HibernateUtil.getSessionFactory().openSession()) {
            var tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, testCatchId);
            if (catchEntity != null) {
                session.remove(catchEntity);
            }
            tx.commit();
        }
        HibernateUtil.getSessionFactory().close();
    }

    @Test
    public void testCreateImages() throws SQLException {
        Set<String> filenames = Set.of("fish1.jpg", "fish2.jpg");
        imageDao.create(filenames);

        Set<String> images = imageDao.read(testCatchId).orElse(Set.of());
        assertEquals(2, images.size(), "Должно быть 2 изображения");
        assertTrue(images.containsAll(filenames), "Коллекция должна содержать все добавленные файлы");
    }

    @Test
    public void testReadImages() throws SQLException {
        Set<String> filenames = Set.of("fish3.jpg");
        imageDao.create(filenames);

        Set<String> retrieved = imageDao.read(testCatchId).orElse(Set.of());
        assertEquals(filenames, retrieved, "Полученные изображения должны совпадать с добавленными");
    }

    @Test
    public void testUpdateImages() throws SQLException {
        imageDao.create(Set.of("old1.jpg", "old2.jpg"));

        Set<String> newFilenames = Set.of("new1.jpg", "new2.jpg");
        imageDao.update(newFilenames);

        Set<String> updated = imageDao.read(testCatchId).orElse(Set.of());
        assertEquals(newFilenames, updated, "Коллекция должна быть полностью обновлена");
    }

    @Test
    public void testDeleteImage() throws SQLException {
        String filenameToDelete = "todelete.jpg";
        imageDao.create(Set.of(filenameToDelete, "keep.jpg"));

        imageDao.delete(filenameToDelete);

        Set<String> remaining = imageDao.read(testCatchId).orElse(Set.of());
        assertFalse(remaining.contains(filenameToDelete), "Изображение должно быть удалено");
        assertEquals(1, remaining.size(), "Должно остаться 1 изображение");
    }

    @Test
    public void testDeleteNonExistentImage() throws SQLException {
        imageDao.create(Set.of("image1.jpg"));
        int initialSize = imageDao.read(testCatchId).orElse(Set.of()).size();

        imageDao.delete("nonexistent.jpg");

        Set<String> images = imageDao.read(testCatchId).orElse(Set.of());
        assertEquals(initialSize, images.size(),
                "Количество изображений не должно измениться при попытке удалить несуществующее");
    }

    @Test
    public void testGetAllNotSupported() {
        assertThrows(UnsupportedOperationException.class,
                () -> imageDao.getAll(),
                "Метод getAll() должен выбрасывать UnsupportedOperationException");
    }
}