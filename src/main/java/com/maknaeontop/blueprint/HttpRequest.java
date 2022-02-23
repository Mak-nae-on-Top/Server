package com.maknaeontop.blueprint;

import sun.net.www.protocol.http.HttpURLConnection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpRequest {
    private static final String BASE_URL = "http://18.224.181.65:5000/";

    /**
     * Method to request http for find route.
     *
     * @param userList  the users location list
     * @param roomList  the rooms location list
     * @param path      the file path
     * @return          the coordinate of route
     */
    public List<HashMap<String, Float>> findRoute(String userList, String roomList, String path){
        List<String> paramList = new ArrayList<>();
        paramList.add("ul="+userList);
        paramList.add("rl="+roomList);
        paramList.add("path="+path);
        String param = paramBuilder(paramList);
        String response = httpRequest(BASE_URL+"findRoute?"+param);
        return stringToArray(response);

    }

    /**
     * Method to http request for convert image to map.
     *
     * @param path      the file path
     * @return          true if there is no error
     */
    public String convertImageToMap(String path){
        return httpRequest(BASE_URL+"convertImageToMap?"+path);
    }

    /**
     * Method to convert string to array.
     *
     * @param targetString  the target string
     * @return              converted array
     */
    private List<HashMap<String, Float>> stringToArray(String targetString){
        List<HashMap<String, Float>> newList = new ArrayList<>();
        String replacedString = targetString.replaceAll("(\r\n|\r|\n|\n\r|\\p{Z}|\\t|\t)", "");
        String subString = replacedString.substring(1,replacedString.length()-1);
        String[] temp = subString.split(",");
        for(String coordinate: temp){
            HashMap<String, Float> hashMap = new HashMap<>();
            String[] xy = coordinate.split(":");
            Float x = Float.parseFloat(xy[0].substring(1,xy[0].length()-1));
            Float y = Float.parseFloat(xy[1].substring(1,xy[1].length()-1));
            hashMap.put("x", x);
            hashMap.put("y", y);
            newList.add(hashMap);
        }
        return newList;
    }

    /**
     * Method to build param string.
     *
     * @param params    the list of params
     * @return          the built param string
     */
    private String paramBuilder(List<String> params){
        StringBuilder sb = new StringBuilder();
        sb.append(params.get(0));
        for(int i=1; i<params.size();i++){
            sb.append("&"+params.get(i));
        }

        return sb.toString();
    }

    /**
     * Method to request other server.
     *
     * @param targetUrl     the target url
     * @return              the response
     */
    public static String httpRequest(String targetUrl) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setUseCaches(false);// 캐싱데이터를 받을지 말지 세팅합니다.
            connection.setDoOutput(true); // 쓰기모드를 지정할지 세팅합니다.

            int responseCode = connection.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println("HTTP 응답 코드 : " + responseCode);
            System.out.println("HTTP body : " + response.toString());

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
