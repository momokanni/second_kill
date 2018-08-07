package com.kill.util;

import com.kill.entity.KillUser;
import com.kill.entity.User;
import com.kill.service.KillUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 创建压测虚拟用户
 *
 * @author Administrator
 * @create 2018-07-25 20:39
 */
@Controller
@RequestMapping("/createUser")
public class CreateUserForJMeter {

    @Autowired
    private KillUserService killUserService;

    @RequestMapping("/createToken")
    public void createToken(){
        killUserService.getAllUsers();
    }
}
