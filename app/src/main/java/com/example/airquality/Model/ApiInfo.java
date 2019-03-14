package com.example.airquality.Model;

import java.io.Serializable;
import java.util.Map;

public class ApiInfo<T> implements Serializable {
    private Class<T> mResponseClass;
    private Map<String, String> mParameter;

    public ApiInfo(Class<T> responseClass, Map<String, String> parameter) {
        mResponseClass = responseClass;
        mParameter = parameter;
    }

    public ApiInfo(Class<T> responseClass) {
        mResponseClass = responseClass;
    }

    public Class<T> getResponseClass() {
        return mResponseClass;
    }

    public Map<String, String> getParameter() {
        return mParameter;
    }

    @Override
    public String toString() {
        return "ApiInfo{" +
                "mResponseClass=" + mResponseClass +
                ", mParameter=" + mParameter +
                '}';
    }
}
