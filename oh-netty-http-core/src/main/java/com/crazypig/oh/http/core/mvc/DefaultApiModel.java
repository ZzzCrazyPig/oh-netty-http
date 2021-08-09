package com.crazypig.oh.http.core.mvc;

/**
 * @author chenjianxin
 * @Description
 * @create 2021-07-24
 */
public class DefaultApiModel implements ApiModel {

    private Object data;

    private Class dataType;

    public DefaultApiModel(Object data, Class dataType) {
        this.data = data;
        this.dataType = dataType;
    }

    @Override
    public Object data() {
        return this.data;
    }

    @Override
    public Class<?> dataType() {
        return this.dataType;
    }

    @Override
    public boolean isVoid() {
        return this.dataType == Void.class;
    }
}
