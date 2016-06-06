package com.ilyamur.topaz.mybatis.mapper;

import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.Set;

public interface UserMapper {

    String SELECT_ALL = "select id_user, name, email, birthday";
    String FROM_USER = "from user";
    String USER_RESULT = "userResult";
    String ROLE_RESULT = "roleResult";

    @Insert({
            "insert into user (id_user, name, email, birthday)",
            "values (#{idUser}, #{name}, #{email}, #{birthday})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "idUser")
    int insert(User user);

    @Update({
            "update user",
            "set name = #{name}, email = #{email}, birthday = #{birthday}",
            "where id_user = #{idUser}"
    })
    int update(User user);

    @Delete({
            "delete from user",
            "where id_user = #{id_user}"
    })
    void delete(long id);

    @Select({
            SELECT_ALL,
            FROM_USER,
            "where id_user = #{idUser}"
    })
    @Results(id = USER_RESULT, value = {
            @Result(column = "id_user", property = "idUser", id = true),
            @Result(column = "name", property = "name"),
            @Result(column = "email", property = "email"),
            @Result(column = "birthday", property = "birthday"),
            @Result(column = "id_user", property = "roles", javaType = Set.class, many = @Many(select = "getRoles"))
    })
    User findById(long idUser);

    @Select({
            "select r.id_role, r.name",
            "from role r join user_role ur on r.id_role = ur.id_role",
            "where ur.id_user = #{idUser}"
    })
    @Results(id = ROLE_RESULT, value = {
            @Result(column = "id_role", property = "idRole", id = true),
            @Result(column = "name", property = "name")
    })
    Set<Role> getRoles(long idUser);

    @Select({
            SELECT_ALL,
            FROM_USER,
            "where email = #{email}"
    })
    @ResultMap(USER_RESULT)
    User findByEmail(String email);

    @Select({
            SELECT_ALL,
            FROM_USER,
            "where name = #{name}"
    })
    @ResultMap(USER_RESULT)
    User findByName(String name);

    @Select({
            SELECT_ALL,
            FROM_USER
    })
    @ResultMap(USER_RESULT)
    Collection<User> getAll();
}
