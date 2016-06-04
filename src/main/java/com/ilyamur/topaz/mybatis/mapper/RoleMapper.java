package com.ilyamur.topaz.mybatis.mapper;

import com.ilyamur.topaz.mybatis.entity.Role;
import com.ilyamur.topaz.mybatis.entity.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface RoleMapper {

    @Select({
            "select r.id_role, r.name",
            "from role r join user_role ur on r.id_role = ur.id_role",
            "where ur.id_user = #{idUser}"
    })
    @Results({
            @Result(column = "id_role", property = "idRole", id = true),
            @Result(column = "name", property = "name")
    })
    Set<Role> findByUser(User user);

    int insert(Role role);
}
