package com.crazypig.oh.http.core.exception;

import com.crazypig.oh.http.core.mvc.RequestHandle;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Getter;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class RequestChainExecuteException extends HttpException {

    @Getter
    private RequestHandle requestHandle;

    public RequestChainExecuteException(RequestHandle requestHandle, Throwable cause) {
        super(HttpResponseStatus.INTERNAL_SERVER_ERROR, cause.getMessage(), cause);
        this.requestHandle = requestHandle;
    }

}
