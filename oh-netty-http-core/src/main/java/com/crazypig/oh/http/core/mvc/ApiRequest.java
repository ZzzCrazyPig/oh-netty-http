package com.crazypig.oh.http.core.mvc;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface ApiRequest {

    /**
     * http请求uri(包含path + queryParams)
     * @return
     */
    String uri();

    /**
     * http请求path部分, 不含queryParams
     * @return
     */
    String path();

    /**
     * http请求uri上的queryParams
     * @return
     */
    Map<String, String> queryParams();

    /**
     * http请求方法
     * @return
     */
    HttpMethod method();

    /**
     * http请求header
     * @return
     */
    HttpHeaders headers();

    /**
     * http请求内容
     * @param
     * @return
     */
    String contentAsString();

}
