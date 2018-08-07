package com.kill.service.impl;

import com.kill.entity.User;
import com.kill.entity.mapper.UserMapper;
import com.kill.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 用戶业务逻辑
 *
 * @author Administrator
 * @create 2018-07-04 11:55
 */
@Service
public class DemoServiceImpl implements DemoService{
    

    @Autowired
    private UserMapper mapper;

    @Override
    public List<User> userList() {
        return mapper.userList();
    }
}
