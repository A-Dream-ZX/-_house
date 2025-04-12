package com.house.exception;

import com.house.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理器
 * 
 * 统一捕获和处理系统中抛出的异常，将异常转换为标准格式的响应返回给客户端。
 * 使用Spring的@ControllerAdvice机制，可以捕获所有控制器中抛出的异常，
 * 避免了在每个控制器方法中都编写try-catch块的冗余代码。
 */
@ControllerAdvice("com.house.controller")
public class GlobalExceptionHandler {
    
    /**
     * 日志记录器
     * 用于记录异常信息到日志系统
     */
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 处理普通异常
     * 捕获控制器中抛出的所有未分类异常，并转换为统一的错误响应
     * 
     * @param e 捕获到的异常对象
     * @return 包含通用错误信息的Result对象
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody // 将result对象转换成JSON格式返回
    public Result error(Exception e) {
        // 记录异常日志，包含异常消息和堆栈跟踪
        log.error("系统异常: " + e.getMessage(), e);
        // 返回通用错误响应，不暴露详细的异常信息给客户端
        return Result.error("系统异常");
    }

    /**
     * 处理自定义业务异常
     * 捕获业务逻辑中抛出的CustomerException，保留其错误代码和消息
     * 
     * @param e 捕获到的自定义异常对象
     * @return 包含详细错误代码和消息的Result对象
     */
    @ExceptionHandler(CustomerException.class)
    @ResponseBody // 将result对象转换成JSON格式返回
    public Result CustomerError(CustomerException e) {
        // 记录业务异常日志，包含错误代码和信息
        log.error("业务异常: 代码={}, 信息={}", e.getCode(), e.getMsg(), e);
        // 返回带有具体错误代码和信息的响应
        return Result.error(e.getCode(), e.getMsg());
    }
}
