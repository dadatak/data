package com.prilaga.data.serialization;

import org.json.JSONObject;

/**
 * Created by Oleg Tarashkevich on 17.07.16.
 */
public abstract class JsonAble extends JsonAbleExtension {

    public JsonAble() {
    }

    public JsonAble(JSONObject jsonObject) throws Throwable {
        deserialize(jsonObject);
    }

    public abstract JSONObject serialize() throws Throwable;

    public abstract void deserialize(JSONObject jsonObject) throws Throwable;

    protected void enableDefault() {

    }

}
