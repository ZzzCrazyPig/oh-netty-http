package com.crazypig.oh.http.core.mvc;

import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public interface ApiView {

    Map<String, Object> viewData();

    void writeTo(ApiResponse response);

}
