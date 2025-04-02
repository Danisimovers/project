package sfedu.danil.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.models.TestEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TestEntityDAO implements BaseDao<TestEntity> {
    private static final Logger logger = LogManager.getLogger(TestEntityDAO.class);

    @Override
    public void create(TestEntity entity) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(entity);
                transaction.commit();
                logger.info("Создан объект TestEntity: {}", entity);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Ошибка при создании TestEntity", e);
                throw new SQLException("Ошибка при создании TestEntity", e);
            }
        }
    }

    @Override
    public Optional<TestEntity> read(String id) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(TestEntity.class, Long.parseLong(id)));
        } catch (Exception e) {
            logger.error("Ошибка при получении TestEntity с id={}", id, e);
            throw new SQLException("Ошибка при получении TestEntity", e);
        }
    }

    @Override
    public void update(TestEntity entity) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(entity);
                transaction.commit();
                logger.info("Обновлен объект TestEntity: {}", entity);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Ошибка при обновлении TestEntity", e);
                throw new SQLException("Ошибка при обновлении TestEntity", e);
            }
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Optional.ofNullable(session.get(TestEntity.class, Long.parseLong(id)))
                        .ifPresent(session::remove);
                transaction.commit();
                logger.info("Удален объект TestEntity с id={}", id);
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Ошибка при удалении TestEntity", e);
                throw new SQLException("Ошибка при удалении TestEntity", e);
            }
        }
    }

    @Override
    public List<TestEntity> getAll() throws SQLException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM TestEntity", TestEntity.class).list();
        } catch (Exception e) {
            logger.error("Ошибка при получении всех TestEntity", e);
            throw new SQLException("Ошибка при получении всех TestEntity", e);
        }
    }
}