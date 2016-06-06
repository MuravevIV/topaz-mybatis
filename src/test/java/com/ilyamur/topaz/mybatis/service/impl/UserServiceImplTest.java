package com.ilyamur.topaz.mybatis.service.impl;

import static org.junit.Assert.assertEquals;

import com.ilyamur.topaz.mybatis.ApplicationConfiguration;
import com.ilyamur.topaz.mybatis.ApplicationProfile;
import com.ilyamur.topaz.mybatis.service.DatabaseReset;
import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.service.UserService;
import com.ilyamur.topaz.mybatis.service.exception.EmailExistsException;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles(ApplicationProfile.DEV)
public class UserServiceImplTest {

    @Autowired
    private UserService target;

    @Autowired
    private DatabaseReset databaseReset;

    @Before
    public void before() {
        databaseReset.apply();
    }

    @Test
    public void saveThenFindById() throws EmailExistsException {
        User user = createUser("dan");
        target.save(user);
        User foundUser = target.findByIdUser(user.getIdUser());

        assertEquals(user.getIdUser(), user.getIdUser());
        assertEquals(user, foundUser);
    }

    @Test
    public void saveThenFindByName() throws EmailExistsException {
        User user = createUser("dan");
        target.save(user);
        User foundUser = target.findByName(user.getName());

        assertEquals(user.getIdUser(), user.getIdUser());
        assertEquals(user, foundUser);
    }

    @Test
    public void findThenSaveUser() throws EmailExistsException {
        User updatedUser = target.findByName("John");
        User savedUser = target.save(updatedUser);
        assertEquals(updatedUser, savedUser);
    }

    @Test
    public void updateUserEmail() throws EmailExistsException {
        String newEmail = "john2@gmail.com";
        User updatedUser = target.findByName("John");
        updatedUser.setEmail(newEmail);
        target.save(updatedUser);

        User foundUser = target.findByName("John");

        assertEquals(newEmail, foundUser.getEmail());
    }

    @Test(expected = EmailExistsException.class)
    public void updateUserEmailConflict() throws EmailExistsException {
        String sameEmail = "steve@gmail.com";
        User userSteve = createUser("steve");
        userSteve.setEmail(sameEmail);
        User userStevo = createUser("stevo");
        userStevo.setEmail(sameEmail);

        target.save(userSteve);
        target.save(userStevo);
    }

    private User createUser(String name) {
        User user = new User();
        user.setName(name);
        user.setEmail(name + "@gmail.com");
        user.setBirthday(LocalDate.of(1990, Month.APRIL, 14));
        HashSet<Role> roles = Sets.newHashSet();
        roles.add(Role.REGISTERED_USER);
        roles.add(Role.ADMIN);
        user.setRoles(roles);
        return user;
    }
}
