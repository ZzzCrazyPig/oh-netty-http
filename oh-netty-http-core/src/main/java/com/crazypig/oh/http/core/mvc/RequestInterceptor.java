package com.crazypig.oh.http.core.mvc;

import com.crazypig.oh.http.core.exception.InterceptorException;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface RequestInterceptor {

    boolean preHandle(ApiRequest request, RequestHandle requestHandle) throws InterceptorException;

    boolean afterHandle(ApiRequest request, RequestHandle requestHandle, ApiModel response) throws InterceptorException;

    void exceptionCaught(Throwable a);

}
