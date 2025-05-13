package sfedu.danil.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.models.mappedSingletable.Competition;

import java.sql.SQLException;
import java.util.List;

public class CompetitionSingleTableDao implements CompetitionSingleTable {

    private static final Logger logger = LogManager.getLogger(CompetitionSingleTableDao.class);

    @Override
    public void create(Competition competition) {
        logger.info("Сохранение сущности Competition: {}", competition);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(competition);
            tx.commit();
            logger.info("Сущность Competition успешно сохранена, ID: {}", competition.getId());
        } catch (Exception e) {
            logger.error("Ошибка при сохранении сущности Competition: {}", competition, e);
            throw new RuntimeException("Не удалось сохранить сущность Competition", e);
        }
    }

    @Override
    public Competition read(String id) {
        logger.info("Поиск сущности Competition с ID: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Competition competition = session.get(Competition.class, id);
            if (competition == null) {
                logger.warn("Сущность Competition с ID {} не найдена", id);
            } else {
                logger.info("Сущность Competition найдена: {}", competition);
            }
            return competition;
        } catch (Exception e) {
            logger.error("Ошибка при поиске сущности Competition с ID: {}", id, e);
            throw new RuntimeException("Не удалось найти сущность Competition по ID", e);
        }
    }

    @Override
    public void update(Competition competition) {
        logger.info("Обновление сущности Competition: {}", competition);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(competition);
            tx.commit();
            logger.info("Сущность Competition успешно обновлена: {}", competition);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении сущности Competition: {}", competition, e);
            throw new RuntimeException("Не удалось обновить сущность Competition", e);
        }
    }

    @Override
    public void delete(String id) {
        logger.info("Удаление сущности Competition с ID: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Competition competition = session.get(Competition.class, id);
            if (competition != null) {
                session.remove(competition);
                tx.commit();
                logger.info("Сущность Competition с ID {} успешно удалена", id);
            } else {
                logger.warn("Сущность Competition с ID {} не найдена для удаления", id);
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении сущности Competition с ID: {}", id, e);
            throw new RuntimeException("Не удалось удалить сущность Competition", e);
        }
    }

    @Override
    public List<Competition> getAll() throws SQLException {
        logger.info("Получение всех соревнований с изображениями");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Competition> competitions = session.createQuery(
                    "FROM CompetitionSingleTable", Competition.class).list();

            logger.info("Найдено {} соревнований", competitions.size());
            return competitions;
        } catch (Exception e) {
            logger.error("Ошибка при получении соревнований", e);
            throw new SQLException("Не удалось получить список соревнований", e);
        }
    }
}
