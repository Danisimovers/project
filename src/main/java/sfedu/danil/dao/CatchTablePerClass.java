package sfedu.danil.dao;

import sfedu.danil.models.mappedTableperclass.Catch;

import java.util.List;

public interface CatchTablePerClass {
    void create(Catch catchEntity);
    Catch read(String id);
    void update(Catch catchEntity);
    void delete(String id);
    List<Catch> getAll();
}
