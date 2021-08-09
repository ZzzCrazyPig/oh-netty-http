package com.crazypig.oh.http.core.mvc;

import com.crazypig.oh.http.core.exception.NoRouteException;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class DefaultRequestRouter implements RequestRouter {

    @Setter
    private String path;

    @Setter
    private List<RequestInterceptor> interceptors = new ArrayList<>();

    @Override
    public String path() {
        return this.path;
    }

    @Override
    public RequestChain route(ApiRequest request, Map<String, RequestHandle> requestHandleMap) throws NoRouteException {

        // 根据 request 找到对应的 request handle
        String path = request.path();
        RequestHandle requestHandle = requestHandleMap.get(path);
        if (requestHandle == null) {
            throw new NoRouteException("RequestHandle Not Found");
        }
        // 组装RequestChain
        RequestChain reqChain = new DefaultRequestChain(request, requestHandle, interceptors);
        return reqChain;
    }

    protected List<RequestInterceptor> matchInterceptors(ApiRequest request) {
        // TODO interceptor excludes
        return this.interceptors;
    }

}
