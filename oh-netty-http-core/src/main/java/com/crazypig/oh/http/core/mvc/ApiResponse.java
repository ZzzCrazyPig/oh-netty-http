package com.crazypig.oh.http.core.mvc;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-26
 */
public interface ApiResponse {

    void setContentType(String contentType);

    void addHeaders(Map<String, String> headers);

    void addHeader(String name, String value);

    void setContent(byte[] bytes, Charset charset);

    void writeAndFlush();

}
