package com.scene.common.exception;

/**
 * 资源不存在异常
 */
public class NotFoundException extends CommonException {

    public NotFoundException(String message) {
        super(message, 404);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause, 404);
    }

    public NotFoundException(Throwable cause) {
        super(cause, 404);
    }
}

