package com.ilyamur.topaz.mybatis.repository;

import com.ilyamur.topaz.mybatis.entity.User;

import java.util.Collection;

public interface UserRepository {

    User save(User user);

    Collection<User> saveAll(Collection<User> users);

    void delete(long idUser);

    User findById(long idUser);

    User findByEmail(String email);

    User findByName(String name);

    Collection<User> getAll();
}
