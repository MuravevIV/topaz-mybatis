package com.ilyamur.topaz.mybatis.repository.impl;

import static org.junit.Assert.assertEquals;

import com.ilyamur.topaz.mybatis.ApplicationConfiguration;
import com.ilyamur.topaz.mybatis.ApplicationProfile;
import com.ilyamur.topaz.mybatis.DatabaseReset;
import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.repository.UserRepository;

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
public class UserRepositoryImplTest {

    @Autowired
    private UserRepository target;

    @Autowired
    private DatabaseReset databaseReset;

    @Before
    public void before() {
        databaseReset.apply();
    }

    @Test
    public void saveThenFindById() {
        User user = createUser();
        User savedUser = target.save(user);
        User foundUser = target.findByIdUser(savedUser.getIdUser());

        assertEquals(savedUser.getIdUser(), user.getIdUser());
        assertEquals(savedUser, foundUser);
    }

    @Test
    public void saveThenFindByName() {
        User user = createUser();
        User savedUser = target.save(user);
        User foundUser = target.findByName(savedUser.getName());

        assertEquals(savedUser.getIdUser(), user.getIdUser());
        assertEquals(savedUser, foundUser);
    }

    @Test
    public void updateUserEmail() {
        String newEmail = "john2@gmail.com";
        User updatedUser = target.findByName("John");
        updatedUser.setEmail(newEmail);
        target.save(updatedUser);

        User foundUser = target.findByName("John");

        assertEquals(newEmail, foundUser.getEmail());
    }

    private User createUser() {
        User user = new User();
        user.setName("Dan");
        user.setEmail("dan@gmail.com");
        user.setBirthday(LocalDate.of(1990, Month.APRIL, 14));
        HashSet<Role> roles = Sets.newHashSet();
        roles.add(Role.REGISTERED_USER);
        roles.add(Role.ADMIN);
        user.setRoles(roles);
        return user;
    }
}
