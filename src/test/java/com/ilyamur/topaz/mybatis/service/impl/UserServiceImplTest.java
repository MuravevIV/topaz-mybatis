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
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles(ApplicationProfile.DEV)
public class UserServiceImplTest {

    public static final LocalDate ANY_BIRTHDAY = LocalDate.of(1990, Month.APRIL, 27);
    public static final HashSet<Role> ANY_ROLES = Sets.newHashSet(Role.REGISTERED_USER, Role.ADMIN);
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
        User user = createUserDan();
        target.save(user);
        User foundUser = target.findByIdUser(user.getIdUser());

        assertEquals(user.getIdUser(), user.getIdUser());
        assertEquals(user, foundUser);
    }

    @Test
    public void saveThenFindByName() throws EmailExistsException {
        User user = createUserDan();
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
        String newEmail = "johnalt@gmail.com";
        User updatedUser = target.findByName("John");
        updatedUser.setEmail(newEmail);
        target.save(updatedUser);

        User foundUser = target.findByName("John");

        assertEquals(newEmail, foundUser.getEmail());
    }

    @Test(expected = EmailExistsException.class)
    public void updateUserEmailConflict() throws EmailExistsException {
        String sameEmail = "steve@gmail.com";
        User userSteven = createUser("Steven", sameEmail, ANY_BIRTHDAY, ANY_ROLES);
        User userStephen = createUser("Stephen", sameEmail, ANY_BIRTHDAY, ANY_ROLES);

        target.save(userSteven);
        target.save(userStephen);
    }

    private User createUser(String name, String email, LocalDate birthday, Set<Role> roles) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setBirthday(birthday);
        user.setRoles(roles);
        return user;
    }

    private User createUserDan() {
        return createUser("Dan", "dan@gmail.com", ANY_BIRTHDAY, ANY_ROLES);
    }
}
