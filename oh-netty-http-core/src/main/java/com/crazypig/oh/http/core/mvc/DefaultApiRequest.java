package com.crazypig.oh.http.core.mvc;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class DefaultApiRequest implements ApiRequest {

    private FullHttpRequest httpRequest;

    private Map<String, String> queryParams = new LinkedHashMap<>();

    private String path;

    private String contentAsString;

    public DefaultApiRequest(FullHttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        parseRequest();
    }

    void parseRequest() {

        String uri = this.httpRequest.uri();
        int idx = uri.indexOf("?");
        if (idx > 0) {
            this.path = uri.substring(0, idx);
        }
        else {
            this.path = uri;
        }

        if (idx > 0) {
            String queryParamStr = uri.substring(idx + 1);
            if (!queryParamStr.isEmpty()) {
                String[] queryParamParts = queryParamStr.split("&");
                for (String queryParamPart : queryParamParts) {
                    String[] pair = queryParamPart.split("=", 2);
                    queryParams.put(pair[0], pair[1]);
                }
            }
        }

        Charset charset = HttpUtil.getCharset(this.httpRequest, CharsetUtil.UTF_8);
        ByteBuf contentByteBuf = this.httpRequest.content();
        this.contentAsString = contentByteBuf.toString(charset);
    }

    @Override
    public String uri() {
        return this.httpRequest.uri();
    }

    @Override
    public String path() {
        return this.path;
    }

    @Override
    public Map<String, String> queryParams() {
        return Collections.unmodifiableMap(this.queryParams);
    }

    @Override
    public HttpMethod method() {
        return this.httpRequest.method();
    }

    @Override
    public HttpHeaders headers() {
        return this.httpRequest.headers();
    }

    @Override
    public String contentAsString() {
        return this.contentAsString;
    }
}
