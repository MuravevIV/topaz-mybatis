package com.ilyamur.topaz.mybatis.mapper;

import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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
            "insert into user (id_user, name, birthday)",
            "values (#{idUser}, #{name}, #{birthday})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "idUser")
    int insert(User user);

    @Update({
            "update user",
            "set name = #{name}, birthday = #{birthday}",
            "where id_user = #{idUser}"
    })
    int update(User user);

    @Update({
            "update user",
            "set email = #{newEmail}",
            "where id_user = #{idUser}",
            "and not exists (select * from user where id_user <> #{idUser} AND email = #{newEmail})"
    })
    int updateEmail(@Param("idUser") long idUser, @Param("newEmail") String newEmail);

    @Delete({
            "delete from user",
            "where id_user = #{idUser}"
    })
    void delete(User user);

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
            @Result(column = "id_user", property = "roles", many = @Many(select = "selectRolesByIdUser"))
    })
    User selectByIdUser(long idUser);

    @Select({
            SELECT_ALL,
            FROM_USER,
            "where name = #{name}"
    })
    @ResultMap(USER_RESULT)
    User findByName(String name);

    @Select({
            "select r.id_role, r.name",
            "from role r join user_role ur on r.id_role = ur.id_role",
            "where ur.id_user = #{idUser}"
    })
    @Results(id = ROLE_RESULT, value = {
            @Result(column = "id_role", property = "idRole", id = true),
            @Result(column = "name", property = "name")
    })
    Set<Role> selectRolesByIdUser(long idUser);

    @Update("delete from user_role where id_user = #{idUser}")
    void deleteRoles(User user);

    @Update("insert into user_role (id_user, id_role) values (#{user.idUser}, #{role.idRole})")
    void insertRole(@Param("user") User user,
                    @Param("role") Role role);

    @Select({
            SELECT_ALL,
            FROM_USER,
            "where name = #{name}"
    })
    @ResultMap(USER_RESULT)
    User selectByName(String name);

    @Select({
            SELECT_ALL,
            FROM_USER
    })
    @ResultMap(USER_RESULT)
    Collection<User> selectAll();
}
