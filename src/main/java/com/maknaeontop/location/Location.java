package com.maknaeontop.location;

import com.maknaeontop.dto.Beacon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Location {
    private Trilateration trilateration = Trilateration.getInstance();

    public static Location getInstance() {
        return Holder.instance;
    }

    public HashMap<String, Float> calculateUserLocation(List<Beacon> beaconList){
        return trilateration.calculateTrilateration(beaconList.get(0), beaconList.get(1), beaconList.get(2));
    }

    public float[] calculateMeanXY(List<List<Beacon>> beaconList){
        float xData = 0, yData = 0;
        for(int i=0;i<beaconList.size(); i++){
            HashMap<String, Float> newData = calculateUserLocation(beaconList.get(i));
            xData += newData.get("x");
            yData += newData.get("y");
        }
        return new float[]{xData / beaconList.size(), yData / beaconList.size()};
    }

    public HashMap<String, Float> createModel(List<List<Beacon>> beaconList, float realX, float realY){
        // xMean 값과 yMean값 두개를 이용하여 모델 만들기(측정 평균 값을 실제 값과 맞추기 위하여)
        // * f(x) = realX, x = x1, f(y) = realY, y = y1
        // 1. realX == ax1 + b , realY == ay1 + b
        // 2. realX-realY == a(x1 - y1)
        // 3. a == (realX - realY)/(x1 - y1)
        // 4. b == realX - ax1
        // 5. f(x) == (realX - realY)/(x1 - y1)*x + realX - ax1

        float[] meanXY = calculateMeanXY(beaconList);

        float x1 = meanXY[0];
        float y1 = meanXY[1];

        float a = (realX - realY)/(x1 - y1);
        float b  = realX - a * x1;

        return new HashMap<String,Float>(){{
            put("a",a);
            put("b",b);
        }};
    }

    public HashMap<String, Float> applyModel(float a, float b, HashMap<String, Float> coordinate){
        return null;
    }

    private Location(){}

    // LazyHolder
    private static class Holder{
        private static final Location instance = new Location();
    }

}
