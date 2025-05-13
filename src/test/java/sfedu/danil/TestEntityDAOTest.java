package sfedu.danil;

import org.junit.jupiter.api.*;
import sfedu.danil.dao.TestEntityDAO;
import sfedu.danil.models.Metadata;
import sfedu.danil.models.TestEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestEntityDAOTest {
    private static TestEntityDAO dao;
    private static Long entityId;

    @BeforeAll
    static void setup() {
        dao = new TestEntityDAO();
    }

    @Test
    @Order(1)
    void testSave() {
        Metadata metadata = new Metadata("ТЕСТ");
        TestEntity entity = new TestEntity("Имя", "Описание", true);
        entity.setMetadata(metadata);

        dao.save(entity);
        assertNotNull(entity.getId());
        entityId = entity.getId();
    }

    @Test
    @Order(2)
    void testGetById() {
        assertNotNull(entityId);
        TestEntity entity = dao.getById(entityId);

        assertNotNull(entity);
        assertEquals("Имя", entity.getName());
        assertEquals("Описание", entity.getDescription());
        assertTrue(entity.getIscheck());
        assertNotNull(entity.getMetadata());
        assertEquals("ТЕСТ", entity.getMetadata().getCreatedBy());
    }

    @Test
    @Order(3)
    void testUpdate() {
        assertNotNull(entityId);
        TestEntity entity = dao.getById(entityId);
        assertNotNull(entity);

        entity.setName("Обновлённое имя");
        entity.setDescription("Обновлённое описание");

        dao.update(entity);

        TestEntity updatedEntity = dao.getById(entityId);
        assertNotNull(updatedEntity);
        assertEquals("Обновлённое имя", updatedEntity.getName());
        assertEquals("Обновлённое описание", updatedEntity.getDescription());
    }

    @Test
    @Order(4)
    void testGetAll() {
        List<TestEntity> entities = dao.getAll();
        assertFalse(entities.isEmpty(), "Список сущностей не должен быть пустым.");
        assertTrue(entities.stream().anyMatch(entity -> entity.getId().equals(entityId)), "Сохранённый объект должен присутствовать в списке.");
    }

    @Test
    @Order(5)
    void testDelete() {
        assertNotNull(entityId);
        TestEntity entity = dao.getById(entityId);
        assertNotNull(entity);

        dao.delete(entity);
        TestEntity deletedEntity = dao.getById(entityId);
        assertNull(deletedEntity, "Удалённая сущность должна быть null.");
    }
}