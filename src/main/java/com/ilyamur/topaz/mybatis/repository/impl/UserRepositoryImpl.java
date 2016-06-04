package com.ilyamur.topaz.mybatis.repository.impl;

import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.mapper.RoleMapper;
import com.ilyamur.topaz.mybatis.mapper.UserMapper;
import com.ilyamur.topaz.mybatis.repository.UserRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private UserMapper userMapper;
    private RoleMapper roleMapper;

    @PostConstruct
    public void postConstruct() {
        userMapper = sqlSessionTemplate.getMapper(UserMapper.class);
        roleMapper = sqlSessionTemplate.getMapper(RoleMapper.class);
    }

    @Override
    public User save(User user) {
        if (user != null) {
            if (user.getIdUser() != null) {
                userMapper.update(user);
            } else {
                userMapper.insert(user);
            }
        }
        return user;
    }

    @Override
    public Collection<User> saveAll(Collection<User> users) {
        return users.stream().map(this::save).collect(Collectors.toList());
    }

    @Override
    public void delete(long idUser) {
        userMapper.delete(idUser);
    }

    @Override
    public User findById(long idUser) {
        return userMapper.findById(idUser);
    }

    @Override
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @Override
    public User findByName(String name) {
        return userMapper.findByName(name);
    }

    @Override
    public Collection<User> getAll() {
        return userMapper.getAll();
    }
}
