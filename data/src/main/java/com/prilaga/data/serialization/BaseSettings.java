package com.prilaga.data.serialization;

import android.text.TextUtils;

import com.prilaga.data.utils.DataUtil;

import org.json.JSONObject;

/**
 * Created by Oleg Tarashkevich on 04.06.16.
 */
public abstract class BaseSettings extends JsonAbleExtension {

    public BaseSettings() {
        init();
    }

    protected void init() {
        String json = load(classKey());
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                deserialize(jsonObject);
            } catch (Throwable e) {
                e.printStackTrace();
                remove();
            }
        } else
            loadDefault();
    }

    protected String classKey() {
        return null;
    }

    protected void serialize(JSONObject jsonObject) throws Throwable {
    }

    protected void deserialize(JSONObject jsonObject) throws Throwable {
    }

    protected void loadDefault() {

    }

    /**
     * Main methods
     */
    public void save() {
        JSONObject jsonObject = new JSONObject();
        try {
            serialize(jsonObject);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        saveJson(json);
    }

    protected void saveJson(String json) {
        if (!TextUtils.isEmpty(json) && !TextUtils.isEmpty(classKey()))
            DataUtil.getInstance().save(classKey(), json);
    }

    protected String load(String classKey) {
        return DataUtil.getInstance().load(classKey);
    }

    public final String createEncryptedJson() {
        String encryptedString = null;
        JSONObject jsonObject = new JSONObject();
        try {
            serialize(jsonObject);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        String json = jsonObject.toString();
        if (!TextUtils.isEmpty(json)) {
            String ndkKey = DataUtil.getInstance().getNdkKey();
            encryptedString = DataUtil.getInstance().getEncryption().encrypt(ndkKey, json);
        }
        return encryptedString;
    }

    public void reset() {

    }

    public void remove(){
        DataUtil.getInstance().getTinyDB().remove(classKey());
    }
}
