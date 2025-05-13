package sfedu.danil.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.models.TestEntity;

import java.util.List;

public class TestEntityDAO {

    private static final Logger logger = LogManager.getLogger(TestEntityDAO.class);

    public void save(TestEntity entity) {
        logger.info("Сохранение сущности: {}", entity);
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(entity);
            tx.commit();
            logger.info("Сущность успешно сохранена, ID: {}", entity.getId());
        } catch (Exception e) {
            logger.error("Ошибка при сохранении сущности: {}", entity, e);
            throw new RuntimeException("Не удалось сохранить сущность", e);
        }
    }

    public TestEntity getById(Long id) {
        logger.info("Поиск сущности с ID: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            TestEntity entity = session.get(TestEntity.class, id);
            if (entity == null) {
                logger.warn("Сущность с ID {} не найдена", id);
            } else {
                logger.info("Сущность найдена: {}", entity);
            }
            return entity;
        } catch (Exception e) {
            logger.error("Ошибка при поиске сущности с ID: {}", id, e);
            throw new RuntimeException("Не удалось найти сущность по ID", e);
        }
    }

    public List<TestEntity> getAll() {
        logger.info("Поиск всех сущностей");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<TestEntity> entities = session.createQuery("FROM TestEntity", TestEntity.class).list();
            logger.info("Найдено {} сущностей", entities.size());
            return entities;
        } catch (Exception e) {
            logger.error("Ошибка при поиске всех сущностей", e);
            throw new RuntimeException("Не удалось найти все сущности", e);
        }
    }

    public void update(TestEntity entity) {
        logger.info("Обновление сущности: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(entity);
            tx.commit();
            logger.info("Сущность успешно обновлена: {}", entity);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении сущности: {}", entity, e);
            throw new RuntimeException("Не удалось обновить сущность", e);
        }
    }

    public void delete(TestEntity entity) {
        logger.info("Удаление сущности: {}", entity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.remove(entity);
            tx.commit();
            logger.info("Сущность успешно удалена: {}", entity);
        } catch (Exception e) {
            logger.error("Ошибка при удалении сущности: {}", entity, e);
            throw new RuntimeException("Не удалось удалить сущность", e);
        }
    }
}
