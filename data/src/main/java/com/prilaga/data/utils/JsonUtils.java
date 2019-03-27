package com.prilaga.data.utils;

import android.text.TextUtils;
import android.util.Log;

//import com.android.vending.billing.util.SkuDetails;
import com.prilaga.data.serialization.JsonAble;
import com.prilaga.data.serialization.JsonAbleList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Oleg Tarashkevich on 04.06.16.
 */
public final class JsonUtils {

    /**
     * Map
     */

    public static Map<String, Boolean> jsonToBoolMap(JSONObject jObject) throws JSONException {

        Map<String, Boolean> map = new HashMap<>();
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            boolean value = jObject.getBoolean(key);
            map.put(key, value);
        }

        return map;
    }

    public static Map<String, String> jsonToStringMap(JSONObject jObject) throws JSONException {

        Map<String, String> map = new HashMap<>();
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            map.put(key, value);
        }

        return map;
    }

    public static JSONObject mapBoolToJson(Map<String, Boolean> data) {
        JSONObject object = new JSONObject();

        if (data != null && data.size() > 0) {
            for (Map.Entry<String, Boolean> entry : data.entrySet()) {

                String key = entry.getKey();
                if (key != null) {
                    try {
                        object.put(key, entry.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return object;
    }

    public static JSONObject mapStringToJson(Map<String, String> data) {
        JSONObject object = new JSONObject();

        if (data != null && data.size() > 0) {
            for (Map.Entry<String, String> entry : data.entrySet()) {

                String key = entry.getKey();
                if (key != null) {
                    try {
                        object.put(key, entry.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return object;
    }

    /**
     * Set
     */

    public static JSONArray setStringToJsonArray(Set<String> set) {
        JSONArray jsonArray = new JSONArray();
        if (ListUtil.isNotEmpty(set)) {
            for (String favorite : set)
                jsonArray.put(favorite);
        }
        return jsonArray;
    }

    public static Set<String> jsonArrayToSetString(JSONArray jsonArray) throws Throwable {
        Set<String> set = new HashSet<>();
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                String favorite = jsonArray.getString(i);
                set.add(favorite);
            }
        }
        return set;
    }

    /**
     * TODO JsonAble object
     */
    /**
     * List AndroidId, ImageUrl
     */

    public static <T extends JsonAble> JsonAbleList<T> jsonArrayToList(JSONArray jsonArray, Class<T> clazz) throws Throwable {
        JsonAbleList<T> list = new JsonAbleList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            T serialization = (T) Class.forName(clazz.getName()).getConstructor().newInstance();
            serialization.deserialize(jsonObject);
            list.add(serialization);
        }
        return list;
    }

//    protected final <T extends Model> List<T> getMany(Class<T> type, String foreignKey) {
//        return (new Select()).from(type).where(Cache.getTableName(type) + "." + foreignKey + "=?", new Object[]{this.getId()}).execute();
//    }
//
//    public static <L> L decompressObject(String compressedString, Class<L> tClass) {
//        String decompressedString = DataUtil.decompress(compressedString);
//        L object = deserialize(decompressedString, tClass);
//        return object;
//    }

    public static JSONArray listToJsonArray(List<? extends JsonAble> list) throws Throwable {
        JSONArray jsonArray = new JSONArray();
        if (ListUtil.isNotEmpty(list)) {
            for (JsonAble serialization : list) {
                if (serialization != null) {
                    JSONObject jsonObject = serialization.serialize();
                    jsonArray.put(jsonObject);
                }
            }
        }
        return jsonArray;
    }

//    /**
//     * Map JsonAble
//     */
//
//    public static JSONObject jsonFromMap(Map<String, ? extends JsonAbleExtension> data) {
//        JSONObject object = new JSONObject();
//
//        if (data != null && data.size() > 0) {
//            for (Map.Entry<String, ? extends JsonAbleExtension> entry : data.entrySet()) {
//
//                String key = entry.getKey();
//                if (key != null) {
//                    try {
//                        JsonAbleExtension serialization = entry.getValue();
//                        object.put(key, serialization.serialize());
//                    } catch (Throwable throwable) {
//                        throwable.printStackTrace();
//                    }
//                }
//            }
//        }
//        return object;
//    }
//
//    public static <T extends JsonAbleExtension> Map<String, T> mapFromJson(JSONObject jObject, Class clazz) throws Throwable {
//
//        Map<String, T> map = new HashMap<>();
//        Iterator<?> keys = jObject.keys();
//
//        while (keys.hasNext()) {
//            String key = (String) keys.next();
//            String value = jObject.getString(key);
//            JSONObject jsonObject = new JSONObject(value);
//            T serialization = (T) Class.forName(clazz.getName()).getConstructor().newInstance();
//            serialization.deserialize(jsonObject);
//            map.put(key, serialization);
//        }
//
//        return map;
//    }

    /**
     * JsonAble objects
     */

    public static <T extends JsonAble> T fromJson(String json, Class<T> clazz) {
        T serialization = null;
        if (clazz != null)
            serialization = fromJson(json, clazz.getName());
        return serialization;
    }

    public static <T extends JsonAble> T fromJson(String json, String className) {
        T serialization = null;
        if (!TextUtils.isEmpty(json) && !TextUtils.isEmpty(className)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                serialization = (T) Class.forName(className).getConstructor().newInstance();
                serialization.deserialize(jsonObject);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return serialization;
    }

    public static <T extends JsonAble> T fromJson(JSONObject jsonObject, Class<T> clazz) {
        T serialization = null;
        if (jsonObject != null) {
            try {
                serialization = (T) Class.forName(clazz.getName()).getConstructor().newInstance();
                serialization.deserialize(jsonObject);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return serialization;
    }

    public static <T extends JsonAble> T emptyObject(Class<T> clazz) {
        T serialization = null;
        try {
            serialization = (T) Class.forName(clazz.getName()).getConstructor().newInstance();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return serialization;
    }

    public static String toJson(JsonAble serialization) {
        String json = null;
        if (serialization != null) {
            try {
                JSONObject jsonObject = serialization.serialize();
                json = jsonObject.toString();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    /**
     * List of Strings
     */

    public static List<String> fromJsonToStringList(String json) {
        List<String> list = new ArrayList<>();
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String text = jsonArray.getString(i);
                    list.add(text);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static String toJsonFromStringList(List<String> list) {
        String json = null;
        if (list != null) {
            try {
                JSONArray jsonArray = new JSONArray(list);
                json = jsonArray.toString();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return json;
    }

    public static List<Boolean> fromJsonToBoolList(String json) {
        List<Boolean> list = new ArrayList<>();
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    boolean text = jsonArray.optBoolean(i);
                    list.add(text);
                }
            } catch (Throwable throwable) {
                Log.e("AlarmJsonUtils", "can't create object", throwable);
            }
        }
        return list;
    }

    public static String toJsonFromBoolList(List<Boolean> list) {
        String json = null;
        if (list != null) {
            try {
                JSONArray jsonArray = new JSONArray(list);
                json = jsonArray.toString();
            } catch (Throwable throwable) {
                Log.e("AlarmJsonUtils", "can't create object", throwable);
            }
        }
        return json;
    }
}