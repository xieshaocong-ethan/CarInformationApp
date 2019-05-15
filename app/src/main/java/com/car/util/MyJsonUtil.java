package com.car.util;


import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

public class MyJsonUtil {
    public static com.alibaba.fastjson.JSONObject StringToJson(String str){
        com.alibaba.fastjson.JSONObject json = JSON.parseObject(str);
        return json;
    }

    public static com.alibaba.fastjson.JSONObject VolleyJsonToAlibabaJson(JSONObject jsonObject){
        String str = jsonObject.toString();
        com.alibaba.fastjson.JSONObject json = JSON.parseObject(str);
        return json;
    }

}
