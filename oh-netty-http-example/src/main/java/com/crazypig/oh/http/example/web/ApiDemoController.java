package com.crazypig.oh.http.example.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.crazypig.oh.http.core.annotation.JsonApi;
import com.crazypig.oh.http.core.annotation.JsonParam;
import lombok.Data;
import org.springframework.stereotype.Controller;

@Controller
@JsonApi("/demo")
public class ApiDemoController {

    @JsonApi("/index")
    public Object index(@JsonParam IndexParam param) {
        return "hello netty http : " + JSON.toJSONString(param);
    }

    @JsonApi("/index2")
    public JSONObject index2() {
        JSONObject obj = new JSONObject();
        obj.put("k1", "hello");
        return obj;
    }

    @JsonApi("/index3")
    public void index3() {

    }

    @JsonApi("/success")
    public WebResult webResultSuccess(@JsonParam IndexParam indexParam) {
        return WebResult.success(indexParam);
    }

    @JsonApi("/fail")
    public WebResult webResultFail(@JsonParam IndexParam indexParam) {
        return WebResult.fail("Error", indexParam);
    }

    @Data
    public static class IndexParam {

        private String a;

        private String b;

    }

    @Data
    public static class WebResult<T> {

        private boolean success;

        private String message;

        private T data;

        public static <T> WebResult<T> success(T data) {
            WebResult webResult = new WebResult();
            webResult.setData(data);
            webResult.setSuccess(true);
            return webResult;
        }

        public static <T> WebResult<T> fail(String message, T data) {
            WebResult webResult = new WebResult();
            webResult.setData(data);
            webResult.setSuccess(false);
            webResult.setMessage(message);
            return webResult;
        }

    }

}
