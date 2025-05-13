package sfedu.danil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.dao.CatchImageTablePerClassDao;
import sfedu.danil.models.mappedTableperclass.Catch;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class CatchImageTablePerClassDaoTest {

    private static String testCatchId;
    private static CatchImageTablePerClassDao imageDao;

    @BeforeAll
    public static void setUp() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = new Catch();
            testCatchId = UUID.randomUUID().toString(); // Генерируем ID вручную
            catchEntity.setId(testCatchId); // Устанавливаем ID перед сохранением
            session.persist(catchEntity);
            tx.commit();
        }
    }

    @BeforeEach
    public void init() {
        imageDao = new CatchImageTablePerClassDao(testCatchId);
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        Map<String, String> images = imageDao.read(testCatchId).orElse(Collections.emptyMap());
        for (String filename : images.keySet()) {
            imageDao.delete(filename);
        }
    }

    @AfterAll
    public static void tearDown() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
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
        Map<String, String> imageMappings = Map.of(
                "photo1.jpg", "Описание 1",
                "photo2.jpg", "Описание 2"
        );
        imageDao.create(imageMappings);

        Map<String, String> images = imageDao.read(testCatchId).orElse(Collections.emptyMap());
        assertEquals(2, images.size(), "Должно быть 2 изображения");
        assertTrue(images.keySet().containsAll(imageMappings.keySet()), "Коллекция должна содержать все добавленные файлы");
    }

    @Test
    public void testReadImages() throws SQLException {
        Map<String, String> imageMappings = Map.of("banner.jpg", "Баннер турнира");
        imageDao.create(imageMappings);

        Map<String, String> retrieved = imageDao.read(testCatchId).orElse(Collections.emptyMap());
        assertEquals(imageMappings, retrieved, "Полученные изображения должны совпадать с добавленными");
    }

    @Test
    public void testUpdateImages() throws SQLException {
        imageDao.create(Map.of(
                "old1.jpg", "Старое описание 1",
                "old2.jpg", "Старое описание 2"
        ));

        Map<String, String> newMappings = Map.of(
                "new1.jpg", "Новое описание 1",
                "new2.jpg", "Новое описание 2"
        );
        imageDao.update(newMappings);

        Map<String, String> updated = imageDao.read(testCatchId).orElse(Collections.emptyMap());
        assertEquals(newMappings, updated, "Коллекция должна быть полностью обновлена");
    }

    @Test
    public void testDeleteImage() throws SQLException {
        String filenameToDelete = "delete_me.jpg";
        imageDao.create(Map.of(
                filenameToDelete, "Удаляемое изображение",
                "keep.jpg", "Оставляемое изображение"
        ));

        imageDao.delete(filenameToDelete);

        Map<String, String> remaining = imageDao.read(testCatchId).orElse(Collections.emptyMap());
        assertFalse(remaining.containsKey(filenameToDelete), "Изображение должно быть удалено");
        assertEquals(1, remaining.size(), "Должно остаться 1 изображение");
    }

    @Test
    public void testGetAllImages() throws SQLException {
        Map<String, String> imageMappings = Map.of(
                "img1.jpg", "Описание 1",
                "img2.jpg", "Описание 2"
        );

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, testCatchId);
            catchEntity.getImageDescriptions().putAll(imageMappings);
            session.merge(catchEntity);
            tx.commit();
        }

        List<Map<String, String>> allImages = imageDao.getAll();
        assertEquals(1, allImages.size(), "Должен вернуться список с одной коллекцией");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Catch catchEntity = session.get(Catch.class, testCatchId);
            Map<String, String> loadedImages = catchEntity.getImageDescriptions();
            assertEquals(imageMappings, loadedImages, "Коллекция изображений должна совпадать");
        }
    }
}