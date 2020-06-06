package com.example.client.util;

import lombok.Data;

@Data
public class ServerResponse {

    public static final String SUCCESS = "0";
    public static final String SUCCESS_MSG = "";

    private String code;

    private String msg;

    private Object data;

    public ServerResponse() {
    }

    public ServerResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ServerResponse(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return SUCCESS.equals(this.getCode());
    }

    public static ServerResponse ok() {
        return new ServerResponse(SUCCESS, SUCCESS_MSG);
    }

    public static ServerResponse ok(String data) {
        return new ServerResponse(SUCCESS, SUCCESS_MSG, data);
    }

    public static ServerResponse error(String code, String errorInfo) {
        return new ServerResponse(code, errorInfo);
    }


}