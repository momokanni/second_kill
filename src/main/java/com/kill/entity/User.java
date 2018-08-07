package com.kill.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 *
 * @author Administrator
 * @create 2018-07-04 11:05
 */
@Data
public class User implements Serializable {


    private static final long serialVersionUID = 8406400228376070953L;

    private Integer id;

    private String username;

    private String password;

}
