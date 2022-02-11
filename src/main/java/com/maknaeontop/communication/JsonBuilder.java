package com.maknaeontop.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maknaeontop.dto.Population;

import java.util.HashMap;

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

    public String locationResponse(Population population){
        return gson.toJson(population);
    }

    public String base64Response(String status, String base64){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("base64", base64);

        return gson.toJson(jsonObject);
    }
}
