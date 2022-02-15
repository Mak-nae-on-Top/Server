package com.maknaeontop.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maknaeontop.dto.MapDto;
import com.maknaeontop.dto.Population;

import java.util.HashMap;
import java.util.List;

public class Response {
    Gson gson = new Gson();

    public Response(){}

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

    public String base64Response(String status, HashMap<String, Integer> heightAndWidth, String base64){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("image_height", heightAndWidth.get("image_height"));
        jsonObject.addProperty("image_width", heightAndWidth.get("image_width"));
        jsonObject.addProperty("blueprint_height", heightAndWidth.get("blueprint_height"));
        jsonObject.addProperty("blueprint_width", heightAndWidth.get("blueprint_width"));
        jsonObject.addProperty("base64", base64);

        return gson.toJson(jsonObject);
    }
}
