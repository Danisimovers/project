package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.dao.TestEntityDAO;
import sfedu.danil.models.TestEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestEntityDAOTest {

    private final TestEntityDAO dao = new TestEntityDAO();
    private TestEntity testEntity;

    @BeforeAll
    static void setupDatabase() {
        HibernateUtil.getSessionFactory(); // Проверяем, что Hibernate инициализирован
    }

    @BeforeEach
    void setUp() throws SQLException {
        testEntity = new TestEntity("Test Name", "Test Description", true);
        dao.create(testEntity);
    }

    @AfterEach
    void tearDown() throws SQLException {
        dao.delete(String.valueOf(testEntity.getId()));
    }

    @Test
    @Order(1)
    void testCreate() throws SQLException {
        Optional<TestEntity> retrievedEntity = dao.read(String.valueOf(testEntity.getId()));
        assertTrue(retrievedEntity.isPresent(), "Созданная сущность должна существовать в базе данных");
        assertEquals("Test Name", retrievedEntity.get().getName(), "Имя должно совпадать");
    }

    @Test
    @Order(2)
    void testRead() throws SQLException {
        Optional<TestEntity> retrievedEntity = dao.read(String.valueOf(testEntity.getId()));
        assertTrue(retrievedEntity.isPresent(), "Объект должен быть найден");
    }

    @Test
    @Order(3)
    void testUpdate() throws SQLException {
        testEntity.setName("Updated Name");
        dao.update(testEntity);
        Optional<TestEntity> updatedEntity = dao.read(String.valueOf(testEntity.getId()));
        assertTrue(updatedEntity.isPresent(), "Обновленный объект должен существовать");
        assertEquals("Updated Name", updatedEntity.get().getName(), "Имя должно обновиться");
    }

    @Test
    @Order(4)
    void testDelete() throws SQLException {
        dao.delete(String.valueOf(testEntity.getId()));
        Optional<TestEntity> deletedEntity = dao.read(String.valueOf(testEntity.getId()));
        assertFalse(deletedEntity.isPresent(), "Удаленный объект не должен существовать");
    }

    @Test
    @Order(5)
    void testGetAll() throws SQLException {
        List<TestEntity> entities = dao.getAll();
        assertFalse(entities.isEmpty(), "Список не должен быть пустым");
    }
}