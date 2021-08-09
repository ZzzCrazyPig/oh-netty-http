package com.crazypig.oh.http.core.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class HttpException extends RuntimeException {

    private HttpResponseStatus status;

    public HttpException(HttpResponseStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpException(HttpResponseStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getCode() {
        return this.status.code();
    }

}
