package com.ilyamur.topaz.mybatis.mapper;

import com.ilyamur.topaz.mybatis.entity.TransactionToken;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface TransactionTokenMapper {

    @Select({
            "select *",
            "from trans_token"
    })
    @Results(value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "trans_id", property = "transaction", jdbcType = JdbcType.VARCHAR),
            @Result(column = "token_id", property = "token", jdbcType = JdbcType.VARCHAR)
    })
    List<TransactionToken> getAll();

    @Select({
            "select *",
            "from trans_token",
            "where id = #{id}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "trans_id", property = "transaction", jdbcType = JdbcType.VARCHAR),
            @Result(column = "token_id", property = "token", jdbcType = JdbcType.VARCHAR)
    })
    TransactionToken getById(long id);

    @Insert({
            "insert into trans_token (trans_id, token_id)",
            "values (#{transaction}, #{token})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(TransactionToken record);

    @Select({
            "select count(*)",
            "from trans_token"
    })
    long count();

    @Delete({
            "delete from trans_token",
            "where id = #{id}"
    })
    void deleteById(TransactionToken token);

    @Delete({
            "delete from trans_token",
            "where id = #{id}"
    })
    void deleteByLongId(long id);

    @Delete({
            "delete from trans_token",
            "where trans_id = #{transaction}"
    })
    void deleteByTransaction(TransactionToken token);

    @Update({
            "update trans_token",
            "set trans_id = #{transaction}, token_id = #{token}",
            "where id = #{id}"
    })
    int update(TransactionToken record);

    @Select({
            "select id, trans_id, token_id",
            "from trans_token",
            "where trans_id = #{transaction}"
    })
    @Results({
            @Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "trans_id", property = "transaction", jdbcType = JdbcType.VARCHAR),
            @Result(column = "token_id", property = "token", jdbcType = JdbcType.VARCHAR)
    })
    TransactionToken selectByTransaction(String transaction);
}
