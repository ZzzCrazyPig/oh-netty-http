package com.crazypig.oh.http.core.mvc;

import com.crazypig.oh.http.core.exception.InterceptorException;
import com.crazypig.oh.http.core.exception.RequestChainExecuteException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class DefaultRequestChain implements RequestChain {

    private List<RequestInterceptor> interceptors = new ArrayList<>();

    private ApiRequest request;

    private RequestHandle requestHandle;

    private RequestHandler requestHandler;

    public DefaultRequestChain(ApiRequest req, RequestHandle requestHandle,
                               List<RequestInterceptor> interceptors) {
        this.request = req;
        this.requestHandle = requestHandle;
        this.interceptors.addAll(interceptors);
        this.requestHandler = new DefaultRequestHandler();
    }

    @Override
    public ApiModel execute() throws RequestChainExecuteException {
        ApiModel resp = null;

        // 拦截器preHandle
        interceptorPreHandle();

        // 执行
        try {
            resp = requestHandler.handle(request, requestHandle);
        }
        catch (Exception e) {
            interceptorOnError(e);
            throw new RequestChainExecuteException(this.requestHandle, e);
        }

        // 拦截器afterHandle
        interceptorAfterHandle(resp);

        return resp;
    }

    protected void interceptorOnError(Throwable e) {
        for (int i = interceptors.size() - 1; i >= 0; i++) {
            interceptors.get(i).exceptionCaught(e);
        }
    }

    protected void interceptorPreHandle() {
        try {
            boolean preHandleBreak = false;
            for (RequestInterceptor interceptor : interceptors) {
                boolean preHandle = interceptor.preHandle(request, requestHandle);
                if (!preHandle) {
                    preHandleBreak = true;
                    break;
                }
            }
            if (preHandleBreak) {
                throw new InterceptorException("Interceptor preHandle");
            }
        }
        catch (InterceptorException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InterceptorException("Interceptor preHandler Error", e);
        }
    }

    protected void interceptorAfterHandle(ApiModel resp) {
        try {
            boolean afterHandleBreak = false;
            for (int i = interceptors.size() - 1; i >= 0; i--) {
                boolean afterHandle = interceptors.get(i).afterHandle(request, requestHandle, resp);
                if (!afterHandle) {
                    afterHandleBreak = true;
                    break;
                }
            }
            if (afterHandleBreak) {
                throw new InterceptorException("Interceptor afterHandle");
            }
        }
        catch (InterceptorException e) {
            throw e;
        }
        catch (Exception e) {
            throw new InterceptorException("Interceptor afterHandle Error", e);
        }
    }

    @Override
    public RequestHandler handler() {
        return this.requestHandler;
    }

    @Override
    public List<RequestInterceptor> interceptors() {
        return Collections.unmodifiableList(this.interceptors);
    }
}
