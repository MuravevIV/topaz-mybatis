package com.ilyamur.topaz.mybatis.service;

import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.service.exception.EmailExistsException;
import com.ilyamur.topaz.mybatis.service.exception.LoginExistsException;

import java.util.Collection;

public interface UserService {

    User save(User user) throws LoginExistsException, EmailExistsException;

    Collection<User> saveAll(Collection<User> users) throws LoginExistsException, EmailExistsException;

    void delete(User user);

    User findByIdUser(long idUser);

    User findByLogin(String login);

    Collection<User> getAll();
}
