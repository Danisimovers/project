package sfedu.danil.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
    void create(T entity) throws SQLException;
    Optional<T> read(String id) throws SQLException;
    void update(T entity) throws SQLException;
    void delete(String id) throws SQLException;
    List<T> getAll() throws SQLException;
}

