package com.house.common;

/**
 * 统一响应结果类
 * 
 * 用于封装API接口的响应数据，提供统一的响应格式
 * 包含状态码、消息和数据三部分，便于前端处理
 */
public class Result {

    /**
     * 状态码
     * 成功："200"
     * 错误：自定义错误码，通常为负数或特定错误代码
     */
    private String code;
    
    /**
     * 响应消息
     * 成功时通常为"成功"
     * 错误时为具体错误信息
     */
    private String msg;
    
    /**
     * 响应数据
     * 成功时包含实际返回的数据对象
     * 错误时通常为null
     */
    private Object data;

    /**
     * 私有构造方法
     * 防止直接实例化，统一使用静态工厂方法创建实例
     */
    private Result() {
    }

    /**
     * 创建成功响应（无数据）
     * 
     * @return 包含默认成功码和消息的Result对象
     */
    public static Result success() {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }

    /**
     * 创建成功响应（带数据）
     * 
     * @param data 要返回的数据对象
     * @return 包含成功码、消息和数据的Result对象
     */
    public static Result success(Object data) {
        Result result = new Result();
        result.setCode("200");
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    /**
     * 创建错误响应（自定义错误消息）
     * 
     * @param msg 错误消息
     * @return 包含默认错误码和自定义错误消息的Result对象
     */
    public static Result error(String msg) {
        Result result = new Result();
        result.setCode("-1");
        result.setMsg(msg);
        return result;
    }

    /**
     * 创建错误响应（自定义错误码和消息）
     * 
     * @param code 错误码
     * @param msg 错误消息
     * @return 包含自定义错误码和消息的Result对象
     */
    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    /**
     * 获取状态码
     * 
     * @return 状态码字符串
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置状态码
     * 
     * @param code 状态码字符串
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取响应消息
     * 
     * @return 消息字符串
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置响应消息
     * 
     * @param msg 消息字符串
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 获取响应数据
     * 
     * @return 数据对象
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置响应数据
     * 
     * @param data 数据对象
     */
    public void setData(Object data) {
        this.data = data;
    }
}
