package com.ilyamur.topaz.mybatis.service.impl;

import com.google.common.collect.Lists;
import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.mapper.UserMapper;
import com.ilyamur.topaz.mybatis.service.UserService;
import com.ilyamur.topaz.mybatis.service.exception.EmailExistsException;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

    private static final String CONSTRAINT_UNIQUE_EMAIL = "U0_USER_EMAIL";

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private UserMapper mapper;

    @PostConstruct
    public void postConstruct() {
        mapper = sqlSessionTemplate.getMapper(UserMapper.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User save(User user) throws EmailExistsException {
        if (user != null) {
            try {
                updateOrInsert(user);
            } catch (DuplicateKeyException e) {
                if (isConstraintViolation(e, CONSTRAINT_UNIQUE_EMAIL)) {
                    throw new EmailExistsException(user.getEmail(), e);
                } else {
                    throw e;
                }
            }
        }
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public Collection<User> saveAll(Collection<User> users) throws EmailExistsException {
        ArrayList<User> savedUsers = Lists.newArrayList();
        for (User user : users) {
            savedUsers.add(save(user));
        }
        return savedUsers;
    }

    @Override
    public void delete(User user) {
        mapper.delete(user);
    }

    @Override
    public User findByIdUser(long idUser) {
        return mapper.selectByIdUser(idUser);
    }

    @Override
    public User findByName(String name) {
        return mapper.selectByName(name);
    }

    @Override
    public Collection<User> getAll() {
        return mapper.selectAll();
    }

    private void updateOrInsert(User user) {
        if (user.getIdUser() != null) {
            mapper.update(user);
            updateRoles(user);
        } else {
            mapper.insert(user);
            insertRoles(user);
        }
    }

    private void updateRoles(User user) {
        mapper.deleteRoles(user);
        insertRoles(user);
    }

    private void insertRoles(User user) {
        for (Role role : user.getRoles()) {
            mapper.insertRole(user, role);
        }
    }

    private boolean isConstraintViolation(DuplicateKeyException e, String constraintName) {
        return e.getMessage().contains(String.format(" %s ", constraintName));
    }
}
