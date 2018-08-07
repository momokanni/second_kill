package com.kill.entity.mapper;

import com.kill.entity.User;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * class_name: UserMapper
 * package: com.kill.entity.mapper
 * describe: 类映射
 * creat_user: sl
 * creat_date: 2018/7/4
 * creat_time: 11:26
 **/
public interface UserMapper {

    @Select("select * from user")
    @Results({
        @Result(column = "id",property = "id"),
        @Result(column = "username",property = "username"),
        @Result(column = "password",property = "password")
    })
    List<User> userList();
}
