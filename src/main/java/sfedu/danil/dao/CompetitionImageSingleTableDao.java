package sfedu.danil.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.models.mappedSingletable.Competition;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CompetitionImageSingleTableDao implements BaseDao<List<String>> {

    private static final Logger logger = LogManager.getLogger(CompetitionImageSingleTableDao.class);
    private final String competitionId;

    public CompetitionImageSingleTableDao(String competitionId) {
        this.competitionId = competitionId;
        logger.info("Создан CompetitionImageSingleTableDao для соревнования с ID: {}", competitionId);
    }

    @Override
    public void create(List<String> filenames) throws SQLException {
        logger.info("Добавление изображений к соревнованию {}: {}", competitionId, filenames);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Competition competition = session.get(Competition.class, competitionId);
            if (competition != null) {
                competition.getImageFilenames().addAll(filenames);
                session.merge(competition);
                tx.commit();
                logger.info("Успешно добавлено {} изображений к соревнованию {}", filenames.size(), competitionId);
            } else {
                logger.warn("Соревнование с ID {} не найдено", competitionId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при добавлении изображений к соревнованию {}", competitionId, e);
            throw new SQLException("Не удалось добавить изображения", e);
        }
    }

    @Override
    public Optional<List<String>> read(String id) throws SQLException {
        logger.info("Получение списка изображений для соревнования {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Competition competition = session.get(Competition.class, id);
            if (competition != null) {
                logger.info("Найдено {} изображений для соревнования {}",
                        competition.getImageFilenames().size(), id);
                return Optional.of(competition.getImageFilenames());
            }
            logger.warn("Соревнование с ID {} не найдено", id);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Ошибка при получении изображений для соревнования {}", id, e);
            throw new SQLException("Не удалось получить изображения", e);
        }
    }

    @Override
    public void update(List<String> filenames) throws SQLException {
        logger.info("Обновление списка изображений для соревнования {}", competitionId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Competition competition = session.get(Competition.class, competitionId);
            if (competition != null) {
                competition.getImageFilenames().clear();
                competition.getImageFilenames().addAll(filenames);
                session.merge(competition);
                tx.commit();
                logger.info("Список изображений для соревнования {} успешно обновлен", competitionId);
            } else {
                logger.warn("Соревнование с ID {} не найдено", competitionId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при обновлении изображений для соревнования {}", competitionId, e);
            throw new SQLException("Не удалось обновить изображения", e);
        }
    }

    @Override
    public void delete(String filename) throws SQLException {
        logger.info("Удаление изображения {} из соревнования {}", filename, competitionId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Competition competition = session.get(Competition.class, competitionId);
            if (competition != null) {
                boolean removed = competition.getImageFilenames().remove(filename);
                if (removed) {
                    session.merge(competition);
                    tx.commit();
                    logger.info("Изображение {} успешно удалено из соревнования {}", filename, competitionId);
                } else {
                    logger.warn("Изображение {} не найдено для соревнования {}", filename, competitionId);
                }
            } else {
                logger.warn("Соревнование с ID {} не найдено", competitionId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении изображения {} из соревнования {}", filename, competitionId, e);
            throw new SQLException("Не удалось удалить изображение", e);
        }
    }

    @Override
    public List<List<String>> getAll() throws SQLException {
        logger.info("Получение всех изображений соревнования {}", competitionId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Competition competition = session.get(Competition.class, competitionId);
            if (competition != null) {
                return Collections.singletonList(competition.getImageFilenames());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Ошибка при получении изображений", e);
            throw new SQLException("Не удалось получить изображения", e);
        }
    }
    }