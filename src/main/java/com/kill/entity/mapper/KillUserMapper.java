package com.kill.entity.mapper;


import com.kill.entity.KillUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface KillUserMapper {

    @Select("SELECT * FROM kill_user where id = #{id}")
    @Results({
            @Result(column = "id",property = "id"),@Result(column = "nickname",property = "nickname"),
            @Result(column = "password",property = "password"),@Result(column = "salt",property = "salt"),
            @Result(column = "head_image",property = "headImage"),@Result(column = "last_login_date",property = "lastLoginDate"),
            @Result(column = "login_count",property = "loginCount")
    })
    KillUser getKillUserById(Long id);

    @Select("SELECT * FROM kill_user")
    List<KillUser> getAllUsers();

    @Insert("insert into jmeter_user_token (user_id,token) values (#{userId},#{tt})")
    void saveUserToken(@Param("userId") long userId, @Param("tt") String tt);
}
