package com.example.domain.chat;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Long id;

    private String account; //账号

    private String password; //密码

    private String nickName; //昵称

    private String photo; //头像

    private String email; //邮箱地址

    private LocalDateTime createTime; //创建时间

    private LocalDateTime updateTime; //更新时间

    private LocalDateTime lastLoginTime; //最近一次登录时间

}
