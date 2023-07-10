package com.chestnut.haoweilai.exception;

import com.chestnut.haoweilai.common.R;
import com.chestnut.haoweilai.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    /**
     * 全局异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String exMessage = ex.getMessage();
        log.error(exMessage);
        if (exMessage.contains("Duplicate entry")) {
            return R.error("操作失败，" + exMessage.split(" ")[2] + "已存在");
        }
        return R.error("操作失败，未知错误");
    }

    /**
     * 业务异常处理方法
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> customExceptionHandler(CustomException ex) {
        String exMessage = ex.getMessage();
        log.error(exMessage);
        return R.error(exMessage);
    }
}
