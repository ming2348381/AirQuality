package com.example.airquality.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonHelper {
    private final static GsonBuilder sGsonBuilder = getDefaultBuilder();

    private static Gson createGson() {
        return sGsonBuilder.create();
    }

    public static GsonBuilder getDefaultBuilder() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    }

    public static <T> T fromJson(String strJson, Class<T> mObjClass) {
        return createGson().fromJson(strJson, mObjClass);
    }
}
