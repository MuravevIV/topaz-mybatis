package com.ilyamur.topaz.mybatis.service;

import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.service.exception.EmailExistsException;

import java.util.Collection;

public interface UserService {

    User save(User user) throws EmailExistsException;

    int updateEmail(long idUser, String newEmail);

    Collection<User> saveAll(Collection<User> users) throws EmailExistsException;

    void delete(User user);

    User findByIdUser(long idUser);

    User findByName(String name);

    Collection<User> getAll();
}
