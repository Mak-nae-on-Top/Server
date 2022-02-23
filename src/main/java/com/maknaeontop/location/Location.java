package com.maknaeontop.location;

import com.maknaeontop.dto.Beacon;
import java.util.HashMap;
import java.util.List;

/**
 * Location is a class that obtains the user's location using the beacon list.
 * In this study, it is calculated by calling the trilateration class.
 */
public class Location {

    private Trilateration trilateration = Trilateration.getInstance();

    public static Location getInstance() {
        return Holder.instance;
    }

    public HashMap<String, Float> calculateUserLocation(List<Beacon> beaconList){
        return trilateration.calculateTrilateration(beaconList.get(0), beaconList.get(1), beaconList.get(2));
    }

    public HashMap<String, Float> calculateUserLocationWithModel(float a, float b, List<Beacon> beaconList){
        HashMap<String, Float> calculatedCoordinate = trilateration.calculateTrilateration(beaconList.get(0), beaconList.get(1), beaconList.get(2));
        return applyModel(a, b, calculatedCoordinate);
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

    public HashMap<String, Float> createModel(float x, float y, List<List<Beacon>> rangedBeacons){
        // xMean 값과 yMean값 두개를 이용하여 모델 만들기(측정 평균 값을 실제 값과 맞추기 위하여)
        // * f(x) = realX, x = x1, f(y) = realY, y = y1
        // 1. realX == ax1 + b , realY == ay1 + b
        // 2. realX-realY == a(x1 - y1)
        // 3. a == (realX - realY)/(x1 - y1)
        // 4. b == realX - ax1
        // 5. f(x) == (realX - realY)/(x1 - y1)*x + realX - ax1

        HashMap<String,Float> hashMap = new HashMap<>();

        float[] meanXY = calculateMeanXY(rangedBeacons);

        float x1 = meanXY[0];
        float y1 = meanXY[1];

        float a  = (x - y)/(x1 - y1);
        hashMap.put("a", a);
        hashMap.put("b", x - a * x1);

        return hashMap;
    }

    private HashMap<String, Float> applyModel(float a, float b, HashMap<String, Float> coordinate){
        HashMap<String, Float> fixedCoordinate = new HashMap<>();
        fixedCoordinate.put("x", coordinate.get("x") * a + b);
        fixedCoordinate.put("y", coordinate.get("y") * a + b);
        return fixedCoordinate;
    }

    private Location(){}

    // LazyHolder
    private static class Holder{
        private static final Location instance = new Location();
    }

}
