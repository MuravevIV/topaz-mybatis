package com.ilyamur.topaz.mybatis.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ilyamur.topaz.mybatis.ApplicationConfiguration;
import com.ilyamur.topaz.mybatis.ApplicationProfile;
import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import com.ilyamur.topaz.mybatis.service.DatabaseReset;
import com.ilyamur.topaz.mybatis.service.UserService;
import com.ilyamur.topaz.mybatis.service.exception.EmailExistsException;
import com.ilyamur.topaz.mybatis.service.exception.LoginExistsException;

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
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles(ApplicationProfile.DEV)
public class UserServiceImplTest {

    private static final int EXISTING_ID_USER = 0;
    private static final String EXISTING_LOGIN = "John";
    private static final String ANY_LOGIN = "Dan";
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
    public void saveThenFindByIdUser() throws LoginExistsException, EmailExistsException {
        User user = createAnyUser();
        User savedUser = target.save(user);
        User foundUser = target.findByIdUser(user.getIdUser());

        assertEquals(user.getIdUser(), savedUser.getIdUser());
        assertEquals(savedUser, foundUser);
    }

    @Test
    public void saveThenFindByLogin() throws LoginExistsException, EmailExistsException {
        User user = createAnyUser();
        User savedUser = target.save(user);
        User foundUser = target.findByLogin(user.getLogin());

        assertEquals(user.getIdUser(), savedUser.getIdUser());
        assertEquals(savedUser, foundUser);
    }

    @Test
    public void findByIdUserThenSave() throws LoginExistsException, EmailExistsException {
        User foundUser = target.findByIdUser(EXISTING_ID_USER);
        User savedUser = target.save(foundUser);

        assertEquals(foundUser, savedUser);
    }

    @Test
    public void findByLoginThenSave() throws LoginExistsException, EmailExistsException {
        User foundUser = target.findByLogin(EXISTING_LOGIN);
        User savedUser = target.save(foundUser);

        assertEquals(foundUser, savedUser);
    }

    @Test
    public void updateEmail() throws LoginExistsException, EmailExistsException {
        String newEmail = "johnalt@gmail.com";
        User updatedUser = target.findByLogin(EXISTING_LOGIN);
        updatedUser.setEmail(newEmail);
        target.save(updatedUser);

        User foundUser = target.findByLogin(EXISTING_LOGIN);

        assertEquals(newEmail, foundUser.getEmail());
    }

    @Test
    public void updateBirthday() throws LoginExistsException, EmailExistsException {
        LocalDate newBirthday = LocalDate.of(1985, Month.DECEMBER, 5);
        User updatedUser = target.findByLogin(EXISTING_LOGIN);
        updatedUser.setBirthday(newBirthday);
        target.save(updatedUser);

        User foundUser = target.findByLogin(EXISTING_LOGIN);

        assertEquals(newBirthday, foundUser.getBirthday());
    }

    @Test
    public void saveTwoUsersWithSameLoginSequentially_savesOnlyFirstUserAndThrowsLoginExistsException()
            throws LoginExistsException, EmailExistsException {

        String sameLogin = "BigBy";
        User userAbby = createUser(sameLogin, ANY_EMAIL, ANY_BIRTHDAY, ANY_ROLES);
        User userBrian = createUser(sameLogin, ANY_EMAIL, ANY_BIRTHDAY, ANY_ROLES);

        target.save(userAbby);
        LoginExistsException exc = null;
        try {
            target.save(userBrian);
        } catch (LoginExistsException e) {
            exc = e;
        }

        assertNotNull("LoginExistsException expected", exc);
        assertEquals(String.format(LoginExistsException.MESSAGE, sameLogin), exc.getMessage());
        Collection<User> users = target.getAll();
        assertTrue("User SHOULD be persisted in database", users.contains(userAbby));
        assertFalse("User SHOULD NOT be persisted in database", users.contains(userBrian));
    }

    @Test
    public void saveTwoUsersWithSameLoginSimultaneously_savesOnlyFirstUserAndThrowsLoginExistsException()
            throws LoginExistsException, EmailExistsException {

        String sameLogin = "BigBy";
        User userAbby = createUser(sameLogin, ANY_EMAIL, ANY_BIRTHDAY, ANY_ROLES);
        User userBrian = createUser(sameLogin, ANY_EMAIL, ANY_BIRTHDAY, ANY_ROLES);

        LoginExistsException exc = null;
        try {
            target.saveAll(Lists.newArrayList(userAbby, userBrian));
        } catch (LoginExistsException e) {
            exc = e;
        }

        assertNotNull("LoginExistsException expected", exc);
        assertEquals(String.format(LoginExistsException.MESSAGE, sameLogin), exc.getMessage());
        Collection<User> users = target.getAll();
        assertFalse("User SHOULD NOT be persisted in database", users.contains(userAbby));
        assertFalse("User SHOULD NOT be persisted in database", users.contains(userBrian));
    }

    @Test
    public void saveTwoUsersWithSameEmailSequentially_savesOnlyFirstUserAndThrowsEmailExistsException()
            throws LoginExistsException, EmailExistsException {

        String sameEmail = "same@gmail.com";
        String userAbbyLogin = "Abby";
        User userAbby = createUser(userAbbyLogin, sameEmail, ANY_BIRTHDAY, ANY_ROLES);
        String userBrianLogin = "Brian";
        User userBrian = createUser(userBrianLogin, sameEmail, ANY_BIRTHDAY, ANY_ROLES);

        target.save(userAbby);
        EmailExistsException exc = null;
        try {
            target.save(userBrian);
        } catch (EmailExistsException e) {
            exc = e;
        }

        assertNotNull("EmailExistsException expected", exc);
        assertEquals(String.format(EmailExistsException.MESSAGE, sameEmail), exc.getMessage());
        assertNotNull("User SHOULD be persisted in database", target.findByLogin(userAbbyLogin));
        assertNull("User SHOULD NOT be persisted in database", target.findByLogin(userBrianLogin));
    }

    @Test
    public void saveTwoUsersWithSameEmailSimultaneously_doNotSaveAnyUserAndThrowsEmailExistsException()
            throws LoginExistsException, EmailExistsException {

        String sameEmail = "same@gmail.com";
        String userAbbyLogin = "Abby";
        User userAbby = createUser(userAbbyLogin, sameEmail, ANY_BIRTHDAY, ANY_ROLES);
        String userBrianLogin = "Brian";
        User userBrian = createUser(userBrianLogin, sameEmail, ANY_BIRTHDAY, ANY_ROLES);

        EmailExistsException exc = null;
        try {
            target.saveAll(Lists.newArrayList(userAbby, userBrian));
        } catch (EmailExistsException e) {
            exc = e;
        }

        assertNotNull("EmailExistsException expected", exc);
        assertEquals(String.format(EmailExistsException.MESSAGE, sameEmail), exc.getMessage());
        assertNull("User SHOULD NOT be persisted in database", target.findByLogin(userAbbyLogin));
        assertNull("User SHOULD NOT be persisted in database", target.findByLogin(userBrianLogin));
    }

    private User createUser(String login, String email, LocalDate birthday, Set<Role> roles) {
        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setBirthday(birthday);
        user.setRoles(roles);
        return user;
    }

    private User createAnyUser() {
        return createUser(ANY_LOGIN, ANY_EMAIL, ANY_BIRTHDAY, ANY_ROLES);
    }
}
