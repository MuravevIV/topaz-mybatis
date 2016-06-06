package com.ilyamur.topaz.mybatis.repository.impl;

import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.mapper.UserMapper;
import com.ilyamur.topaz.mybatis.repository.UserRepository;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private UserMapper mapper;

    @PostConstruct
    public void postConstruct() {
        mapper = sqlSessionTemplate.getMapper(UserMapper.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public User save(User user) {
        if (user != null) {
            if (user.getIdUser() != null) {
                mapper.update(user);
                updateRoles(user);
            } else {
                mapper.insert(user);
                insertRoles(user);
            }
        }
        return user;
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

    @Override
    public Collection<User> saveAll(Collection<User> users) {
        return users.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public void delete(long idUser) {
        mapper.delete(idUser);
    }

    @Override
    public User findById(long idUser) {
        return mapper.findById(idUser);
    }

    @Override
    public User findByEmail(String email) {
        return mapper.findByEmail(email);
    }

    @Override
    public User findByName(String name) {
        return mapper.findByName(name);
    }

    @Override
    public Collection<User> getAll() {
        return mapper.getAll();
    }
}
