package com.crazypig.oh.http.core.mvc;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
@Slf4j
public class DefaultRequestExceptionHandler implements RequestExceptionHandler {

    @Override
    public ApiView handleException(ApiRequest req, RequestHandle handle, Throwable e) {
        log.error("", e);
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        obj.put("message", e.getMessage());
        DefaultApiView apiView = new DefaultApiView(obj);
        return apiView;
    }
}
