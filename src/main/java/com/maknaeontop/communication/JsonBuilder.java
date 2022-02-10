package com.maknaeontop.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonBuilder {
    Gson gson = new Gson();

    public JsonBuilder(){}

    public String statusResponse(String status, String message){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("message", message);

        return gson.toJson(jsonObject);
    }

    public String tokenResponse(String status, String message, String token){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("token", token);

        return gson.toJson(jsonObject);
    }

    public String locationResponse(float x, float y, int floor, String uuid){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", x);
        jsonObject.addProperty("y", y);
        jsonObject.addProperty("floor", floor);
        jsonObject.addProperty("uuid", uuid);

        return gson.toJson(jsonObject);
    }
}
