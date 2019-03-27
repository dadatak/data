package com.prilaga.data.serialization;

import com.prilaga.data.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Oleg Tarashkevich on 18.07.16.
 */
public class JsonAbleExtension {

    private transient boolean mIsSerialized;

    protected String getString(JSONObject jsonObject, String name) throws JSONException {
        return getString(jsonObject, name, null);
    }

    protected String getString(JSONObject jsonObject, String name, String defaultValue) throws JSONException {
        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            setSerialized(true);
            return jsonObject.getString(name);
        } else
            return defaultValue;
    }

    protected int getInt(JSONObject jsonObject, String name) throws JSONException {
        return getInt(jsonObject, name, 0);
    }

    protected int getInt(JSONObject jsonObject, String name, int defaultValue) throws JSONException {
        if (jsonObject.has(name)) {
            setSerialized(true);
            return jsonObject.getInt(name);
        } else
            return defaultValue;
    }

    protected long getLong(JSONObject jsonObject, String name) throws JSONException {
        return getLong(jsonObject, name, 0);
    }

    protected long getLong(JSONObject jsonObject, String name, long defaultValue) throws JSONException {
        if (jsonObject.has(name)) {
            setSerialized(true);
            return jsonObject.getLong(name);
        } else
            return defaultValue;
    }

    protected boolean getBoolean(JSONObject jsonObject, String name) throws JSONException {
        return getBoolean(jsonObject, name, false);
    }

    protected boolean getBoolean(JSONObject jsonObject, String name, boolean defaultValue) throws JSONException {
        if (jsonObject.has(name)) {
            setSerialized(true);
            return jsonObject.getBoolean(name);
        } else
            return defaultValue;
    }

    protected <T extends JsonAble> JsonAbleList<T> getSerializeList(JSONObject jsonObject, String name, Class<T> clazz) throws Throwable {
        JsonAbleList<T> list = null;
        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            setSerialized(true);
            JSONArray jsonArray = jsonObject.getJSONArray(name);
            list = JsonUtils.jsonArrayToList(jsonArray, clazz);
        }
        return list;
    }

    protected <T extends JsonAble> T getSerializeObject(JSONObject jsonObject, String name, Class<T> clazz) throws JSONException {
        T t = null;
        if (jsonObject.has(name) && !jsonObject.isNull(name)) {
            setSerialized(true);
            JSONObject jObject = jsonObject.getJSONObject(name);
            t = JsonUtils.fromJson(jObject, clazz);
        }
        return t;
    }

    protected <T extends JsonAble> void putSerializeList(JSONObject jsonObject, String name, JsonAbleList<T> list) throws Throwable {
        JSONArray jsonArray = JsonUtils.listToJsonArray(list);
        jsonObject.put(name, jsonArray);
    }

    protected void setSerialized(boolean isSerialized) {
        mIsSerialized = isSerialized;
    }

    public boolean isSerialized() {
        return mIsSerialized;
    }

    public boolean isNotSerialized() {
        return !isSerialized();
    }
}
