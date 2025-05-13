package sfedu.danil.dao;

import sfedu.danil.models.mappedSuperclass.User;

import java.util.List;

public interface UserMappedSuperclass {
    void create(User user);

    <T extends User> T read(Class<T> clazz, String id);

    void update(User user);

    <T extends User> void delete(Class<T> clazz, String id);

    List<User> getAll();
}

