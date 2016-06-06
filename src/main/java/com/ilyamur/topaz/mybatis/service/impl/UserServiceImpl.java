package com.ilyamur.topaz.mybatis.service.impl;

import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.mapper.UserMapper;
import com.ilyamur.topaz.mybatis.service.UserService;
import com.ilyamur.topaz.mybatis.service.exception.EmailExistsException;

import com.google.common.collect.Lists;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {

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
            if (user.getIdUser() != null) {
                mapper.update(user);
                updateRoles(user);
            } else {
                mapper.insert(user);
                insertRoles(user);
            }
            String email = user.getEmail();
            int modCount = updateEmail(user.getIdUser(), email);
            if (modCount == 0){
                throw new EmailExistsException(email);
            }
        }
        return user;
    }

    @Override
    public int updateEmail(long idUser, String newEmail) {
        return mapper.updateEmail(idUser, newEmail);
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
}
