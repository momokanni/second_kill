package com.kill.service;

import com.kill.VO.LoginVO;
import com.kill.entity.KillUser;

import javax.servlet.http.HttpServletResponse;

/**
 * class_name: KillUserService
 * package: com.kill.service
 * describe: 秒杀用户业务层
 * creat_user: sl
 * creat_date: 2018/7/16
 * creat_time: 15:31
 **/
public interface KillUserService {

    KillUser getKillUserById(Long id);

    boolean login(HttpServletResponse response,LoginVO loginVO);

    KillUser getByToken(HttpServletResponse response,String token);

    void getAllUsers();
}
