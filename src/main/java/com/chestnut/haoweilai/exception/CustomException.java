package com.chestnut.haoweilai.exception;

/**
 * 业务异常类
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
