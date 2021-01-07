package com.example.info;


import lombok.Data;

/**
 * 返回消息类
 */
@Data
public class R {

    private int code;//消息码
    private String msg;//消息值
    private Object data;//数据

    R(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功，无数据
     *
     * @return
     */
    public static R success() {
        return new R(0, "", null);
    }

    /**
     * 成功，有数据
     *
     * @param data 数据
     * @return
     */
    public static R success(Object data) {
        return new R(0, "", data);
    }

    /**
     * 错误，无数据
     *
     * @param msg 错误描述
     * @return
     */
    public static R error(String msg) {
        return new R(-1, msg, null);
    }

    /**
     * 错误，有数据
     *
     * @param msg  错误描述
     * @param data 数据
     * @return
     */
    public static R error(String msg, Object data) {
        return new R(-1, msg, data);
    }
}
