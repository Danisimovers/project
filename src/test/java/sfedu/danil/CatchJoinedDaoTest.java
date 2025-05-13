package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.dao.CatchJoinedDao;
import sfedu.danil.models.mappedJoined.Catch;
import sfedu.danil.models.mappedJoined.FeederCatch;
import sfedu.danil.models.mappedJoined.SpinningCatch;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CatchJoinedDaoTest {

    private static CatchJoinedDao catchDao;

    @BeforeAll
    public static void setUp() {
        catchDao = new CatchJoinedDao();
    }

    @AfterAll
    public static void tearDown() {
        HibernateUtil.getSessionFactory().close();
    }

    @Test
    public void testCreateCatch() {
        Catch feederCatch = new FeederCatch("Карп", 5.5, 8.0, "пользователь1", "соревнование1", "Черви", "Фидерное удилище");
        catchDao.create(feederCatch);

        Catch retrievedCatch = catchDao.read(feederCatch.getId());
        assertNotNull(retrievedCatch, "Сущность Catch должна быть сохранена и доступна для получения.");
        assertEquals(feederCatch.getFishType(), retrievedCatch.getFishType(), "Тип рыбы должен совпадать.");
    }

    @Test
    public void testCreateSpinningCatch() {
        Catch spinningCatch = new SpinningCatch("Щука", 3.2, 6.0, "пользователь2", "соревнование1", "Блесна", "Спиннинговое удилище");
        catchDao.create(spinningCatch);

        Catch retrievedCatch = catchDao.read(spinningCatch.getId());
        assertNotNull(retrievedCatch, "Сущность Catch должна быть сохранена и доступна для получения.");
        assertEquals(spinningCatch.getFishType(), retrievedCatch.getFishType(), "Тип рыбы должен совпадать.");
    }

    @Test
    public void testUpdateCatch() {
        Catch feederCatch = new FeederCatch("Карп", 5.5, 8.0, "пользователь1", "соревнование1", "Черви", "Фидерное удилище");
        catchDao.create(feederCatch);

        feederCatch.setFishType("Линь");
        catchDao.update(feederCatch);

        Catch updatedCatch = catchDao.read(feederCatch.getId());
        assertNotNull(updatedCatch, "Сущность Catch должна быть доступна для получения после обновления.");
        assertEquals("Линь", updatedCatch.getFishType(), "Тип рыбы должен быть обновлен.");
    }

    @Test
    public void testDeleteCatch() {
        Catch spinningCatch = new SpinningCatch("Щука", 3.2, 6.0, "пользователь2", "соревнование1", "Блесна", "Спиннинговое удилище");
        catchDao.create(spinningCatch);

        catchDao.delete(spinningCatch.getId());

        Catch deletedCatch = catchDao.read(spinningCatch.getId());
        assertNull(deletedCatch, "Сущность Catch должна быть удалена и недоступна для получения.");
    }

    @Test
    public void testGetAllCatches() {
        catchDao.create(new FeederCatch("Карп", 5.5, 8.0, "пользователь1", "соревнование1", "Черви", "Фидерное удилище"));
        catchDao.create(new SpinningCatch("Щука", 3.2, 6.0, "пользователь2", "соревнование1", "Блесна", "Спиннинговое удилище"));

        List<Catch> catches = catchDao.getAll();
        assertTrue(catches.size() > 0, "Должны быть получены сущности Catch из базы данных.");
    }
}
