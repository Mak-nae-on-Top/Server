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

    public String locationResponse(float[] location){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("x", location[0]);
        jsonObject.addProperty("y", location[1]);
        jsonObject.addProperty("floor", (int) location[2]);

        return gson.toJson(jsonObject);
    }
}
