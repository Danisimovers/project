package sfedu.danil.dao;

import sfedu.danil.models.mappedSingletable.Competition;

import java.sql.SQLException;
import java.util.List;

public interface CompetitionSingleTable {
    void create(Competition competition);
    Competition read(String id);
    void update(Competition competition);
    void delete(String id);
    List<Competition> getAll() throws SQLException;
}
