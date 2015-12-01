package com.example.iamsimakov.myproject;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Alex on 02.12.2015.
 */
public class News {

    private String imgUrl;
    private String desc;

    public News(JSONObject widget) throws JSONException {
        JSONObject img = widget.getJSONObject("img");
        imgUrl = img.getString("url");
        desc = widget.getString("desc");
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDesc() {
        return desc;
    }
}
