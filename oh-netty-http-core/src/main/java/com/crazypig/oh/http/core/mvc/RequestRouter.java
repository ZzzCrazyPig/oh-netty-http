package com.crazypig.oh.http.core.mvc;

import com.crazypig.oh.http.core.exception.NoRouteException;

import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface RequestRouter {

    /**
     * 对应的路径
     * @return
     */
    String path();

    /**
     * 请求路由, 路由到对应的RouteChain进行处理
     * @param request
     * @param requestHandleMap
     * @return
     * @throws NoRouteException
     */
    RequestChain route(ApiRequest request, Map<String, RequestHandle> requestHandleMap) throws NoRouteException;

}
