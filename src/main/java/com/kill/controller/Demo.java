package com.kill.controller;

import com.kill.VO.ResultVO;
import com.kill.entity.User;
import com.kill.enums.StatusCode;
import com.kill.rabbitmq.MQSender;
import com.kill.redis.RedisServiceImpl;
import com.kill.redis.KillUserKey;
import com.kill.service.DemoService;
import com.kill.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


/**
 * 样例
 *
 * @author Administrator
 * @create 2018-07-02 10:34
 */
@Controller
@RequestMapping(value = "/demo")
public class Demo {

    @Autowired
    private DemoService service;

    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private MQSender sender;


    @GetMapping(value = "/")
    @ResponseBody
    public ResultVO<String> hello(){
        return ResultUtil.success(null);
    }

    @GetMapping(value = "/demoError")
    @ResponseBody
    public ResultVO<String> error(){

        return ResultUtil.error(StatusCode.FAILED.getCode(),StatusCode.FAILED.getMsg());
    }

    @GetMapping(value = "/thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","SunL");
        model.addAttribute("age","28");
        return "demo";
    }

    @GetMapping(value = "/users")
    @ResponseBody
    public ResultVO<List<User>> userList(){
        List<User> list = service.userList();
        if(list.size() > 0){
            return ResultUtil.success(list);
        }
        return ResultUtil.error(StatusCode.FAILED.getCode(),StatusCode.FAILED.getMsg());
    }

    @GetMapping(value = "/redis/get")
    @ResponseBody
    public ResultVO<Long> redisGet(){
        Long keys = redisService.get(KillUserKey.getById,":"+1,Long.class);
        return ResultUtil.success(keys);
    }

    @GetMapping(value = "/redis/set")
    @ResponseBody
    public ResultVO<Boolean> redisSet(){
        boolean status = redisService.set(KillUserKey.getById,":" + 1,"hello,my_redis");
        return ResultUtil.success(status);
    }

    @GetMapping(value = "/mq")
    @ResponseBody
    public ResultVO<String> mq(){
        sender.headerSend("header msg");
        //sender.send("hello rabbit");
        //sender.topicSend("topic_message");
        return ResultUtil.success();
    }

}
