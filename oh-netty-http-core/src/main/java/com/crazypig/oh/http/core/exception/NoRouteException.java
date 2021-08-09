package com.crazypig.oh.http.core.exception;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class NoRouteException extends HttpException {

    private static HttpResponseStatus thisStatus = HttpResponseStatus.NOT_FOUND;

    public NoRouteException(String message) {
        super(thisStatus, message);
    }

    public NoRouteException(String message, Throwable cause) {
        super(thisStatus, message);
    }
}
