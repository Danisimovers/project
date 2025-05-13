package sfedu.danil.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.models.mappedSuperclass.User;

import java.util.List;

public class UserMappedSuperclassDao implements UserMappedSuperclass {

    private static final Logger logger = LogManager.getLogger(UserMappedSuperclassDao.class);

    @Override
    public void create(User user) {
        logger.info("Сохранение сущности User: {}", user);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            logger.info("Сущность User успешно сохранена, ID: {}", user.getId());
        } catch (Exception e) {
            logger.error("Ошибка при сохранении сущности User: {}", user, e);
            throw new RuntimeException("Не удалось сохранить сущность User", e);
        }
    }

    @Override
    public <T extends User> T read(Class<T> clazz, String id) {
        logger.info("Поиск сущности {} с ID: {}", clazz.getSimpleName(), id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T user = session.get(clazz, id);
            if (user == null) {
                logger.warn("Сущность {} с ID {} не найдена", clazz.getSimpleName(), id);
            } else {
                logger.info("Сущность {} найдена: {}", clazz.getSimpleName(), user);
            }
            return user;
        } catch (Exception e) {
            logger.error("Ошибка при поиске сущности {} с ID: {}", clazz.getSimpleName(), id, e);
            throw new RuntimeException("Не удалось найти сущность " + clazz.getSimpleName() + " по ID", e);
        }
    }


    @Override
    public void update(User user) {
        logger.info("Обновление сущности User: {}", user);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            logger.info("Сущность User успешно обновлена: {}", user);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении сущности User: {}", user, e);
            throw new RuntimeException("Не удалось обновить сущность User", e);
        }
    }

    @Override
    public <T extends User> void delete(Class<T> clazz, String id) {
        logger.info("Удаление сущности {} с ID: {}", clazz.getSimpleName(), id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            T user = session.get(clazz, id);
            if (user != null) {
                session.remove(user);
                logger.info("Сущность {} успешно удалена, ID: {}", clazz.getSimpleName(), id);
            } else {
                logger.warn("Сущность {} с ID {} не найдена", clazz.getSimpleName(), id);
            }
            tx.commit();
        } catch (Exception e) {
            logger.error("Ошибка при удалении сущности {} с ID: {}", clazz.getSimpleName(), id, e);
            throw new RuntimeException("Не удалось удалить сущность " + clazz.getSimpleName() + " по ID", e);
        }
    }


    @Override
    public List<User> getAll() {
        logger.info("Поиск всех сущностей User");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<User> userEntities = session.createQuery("FROM Participant", User.class).list();
            userEntities.addAll(session.createQuery("FROM Organizer", User.class).list());
            logger.info("Найдено {} сущностей User", userEntities.size());
            return userEntities;
        } catch (Exception e) {
            logger.error("Ошибка при поиске всех сущностей User", e);
            throw new RuntimeException("Не удалось найти все сущности User", e);
        }
    }
}
