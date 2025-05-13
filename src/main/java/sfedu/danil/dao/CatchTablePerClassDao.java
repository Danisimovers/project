package sfedu.danil.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.models.mappedTableperclass.Catch;

import java.util.List;

public class CatchTablePerClassDao implements CatchTablePerClass {

    private static final Logger logger = LogManager.getLogger(CatchTablePerClassDao.class);

    @Override
    public void create(Catch catchEntity) {
        logger.info("Сохранение сущности Catch: {}", catchEntity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(catchEntity);
            tx.commit();
            logger.info("Сущность Catch успешно сохранена, ID: {}", catchEntity.getId());
        } catch (Exception e) {
            logger.error("Ошибка при сохранении сущности Catch: {}", catchEntity, e);
            throw new RuntimeException("Не удалось сохранить сущность Catch", e);
        }
    }

    @Override
    public Catch read(String id) {
        logger.info("Поиск сущности Catch с ID: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Catch catchEntity = session.get(Catch.class, id);
            if (catchEntity == null) {
                logger.warn("Сущность Catch с ID {} не найдена", id);
            } else {
                logger.info("Сущность Catch найдена: {}", catchEntity);
            }
            return catchEntity;
        } catch (Exception e) {
            logger.error("Ошибка при поиске сущности Catch с ID: {}", id, e);
            throw new RuntimeException("Не удалось найти сущность Catch по ID", e);
        }
    }

    @Override
    public void update(Catch catchEntity) {
        logger.info("Обновление сущности Catch: {}", catchEntity);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(catchEntity);
            tx.commit();
            logger.info("Сущность Catch успешно обновлена: {}", catchEntity);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении сущности Catch: {}", catchEntity, e);
            throw new RuntimeException("Не удалось обновить сущность Catch", e);
        }
    }

    @Override
    public void delete(String id) {
        logger.info("Удаление сущности Catch с ID: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, id);
            if (catchEntity != null) {
                session.remove(catchEntity);
                tx.commit();
                logger.info("Сущность Catch с ID {} успешно удалена", id);
            } else {
                logger.warn("Сущность Catch с ID {} не найдена для удаления", id);
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении сущности Catch с ID: {}", id, e);
            throw new RuntimeException("Не удалось удалить сущность Catch", e);
        }
    }

    @Override
    public List<Catch> getAll() {
        logger.info("Поиск всех сущностей Catch");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Catch> catchEntities = session.createQuery("FROM CatchPerClass", Catch.class).list();
            logger.info("Найдено {} сущностей Catch", catchEntities.size());
            return catchEntities;
        } catch (Exception e) {
            logger.error("Ошибка при поиске всех сущностей Catch", e);
            throw new RuntimeException("Не удалось найти все сущности Catch", e);
        }
    }
}
