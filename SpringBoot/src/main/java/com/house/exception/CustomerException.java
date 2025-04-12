package com.house.exception;

/**
 * 自定义异常类
 * 
 * 用于在应用程序中抛出包含特定错误代码和消息的异常，
 * 便于全局异常处理器进行统一的异常处理和响应格式化。
 * 
 * 异常包含错误代码和错误消息两个主要属性，可用于向客户端
 * 提供更具体的错误信息。
 */
public class CustomerException extends RuntimeException {
    
    /**
     * 错误代码
     * 用于标识不同类型的错误，如400表示请求错误，500表示服务器错误等
     */
    private String code;
    
    /**
     * 错误消息
     * 对错误的详细描述，提供给前端显示或日志记录
     */
    private String msg;

    /**
     * 带有错误代码和错误消息的构造函数
     * 
     * @param code 错误代码
     * @param msg 错误消息
     */
    public CustomerException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public CustomerException(String msg) {
        super(msg);
        this.code = "500";
        this.msg = msg;
    }

    public CustomerException() {
        super();
    }

    /**
     * 获取错误代码
     * 
     * @return 当前异常的错误代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置错误代码
     * 
     * @param code 新的错误代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取错误消息
     * 
     * @return 当前异常的错误消息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置错误消息并同时设置异常原因
     * 
     * @param msg 新的错误消息
     * @param cause 导致此异常的原始异常
     */
    public void setMsg(String msg, Throwable cause) {
        this.msg = msg;
        initCause(cause);
    }

    /**
     * 设置错误消息
     * 
     * @param msg 新的错误消息
     */
    public void setMsg(String msg) {
        this.msg = msg;
        super.initCause(new RuntimeException(msg));
    }
    
    /**
     * 返回异常的字符串表示形式
     * 
     * @return 格式化的异常信息，包含错误代码和消息
     */
    @Override
    public String toString() {
        return "CustomerException{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
