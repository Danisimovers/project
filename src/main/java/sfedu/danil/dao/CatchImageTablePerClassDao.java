package sfedu.danil.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.models.mappedTableperclass.Catch;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CatchImageTablePerClassDao implements BaseDao<Map<String, String>> {

    private static final Logger logger = LogManager.getLogger(CatchImageTablePerClassDao.class);
    private final String catchId;

    public CatchImageTablePerClassDao(String catchId) {
        this.catchId = catchId;
        logger.info("Создан CatchImageTablePerClassDao для улова с ID: {}", catchId);
    }

    @Override
    public void create(Map<String, String> imageMappings) throws SQLException {
        logger.info("Добавление изображений к улову {}: {}", catchId, imageMappings);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, catchId);
            if (catchEntity != null) {
                catchEntity.getImageDescriptions().putAll(imageMappings);
                session.merge(catchEntity);
                tx.commit();
                logger.info("Успешно добавлено {} изображений к улову {}", imageMappings.size(), catchId);
            } else {
                logger.warn("Улов с ID {} не найден", catchId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при добавлении изображений к улову {}", catchId, e);
            throw new SQLException("Не удалось добавить изображения", e);
        }
    }

    @Override
    public Optional<Map<String, String>> read(String id) throws SQLException {
        logger.info("Получение изображений для улова {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Catch catchEntity = session.get(Catch.class, id);
            if (catchEntity != null) {
                logger.info("Найдено {} изображений для улова {}",
                        catchEntity.getImageDescriptions().size(), id);
                return Optional.of(catchEntity.getImageDescriptions());
            }
            logger.warn("Улов с ID {} не найден", id);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Ошибка при получении изображений для улова {}", id, e);
            throw new SQLException("Не удалось получить изображения", e);
        }
    }

    @Override
    public void update(Map<String, String> imageMappings) throws SQLException {
        logger.info("Обновление изображений для улова {}", catchId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, catchId);
            if (catchEntity != null) {
                catchEntity.getImageDescriptions().clear();
                catchEntity.getImageDescriptions().putAll(imageMappings);
                session.merge(catchEntity);
                tx.commit();
                logger.info("Изображения для улова {} успешно обновлены", catchId);
            } else {
                logger.warn("Улов с ID {} не найден", catchId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при обновлении изображений для улова {}", catchId, e);
            throw new SQLException("Не удалось обновить изображения", e);
        }
    }

    @Override
    public void delete(String filename) throws SQLException {
        logger.info("Удаление изображения {} из улова {}", filename, catchId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, catchId);
            if (catchEntity != null) {
                String removedValue = catchEntity.getImageDescriptions().remove(filename);
                if (removedValue != null) {
                    session.merge(catchEntity);
                    tx.commit();
                    logger.info("Изображение {} успешно удалено из улова {}", filename, catchId);
                } else {
                    logger.warn("Изображение {} не найдено для улова {}", filename, catchId);
                }
            } else {
                logger.warn("Улов с ID {} не найден", catchId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении изображения {} из улова {}", filename, catchId, e);
            throw new SQLException("Не удалось удалить изображение", e);
        }
    }

    @Override
    public List<Map<String, String>> getAll() throws SQLException {
        logger.info("Получение всех изображений улова {}", catchId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Catch catchEntity = session.get(Catch.class, catchId);
            if (catchEntity != null) {
                return Collections.singletonList(catchEntity.getImageDescriptions());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            logger.error("Ошибка при получении изображений", e);
            throw new SQLException("Не удалось получить изображения", e);
        }
    }
}