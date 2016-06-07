package com.ilyamur.topaz.mybatis.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.ilyamur.topaz.mybatis.ApplicationConfiguration;
import com.ilyamur.topaz.mybatis.ApplicationProfile;
import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.service.DatabaseReset;
import com.ilyamur.topaz.mybatis.service.UserService;
import com.ilyamur.topaz.mybatis.service.exception.EmailExistsException;

import com.google.common.collect.Lists;
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

    private static final int EXISTING_ID_USER = 0;
    private static final String EXISTING_NAME = "John";
    private static final String ANY_NAME = "Dan";
    private static final String ANY_EMAIL = "dan@gmail.com";
    private static final LocalDate ANY_BIRTHDAY = LocalDate.of(1990, Month.APRIL, 27);
    private static final HashSet<Role> ANY_ROLES = Sets.newHashSet(Role.REGISTERED_USER, Role.ADMIN);

    @Autowired
    private UserService target;

    @Autowired
    private DatabaseReset databaseReset;

    @Before
    public void before() {
        databaseReset.apply();
    }

    @Test
    public void saveThenFindByIdUser() throws EmailExistsException {
        User user = createAnyUser();
        User savedUser = target.save(user);
        User foundUser = target.findByIdUser(user.getIdUser());

        assertEquals(user.getIdUser(), savedUser.getIdUser());
        assertEquals(savedUser, foundUser);
    }

    @Test
    public void saveThenFindByName() throws EmailExistsException {
        User user = createAnyUser();
        User savedUser = target.save(user);
        User foundUser = target.findByName(user.getName());

        assertEquals(user.getIdUser(), savedUser.getIdUser());
        assertEquals(savedUser, foundUser);
    }

    @Test
    public void findByIdUserThenSave() throws EmailExistsException {
        User foundUser = target.findByIdUser(EXISTING_ID_USER);
        User savedUser = target.save(foundUser);

        assertEquals(foundUser, savedUser);
    }

    @Test
    public void findByNameThenSave() throws EmailExistsException {
        User foundUser = target.findByName(EXISTING_NAME);
        User savedUser = target.save(foundUser);

        assertEquals(foundUser, savedUser);
    }

    @Test
    public void updateEmail() throws EmailExistsException {
        String newEmail = "johnalt@gmail.com";
        User updatedUser = target.findByName(EXISTING_NAME);
        updatedUser.setEmail(newEmail);
        target.save(updatedUser);

        User foundUser = target.findByName(EXISTING_NAME);

        assertEquals(newEmail, foundUser.getEmail());
    }

    @Test
    public void updateBirthday() throws EmailExistsException {
        LocalDate newBirthday = LocalDate.of(1985, Month.DECEMBER, 5);
        User updatedUser = target.findByName(EXISTING_NAME);
        updatedUser.setBirthday(newBirthday);
        target.save(updatedUser);

        User foundUser = target.findByName(EXISTING_NAME);

        assertEquals(newBirthday, foundUser.getBirthday());
    }

    @Test
    public void saveTwoUsersWithSameEmailSequentially_savesOnlyFirstUserAndThrowsEmailExistsException() throws EmailExistsException {
        String sameEmail = "same@gmail.com";
        String userAbbyName = "Abby";
        User userAbby = createUser(userAbbyName, sameEmail, ANY_BIRTHDAY, ANY_ROLES);
        String userBrianName = "Brian";
        User userBrian = createUser(userBrianName, sameEmail, ANY_BIRTHDAY, ANY_ROLES);

        target.save(userAbby);
        EmailExistsException exc = null;
        try {
            target.save(userBrian);
        } catch (EmailExistsException e) {
            exc = e;
        }

        assertNotNull("EmailExistsException expected", exc);
        assertEquals(String.format(EmailExistsException.MESSAGE, sameEmail), exc.getMessage());
        assertNotNull("User SHOULD be persisted in database", target.findByName(userAbbyName));
        assertNull("User SHOULD NOT be persisted in database", target.findByName(userBrianName));
    }

    @Test
    public void saveTwoUsersWithSameEmailSimultaneously_doNotSaveAnyUserAndThrowsEmailExistsException() throws EmailExistsException {
        String sameEmail = "same@gmail.com";
        String userAbbyName = "Abby";
        User userAbby = createUser(userAbbyName, sameEmail, ANY_BIRTHDAY, ANY_ROLES);
        String userBrianName = "Brian";
        User userBrian = createUser(userBrianName, sameEmail, ANY_BIRTHDAY, ANY_ROLES);

        EmailExistsException exc = null;
        try {
            target.saveAll(Lists.newArrayList(userAbby, userBrian));
        } catch (EmailExistsException e) {
            exc = e;
        }

        assertNotNull("EmailExistsException expected", exc);
        assertEquals(String.format(EmailExistsException.MESSAGE, sameEmail), exc.getMessage());
        assertNull("User SHOULD NOT be persisted in database", target.findByName(userAbbyName));
        assertNull("User SHOULD NOT be persisted in database", target.findByName(userBrianName));
    }

    private User createUser(String name, String email, LocalDate birthday, Set<Role> roles) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setBirthday(birthday);
        user.setRoles(roles);
        return user;
    }

    private User createAnyUser() {
        return createUser(ANY_NAME, ANY_EMAIL, ANY_BIRTHDAY, ANY_ROLES);
    }
}
