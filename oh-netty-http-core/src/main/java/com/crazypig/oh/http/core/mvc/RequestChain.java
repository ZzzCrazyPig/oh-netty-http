package com.crazypig.oh.http.core.mvc;

import com.crazypig.oh.http.core.exception.RequestChainExecuteException;

import java.util.List;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface RequestChain {

    /**
     * 执行对应的http请求所对应的chain
     * @return
     * @throws RequestChainExecuteException
     */
    ApiModel execute() throws RequestChainExecuteException;

    /**
     * 最终处理请求的handler
     * @return
     */
    RequestHandler handler();

    /**
     * 请求拦截器
     * @return
     */
    List<RequestInterceptor> interceptors();

}
