package com.crazypig.oh.http.core.mvc;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface RequestExceptionHandler {

    ApiView handleException(ApiRequest req, RequestHandle handle, Throwable e);

}
