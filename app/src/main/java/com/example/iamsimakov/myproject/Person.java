package com.example.iamsimakov.myproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alex on 02.12.2015.
 */
public class Person {

    private String imgUrl = "";
    private String imgDesc = "";
    private String desc;

    public Person(JSONObject widget) throws JSONException {
        JSONObject content = widget.getJSONObject("content");
        JSONArray person = content.getJSONArray("person");
        JSONObject img = person.getJSONObject(0).getJSONObject("img");

        if (img.has("url")) imgUrl = img.getString("url");
        if (person.getJSONObject(0).has("text")) imgDesc = person.getJSONObject(0).getString("text");
        if (content.has("text")) desc = content.getString("text");

    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public String getDesc() {
        return desc;
    }
}
