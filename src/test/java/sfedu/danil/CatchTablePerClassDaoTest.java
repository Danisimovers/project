package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.dao.CatchTablePerClassDao;
import sfedu.danil.models.mappedTableperclass.Catch;
import sfedu.danil.models.mappedTableperclass.DayCatch;
import sfedu.danil.models.mappedTableperclass.NightCatch;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatchTablePerClassDaoTest {

    private static CatchTablePerClassDao catchDao;

    @BeforeAll
    public static void setUp() {
        catchDao = new CatchTablePerClassDao();
    }

    @AfterAll
    public static void tearDown() {
        HibernateUtil.getSessionFactory().close();
    }

    @Test
    public void testCreateDayCatch() {
        Catch dayCatch = new DayCatch("Окунь", 2.3, 4.0, "пользователь1", "соревнование1", LocalDateTime.now());
        catchDao.create(dayCatch);

        Catch retrieved = catchDao.read(dayCatch.getId());
        assertNotNull(retrieved, "Сущность DayCatch должна быть сохранена и доступна для получения.");
        assertEquals("Окунь", retrieved.getFishType(), "Тип рыбы должен совпадать.");
    }

    @Test
    public void testCreateNightCatch() {
        Catch nightCatch = new NightCatch("Судак", 3.7, 5.0, "пользователь2", "соревнование1", LocalDateTime.now());
        catchDao.create(nightCatch);

        Catch retrieved = catchDao.read(nightCatch.getId());
        assertNotNull(retrieved, "Сущность NightCatch должна быть сохранена и доступна для получения.");
        assertEquals("Судак", retrieved.getFishType(), "Тип рыбы должен совпадать.");
    }

    @Test
    public void testUpdateCatch() {
        Catch dayCatch = new DayCatch("Щука", 4.1, 6.5, "пользователь3", "соревнование2", LocalDateTime.now());
        catchDao.create(dayCatch);

        dayCatch.setFishType("Карп");
        catchDao.update(dayCatch);

        Catch updated = catchDao.read(dayCatch.getId());
        assertNotNull(updated, "Сущность Catch должна быть доступна после обновления.");
        assertEquals("Карп", updated.getFishType(), "Тип рыбы должен быть обновлен.");
    }

    @Test
    public void testDeleteCatch() {
        Catch nightCatch = new NightCatch("Сом", 6.8, 9.0, "пользователь4", "соревнование3", LocalDateTime.now());
        catchDao.create(nightCatch);

        catchDao.delete(nightCatch.getId());

        Catch deleted = catchDao.read(nightCatch.getId());
        assertNull(deleted, "Сущность Catch должна быть удалена.");
    }

    @Test
    public void testGetAllCatches() {
        catchDao.create(new DayCatch("Лещ", 1.2, 2.0, "пользователь5", "соревнование4", LocalDateTime.now()));
        catchDao.create(new NightCatch("Налим", 2.5, 3.5, "пользователь6", "соревнование4", LocalDateTime.now()));

        List<Catch> allCatches = catchDao.getAll();
        assertTrue(allCatches.size() > 0, "Список пойманных рыб не должен быть пустым.");
    }
}
