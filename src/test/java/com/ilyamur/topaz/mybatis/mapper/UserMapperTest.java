package com.ilyamur.topaz.mybatis.mapper;

import static org.junit.Assert.assertEquals;

import com.ilyamur.topaz.mybatis.ApplicationConfiguration;
import com.ilyamur.topaz.mybatis.ApplicationProfile;
import com.ilyamur.topaz.mybatis.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
@ActiveProfiles(ApplicationProfile.DEV)
public class UserMapperTest {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    PlatformTransactionManager txManager;

    private UserMapper target;

    @Before
    public void before() {
        target = sqlSessionTemplate.getMapper(UserMapper.class);
    }

    @Test
    public void insertThenFindById() {
        User savedUser = createUser("John");

        target.insert(savedUser);
        User foundUser = target.findById(savedUser.getIdUser());

        assertEquals(savedUser, foundUser);
    }

    @Test
    public void insertThenFindByEmail() {
        User savedUser = createUser("Jane");

        target.insert(savedUser);
        User foundUser = target.findByEmail(savedUser.getEmail());

        assertEquals(savedUser, foundUser);
    }

    private User createUser(String name) {
        User user = new User();
        user.setName(name);
        user.setEmail(name + "@gmail.com");
        user.setBirthday(LocalDate.of(1988, Month.FEBRUARY, 10));
        user.setRoles(Collections.emptySet());
        return user;
    }
}
