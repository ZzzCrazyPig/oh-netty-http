package com.crazypig.oh.http.core.mvc;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface RequestDispatcher {

    /**
     * api请求分发
     * @param req
     * @return
     */
    ApiView dispatch(ApiRequest req);

    /**
     * 注册router
     * @param path
     * @param reqRouter
     */
    void registerRouter(String path, RequestRouter reqRouter);

    /**
     * 注册api视图渲染器
     * @param apiViewMapper
     */
    void registerApiViewMapper(ApiViewMapper apiViewMapper);

}
