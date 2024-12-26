package sfedu.danil.dao;

import sfedu.danil.Constants;
import sfedu.danil.models.Catch;
import sfedu.danil.api.PsqlDBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CatchDao implements BaseDao<Catch> {

    private static final Logger logger = LogManager.getLogger(CatchDao.class);

    @Override
    public void create(Catch catchEntry) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.CREATE_CATCH_QUERY)) {
            statement.setString(1, catchEntry.getId());
            statement.setDouble(2, catchEntry.getWeight());
            statement.setDouble(3, catchEntry.getPoints());
            statement.setString(4, catchEntry.getUserId());
            statement.setString(5, catchEntry.getCompetitionId());
            statement.setString(6, catchEntry.getFishType());
            statement.executeUpdate();
            logger.info("Запись Catch с id {} успешно добавлена", catchEntry.getId());
        } catch (IOException e) {
            logger.error("Ошибка при добавлении Catch с id {}", catchEntry.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Catch> read(String id) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.READ_CATCH_QUERY)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Catch catchEntry = new Catch();
                    catchEntry.setId(resultSet.getString("id"));
                    catchEntry.setWeight(resultSet.getDouble("weight"));
                    catchEntry.setPoints(resultSet.getDouble("points"));
                    catchEntry.setUserId(resultSet.getString("user_id"));
                    catchEntry.setCompetitionId(resultSet.getString("competition_id"));
                    catchEntry.setFishType(resultSet.getString("fish_type"));
                    logger.info("Catch с id {} найден", id);
                    return Optional.of(catchEntry);
                } else {
                    logger.warn("Catch с id {} не найден", id);
                    return Optional.empty();
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении Catch с id {}", id, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Catch catchEntry) throws SQLException {
        Optional<String> currentCompetitionIdOpt = getCurrentCompetitionId(catchEntry.getId());

        String competitionIdToUpdate = Optional.ofNullable(catchEntry.getCompetitionId())
                .filter(id -> !id.isEmpty())
                .or(() -> currentCompetitionIdOpt) // Если новое значение отсутствует, берем текущее из базы
                .orElseThrow(() -> new IllegalStateException("Не удалось определить competitionId для обновления"));

        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.UPD_CATCH_QUERY)) {
            statement.setDouble(1, catchEntry.getWeight());
            statement.setDouble(2, catchEntry.getPoints());
            statement.setString(3, catchEntry.getUserId());
            statement.setString(4, competitionIdToUpdate);
            statement.setString(5, catchEntry.getFishType());
            statement.setString(6, catchEntry.getId());
            statement.executeUpdate();
            logger.info("Catch с id {} успешно обновлен", catchEntry.getId());
        } catch (IOException e) {
            logger.error("Ошибка при обновлении Catch с id {}", catchEntry.getId(), e);
            throw new RuntimeException(e);
        }
    }

    // Метод для получения текущего competitionId по id записи Catch
    private Optional<String> getCurrentCompetitionId(String catchId) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.RDCUR_COMP_ID)) {
            statement.setString(1, catchId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.ofNullable(resultSet.getString("competition_id"));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


    @Override
    public void delete(String id) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.DEL_CATCH_QUERY)) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.info("Catch с id {} успешно удален", id);
        } catch (IOException e) {
            logger.error("Ошибка при удалении Catch с id {}", id, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Catch> getAll() throws SQLException {
        List<Catch> catches = new ArrayList<>();
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.RDALL_CATCH_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Catch catchEntry = new Catch();
                catchEntry.setId(resultSet.getString("id"));
                catchEntry.setWeight(resultSet.getDouble("weight"));
                catchEntry.setPoints(resultSet.getDouble("points"));
                catchEntry.setUserId(resultSet.getString("user_id"));
                catchEntry.setCompetitionId(resultSet.getString("competition_id"));
                catchEntry.setFishType(resultSet.getString("fish_type"));
                catches.add(catchEntry);
            }
            logger.info("Получено {} записей Catch", catches.size());
        } catch (IOException e) {
            logger.error("Ошибка при получении списка Catch", e);
            throw new RuntimeException(e);
        }

        return catches;
    }
}
