package com.example.client.util;

import java.util.concurrent.ConcurrentHashMap;

public class Constant {

    public static final ConcurrentHashMap<String,Object> HOLDER = new ConcurrentHashMap<>();
    public static final String TOKEN = "token";
    public static final String SRDR = "srvdr";

    public static final String LOGIN_URL = "http://localhost:8083/user/login";
    public static final String REGISTER_URL = "http://localhost:8083/user/register";
    public static final String SELECT_URL = "http://localhost:8083/user/select";




}
