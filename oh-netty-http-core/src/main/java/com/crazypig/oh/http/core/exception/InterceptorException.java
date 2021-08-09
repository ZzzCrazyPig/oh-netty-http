package com.crazypig.oh.http.core.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class InterceptorException extends HttpException {

    public InterceptorException(String message) {
        super(HttpResponseStatus.FORBIDDEN, message);
    }

    public InterceptorException(String message, Throwable cause) {
        super(HttpResponseStatus.FORBIDDEN, message, cause);
    }

}
