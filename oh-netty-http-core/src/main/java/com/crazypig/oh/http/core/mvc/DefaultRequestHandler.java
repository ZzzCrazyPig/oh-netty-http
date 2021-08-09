package com.crazypig.oh.http.core.mvc;

import com.alibaba.fastjson.JSON;
import com.crazypig.oh.http.core.annotation.JsonParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class DefaultRequestHandler implements RequestHandler {

    @Override
    public ApiModel handle(ApiRequest req, RequestHandle handle) throws Exception {
        Object target = handle.target();
        Method targetMethod = handle.targetMethod();
        Object[] params = resolveParam(targetMethod, req);
        Class returnType = targetMethod.getReturnType();
        Object rtnValue =  targetMethod.invoke(target, params);
        ApiModel resp = new DefaultApiModel(rtnValue, returnType);
        return resp;
    }

    protected Object[] resolveParam(Method targetMethod, ApiRequest req) {

        Parameter[] parameters = targetMethod.getParameters();
        if (parameters.length <= 0) {
            return new Object[] {};
        }

        Object[] params = new Object[parameters.length];
        for (int i = 0; i < params.length; i++) {
            Parameter methodParameter = parameters[i];
            JsonParam jsonParam = methodParameter.getAnnotation(JsonParam.class);
            if (jsonParam != null) {
                Object param = JSON.parseObject(req.contentAsString(), methodParameter.getType());
                params[i] = param;
            }
            else {
                // TODO param binder
                params[i] = null;
            }
        }

        return params;
    }

}
