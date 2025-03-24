package com.house.exception;

import com.house.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;



/**
 * 全局异常处理
 */
@ControllerAdvice("com.example.controller")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseBody //将result对象转换成 json 格式
    public Result error(Exception e){
        log.error("系统异常: " + e.getMessage(), e);
        return Result.error("系统异常");
    }

    @ExceptionHandler(CustomerException.class)
    @ResponseBody //将result对象转换成 json 格式
    public Result CustomerError(CustomerException e){
        log.error("业务异常: 代码={}, 信息={}", e.getCode(), e.getMsg(), e);
        return Result.error(e.getCode(), e.getMsg());
    }

}
