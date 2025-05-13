package sfedu.danil.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import sfedu.danil.api.HibernateUtil;
import sfedu.danil.models.mappedJoined.Catch;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CatchImageJoinedDao implements BaseDao<Set<String>> {

    private static final Logger logger = LogManager.getLogger(CatchImageJoinedDao.class);
    private final String catchId;

    public CatchImageJoinedDao(String catchId) {
        this.catchId = catchId;
        logger.info("Создан CatchImageJoinedDao для улова с ID: {}", catchId);
    }

    @Override
    public void create(Set<String> filenames) throws SQLException {
        logger.info("Добавление изображений к улову {}: {}", catchId, filenames);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, catchId);
            if (catchEntity != null) {
                catchEntity.getImageFilenames().addAll(filenames);
                session.merge(catchEntity);
                tx.commit();
                logger.info("Успешно добавлено {} изображений к улову {}", filenames.size(), catchId);
            } else {
                logger.warn("Улов с ID {} не найден", catchId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при добавлении изображений к улову {}", catchId, e);
            throw new SQLException("Не удалось добавить изображения", e);
        }
    }

    @Override
    public Optional<Set<String>> read(String id) throws SQLException {
        logger.info("Получение списка изображений для улова {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Catch catchEntity = session.get(Catch.class, id);
            if (catchEntity != null) {
                logger.info("Найдено {} изображений для улова {}",
                        catchEntity.getImageFilenames().size(), id);
                return Optional.of(catchEntity.getImageFilenames());
            }
            logger.warn("Улов с ID {} не найден", id);
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Ошибка при получении изображений для улова {}", id, e);
            throw new SQLException("Не удалось получить изображения", e);
        }
    }

    @Override
    public void update(Set<String> filenames) throws SQLException {
        logger.info("Обновление списка изображений для улова {}", catchId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, catchId);
            if (catchEntity != null) {
                catchEntity.getImageFilenames().clear();
                catchEntity.getImageFilenames().addAll(filenames);
                session.merge(catchEntity);
                tx.commit();
                logger.info("Список изображений для улова {} успешно обновлен", catchId);
            } else {
                logger.warn("Улов с ID {} не найден", catchId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при обновлении изображений для улова {}", catchId, e);
            throw new SQLException("Не удалось обновить изображения", e);
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        logger.info("Удаление изображения с ID {} из улова {}", id, catchId);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Catch catchEntity = session.get(Catch.class, catchId);
            if (catchEntity != null) {
                boolean removed = catchEntity.getImageFilenames().remove(id);
                if (removed) {
                    session.merge(catchEntity);
                    tx.commit();
                    logger.info("Изображение с ID {} успешно удалено из улова {}", id, catchId);
                } else {
                    logger.warn("Изображение с ID {} не найдено для улова {}", id, catchId);
                }
            } else {
                logger.warn("Улов с ID {} не найден", catchId);
            }
        } catch (Exception e) {
            logger.error("Ошибка при удалении изображения с ID {} из улова {}", id, catchId, e);
            throw new SQLException("Не удалось удалить изображение", e);
        }
    }

    @Override
    public List<Set<String>> getAll() throws SQLException {
        logger.error("Метод getAll() для CatchImageJoinedDao");
        throw new UnsupportedOperationException("Метод не поддерживается для коллекций изображений");
    }
}