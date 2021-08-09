package com.crazypig.oh.http.core.mvc;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface RequestHandler {

    ApiModel handle(ApiRequest req, RequestHandle handle) throws Exception;

}
