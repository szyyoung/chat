package com.example.restful.service;

import com.alibaba.fastjson.JSONObject;
import com.example.common.dto.Response;
import com.example.common.dto.request.LoginDto;
import com.example.common.dto.request.RegisterDto;
import com.example.restful.dao.UserDao;
import com.example.restful.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.common.constants.Constants.*;


@Service
public class UserService {

    @Resource
    private UserDao userDao;

    @Autowired
    private RedisTemplate redisTemplate;


    public Response register(RegisterDto registerDto, HttpServletRequest httpServletRequest) {
        User user = new User();
        user.setAccount(registerDto.getAccount());
        user.setPassword(registerDto.getPassword());
        user.setNickName(registerDto.getNickName());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userDao.save(user);
        return Response.ok();
    }

    public Response login(LoginDto loginDto, HttpServletRequest httpServletRequest) {
        Optional<User> userOptional = userDao.findByAccountAndPassword(loginDto.getAccount(), loginDto.getPassword());
        if (!userOptional.isPresent()) {
            return Response.error("1", "未查询到用户");
        }
        String remoteAddr = httpServletRequest.getRemoteAddr();
        User user = userOptional.get();
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(remoteAddr);
        String token = UUID.randomUUID().toString();
        String key = USER_TOKEN_KEY + token;
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(user), KEY_EXPIRE_7_DAY, TimeUnit.DAYS);
        return Response.ok(token);
    }


    public Response select(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        Object o = redisTemplate.opsForValue().get(USER_TOKEN_KEY + token);
        if (null != o) {

            return null;
        } else {
            return Response.error("1", "无效token");
        }
    }
}
