/**
 *
 */
package com.itee.exam.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

/**
 * 实体和json对象转换工具
 *
 * @author xin
 */
public final class JsonHelper {

    /**
     *
     */
    private JsonHelper() {
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Class<T> clazz, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, TypeToken<T> token) {
        return new Gson().fromJson(json, token.getType());
    }

    public static <T> T fromJson(String json, TypeToken<T> token, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.fromJson(json, token.getType());
    }

    public static Map<String, String> fromJson(String json) {
        return new Gson().fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public static String toJson(Object obj) {
        return new Gson().toJson(obj);
    }

    public static String toJson(Object obj, String dateFormat) {
        Gson gson = new GsonBuilder().setDateFormat(dateFormat).create();
        return gson.toJson(obj);
    }

}