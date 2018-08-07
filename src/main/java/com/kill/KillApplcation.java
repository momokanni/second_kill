package com.kill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author Administrator
 * @create 2018-07-02 10:31
 */
@SpringBootApplication
@MapperScan(basePackages = "com.kill.entity.mapper")
public class KillApplcation {

    public static void main(String[] args){
        SpringApplication.run(KillApplcation.class,args);
    }
}
