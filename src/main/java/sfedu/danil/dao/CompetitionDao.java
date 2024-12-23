package sfedu.danil.dao;

import sfedu.danil.Constants;
import sfedu.danil.models.Competition;
import sfedu.danil.api.PsqlDBConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompetitionDao implements BaseDao<Competition> {

    private static final Logger logger = LogManager.getLogger(CompetitionDao.class);

    @Override
    public void create(Competition competition) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.CREATE_COMP_QUERY)) {

            statement.setString(1, competition.getId());
            statement.setString(2, competition.getName());
            statement.setTimestamp(3, Timestamp.valueOf(competition.getDate()));

            statement.executeUpdate();
            logger.info("Соревнование с id {} успешно добавлено", competition.getId());
        } catch (SQLException e) {
            logger.error("Ошибка при добавлении соревнования с id {}", competition.getId(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Competition> read(String id) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.READ_COMP_QUERY)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Competition competition = new Competition();
                    competition.setId(resultSet.getString("id"));
                    competition.setName(resultSet.getString("name"));
                    competition.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                    logger.info("Соревнование с id {} найдено", id);
                    return Optional.of(competition);
                } else {
                    logger.warn("Соревнование с id {} не найдено", id);
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error("Ошибка при чтении соревнования с id {}", id, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Competition competition) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.UPD_COMP_QUERY)) {

            statement.setString(1, competition.getName());
            statement.setTimestamp(2, Timestamp.valueOf(competition.getDate()));
            statement.setString(3, competition.getId());

            statement.executeUpdate();
            logger.info("Соревнование с id {} успешно обновлено", competition.getId());
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении соревнования с id {}", competition.getId(), e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.DEL_COMP_QUERY)) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.info("Соревнование с id {} успешно удалено", id);
        } catch (SQLException e) {
            logger.error("Ошибка при удалении соревнования с id {}", id, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Competition> getAll() throws SQLException {
        List<Competition> competitions = new ArrayList<>();
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.RDALL_COMP_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Competition competition = new Competition();
                competition.setId(resultSet.getString("id"));
                competition.setName(resultSet.getString("name"));
                competition.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                competitions.add(competition);
            }
            logger.info("Получено {} соревнований", competitions.size());
        } catch (SQLException e) {
            logger.error("Ошибка при получении списка соревнований", e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return competitions;
    }
}
