package com.crazypig.oh.http.example.web.interceptor;

import com.crazypig.oh.http.core.exception.InterceptorException;
import com.crazypig.oh.http.core.mvc.ApiRequest;
import com.crazypig.oh.http.core.mvc.ApiModel;
import com.crazypig.oh.http.core.mvc.RequestHandle;
import com.crazypig.oh.http.core.mvc.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
@Slf4j
public class LogRequestInterceptor implements RequestInterceptor {

    @Override
    public boolean preHandle(ApiRequest request, RequestHandle requestHandle) throws InterceptorException {
        log.info("preHandle : {}", request.uri());
        return true;
    }

    @Override
    public boolean afterHandle(ApiRequest request, RequestHandle requestHandle, ApiModel response) throws InterceptorException {
        log.info("afterHandle : {}", request.uri());
        return true;
    }

    @Override
    public void exceptionCaught(Throwable e) {
        log.error("exceptionCaught", e);
    }
}
