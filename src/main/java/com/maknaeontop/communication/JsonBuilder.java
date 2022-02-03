package com.maknaeontop.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonBuilder {
    Gson gson = new Gson();

    public JsonBuilder(){}

    public String joinResponse(String status, String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("message", message);

        return gson.toJson(jsonObject);
    }

    public String loginResponse(String status, String message, String token){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("token", token);

        return gson.toJson(jsonObject);
    }
}
