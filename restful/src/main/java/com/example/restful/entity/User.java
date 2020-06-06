package com.example.restful.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Generated;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account; //账号

    private String password; //密码

    @Column(name = "nick_name")
    private String nickName; //昵称

    private String photo =  StringUtils.EMPTY; //头像

    private String email = StringUtils.EMPTY; //邮箱地址

    @Column(name = "create_time")
    private LocalDateTime createTime = LocalDateTime.MIN; //创建时间

    @Column(name = "update_time")
    private LocalDateTime updateTime = LocalDateTime.MIN; //更新时间

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime = LocalDateTime.MIN; //最近一次登录时间


    @Column(name = "last_login_ip")
    private String lastLoginIp = "127.0.0.1"; //最近一次登录时间

}
