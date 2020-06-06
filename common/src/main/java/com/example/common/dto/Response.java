package com.example.common.dto;

import lombok.Data;

@Data
public class Response {

    public static final String SUCCESS = "0";
    public static final String SUCCESS_MSG = "";

    private String code;

    private String msg;

    private Object data;

    public Response() {
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Response(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Response ok() {
        return new Response(SUCCESS, SUCCESS_MSG);
    }

    public static Response ok(String data) {
        return new Response(SUCCESS, SUCCESS_MSG,data);
    }

    public static Response error(String code,String errorInfo) {
        return new Response(code, errorInfo);
    }


}
