package com.example.czxtks.Models.DirectoryManagement.result;

public class Result<T> {

//      200: 请求处理成功
//      201: 请求处理失败
//      401: 服务器内部错误
//      403:未认证（签名错误)
//      404:接口不存在


    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
