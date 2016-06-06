package com.ilyamur.topaz.mybatis.repository;

import com.ilyamur.topaz.mybatis.entity.User;

import java.util.Collection;

public interface UserRepository {

    User save(User user);

    Collection<User> saveAll(Collection<User> users);

    void delete(User user);

    User findByIdUser(long idUser);

    User findByName(String name);

    Collection<User> getAll();
}
