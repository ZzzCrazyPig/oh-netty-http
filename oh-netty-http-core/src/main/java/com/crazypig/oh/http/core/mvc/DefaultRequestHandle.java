package com.crazypig.oh.http.core.mvc;

import java.lang.reflect.Method;

public class DefaultRequestHandle implements RequestHandle {

    /**
     * 被@JsonApi注解标记的当前bean
     */
    private Object target;

    /**
     * 被@JsonApi注解标记的当前bean的当前方法
     */
    private Method targetMethod;

    public DefaultRequestHandle(Object target, Method targetMethod) {
        this.target = target;
        this.targetMethod = targetMethod;
    }

    @Override
    public Object target() {
        return this.target;
    }

    @Override
    public Method targetMethod() {
        return this.targetMethod;
    }
}
