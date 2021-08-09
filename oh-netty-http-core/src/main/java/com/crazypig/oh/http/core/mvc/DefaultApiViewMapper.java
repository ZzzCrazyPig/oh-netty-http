package com.crazypig.oh.http.core.mvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class DefaultApiViewMapper implements ApiViewMapper {

    @Override
    public ApiView map(ApiModel model) {
        boolean isVoid = model.isVoid();
        ApiView view = null;
        if (isVoid) {
            view = new DefaultApiView(null);
        }
        else {
            Class dataType = model.dataType();
            if (dataType.isPrimitive()) {
                view = buildNotJsonApiView(model);
            }
            else {
                try {
                    String jsonStr = JSON.toJSONString(model.data());
                    JSONObject obj = JSON.parseObject(jsonStr);
                    view = new DefaultApiView(obj);
                }
                catch (JSONException e) {
                    view = buildNotJsonApiView(model);
                }
            }
        }
        return view;
    }

    private ApiView buildNotJsonApiView(ApiModel resp) {
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("rtnValue", resp.data());
        ApiView view = new DefaultApiView(dataMap);
        return view;
    }

}
