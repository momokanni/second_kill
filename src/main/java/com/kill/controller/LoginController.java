package com.kill.controller;

import com.kill.VO.LoginVO;
import com.kill.VO.ResultVO;
import com.kill.redis.RedisServiceImpl;
import com.kill.service.KillUserService;
import com.kill.util.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录
 *
 * @author Administrator
 * @create 2018-07-14 20:02
 */
@Slf4j
@Api(tags ="登录控制器")
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private KillUserService killUserService;

    @ApiOperation(value="访问登录页面")
    @RequestMapping(value = "/to_login")
    public ModelAndView toLogin(){

        return new ModelAndView("login");
    }


    @ApiOperation(value="登录接口")
    @ApiImplicitParam(name = "loginVO", value = "登录展示层对象类", required = true, dataType = "LoginVO")
    @ApiResponses({
            @ApiResponse(code=301,message = "秒杀用户不存在"),
            @ApiResponse(code=302,message="登录密码错误")
    })
    @RequestMapping(value = "/do_login")
    @ResponseBody
    public ResultVO<String> doLogin(HttpServletResponse response,@Valid LoginVO loginVO){
        log.info(loginVO.toString());
        killUserService.login(response,loginVO);
        return ResultUtil.success(true);
    }
}
