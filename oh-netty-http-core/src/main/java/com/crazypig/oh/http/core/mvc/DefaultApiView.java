package com.crazypig.oh.http.core.mvc;

import com.alibaba.fastjson.JSON;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class DefaultApiView implements ApiView {

    private Map<String, Object> viewData;

    public DefaultApiView(Map<String, Object> viewData) {
        this.viewData = viewData;
    }

    @Override
    public Map<String, Object> viewData() {
        return this.viewData;
    }

    @Override
    public void writeTo(ApiResponse response) {
        response.setContentType(HttpHeaderValues.APPLICATION_JSON.toString());
        response.setContent(dataAsBytes(), charset());
        response.writeAndFlush();
    }

    protected byte[] dataAsBytes() {
        byte[] bytes = JSON.toJSONBytes(viewData);
        return bytes;
    }

    protected Charset charset() {
        return CharsetUtil.UTF_8;
    }

}
