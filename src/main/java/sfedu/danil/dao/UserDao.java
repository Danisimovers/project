package sfedu.danil.dao;

import sfedu.danil.Constants;
import sfedu.danil.models.Role;
import sfedu.danil.models.User;
import sfedu.danil.api.PsqlDBConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserDao implements BaseDao<User> {

    private static final Logger logger = LogManager.getLogger(UserDao.class);

    @Override
    public void create(User user) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.CREATE_USER_QUERY)) {
            statement.setString(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.setObject(5, user.getRole().name(), Types.OTHER);
            statement.setDouble(6, Double.parseDouble(user.getRating()));
            if (user.getCompetitionId() != null) {
                statement.setString(7, user.getCompetitionId());
            } else {
                statement.setNull(7, Types.VARCHAR);
            }
            statement.executeUpdate();
            logger.info("Пользователь с id {} успешно добавлен", user.getId());
        } catch (IOException e) {
            logger.error("Ошибка при добавлении пользователя с id {}", user.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> read(String id) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.READ_USER_QUERY)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getString("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPhoneNumber(resultSet.getString("phone_number"));
                    user.setRole(Role.valueOf(resultSet.getString("role")));
                    user.setRating(resultSet.getString("rating"));
                    user.setCompetitionId(resultSet.getString("competition_id"));
                    logger.info("Пользователь с id {} найден", id);
                    return Optional.of(user);
                } else {
                    logger.warn("Пользователь с id {} не найден", id);
                    return Optional.empty();
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка при чтении пользователя с id {}", id, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.UPD_USER_QUERY)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhoneNumber());
            statement.setObject(4, user.getRole().name(), Types.OTHER);
            statement.setDouble(5, Double.parseDouble(user.getRating()));
            if (user.getCompetitionId() != null) {
                statement.setString(6, user.getCompetitionId());
            } else {
                statement.setNull(6, Types.VARCHAR);
            }
            statement.setString(7, user.getId());
            statement.executeUpdate();
            logger.info("Пользователь с id {} успешно обновлен", user.getId());
        } catch (IOException e) {
            logger.error("Ошибка при обновлении пользователя с id {}", user.getId(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.DEL_USER_QUERY)) {
            statement.setString(1, id);
            statement.executeUpdate();
            logger.info("Пользователь с id {} успешно удален", id);
        } catch (IOException e) {
            logger.error("Ошибка при удалении пользователя с id {}", id, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = PsqlDBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(Constants.RDALL_USER_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhoneNumber(resultSet.getString("phone_number"));
                user.setRole(Role.valueOf(resultSet.getString("role")));
                user.setRating(resultSet.getString("rating"));
                user.setCompetitionId(resultSet.getString("competition_id"));
                users.add(user);
            }
            logger.info("Получено {} пользователей", users.size());
        } catch (IOException e) {
            logger.error("Ошибка при получении списка пользователей", e);
            throw new RuntimeException(e);
        }

        return users;
    }
}
