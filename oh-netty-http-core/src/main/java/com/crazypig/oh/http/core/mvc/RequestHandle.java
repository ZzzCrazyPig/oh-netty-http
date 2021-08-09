package com.crazypig.oh.http.core.mvc;

import java.lang.reflect.Method;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface RequestHandle {

    /**
     * 请求对应的目标对象
     * @return
     */
    Object target();

    /**
     * 请求对应的目标方法
     * @return
     */
    Method targetMethod();

}
