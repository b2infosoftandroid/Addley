package com.b2infosoft.addley.server;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    private String data;
    public JsonParser(String data) {
        this.data = data;
    }
    public JSONArray getJsonArray(){
        return null;
    }
    public JSONObject getJsonObject()  {
        try {
            return new JSONObject(data);
        }catch (JSONException je){
            je.printStackTrace();
            Log.e("JSON_ERROR",je.toString());
        }
        return null;
    }
}