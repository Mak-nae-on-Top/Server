package com.maknaeontop.location;

import com.maknaeontop.dto.Beacon;

import java.util.HashMap;
import java.util.Map;

public class Trilateration {
    /**
     * Method to calculate user coordinates through trilateration.
     *
     * @param beacon1   the beacon1 information which contains beacon's coordinate and distance
     * @param beacon2   the beacon2 information which contains beacon's coordinate and distance
     * @param beacon3   the beacon3 information which contains beacon's coordinate and distance
     * @return          the user coordinate
     */
    public HashMap<String, Float> calculateTrilateration(Beacon beacon1, Beacon beacon2, Beacon beacon3){
        float[] p1 = {beacon1.getX(), beacon1.getY()};
        float d1 = beacon1.getAccuracy();

        float[] p2 = {beacon2.getX(), beacon2.getY()};
        float d2 = beacon2.getAccuracy();

        float[] p3 = {beacon3.getX(), beacon3.getY()};
        float d3 = beacon3.getAccuracy();

        double[] ex   = new double[2];
        double[] ey   = new double[2];
        double[] differenceBetweenP1AndP3 = new double[2];
        double jval  = 0;
        double temp  = 0;
        double ival  = 0;
        double p3p1i = 0;
        double xval;
        double yval;
        double t1;
        double t2;
        double t3;
        double t;
        
        HashMap<String, Float> calculatedCoordinate = new HashMap<>();

        for (int i = 0; i < p1.length; i++) {
            t1 = p2[i];
            t2 = p1[i];
            t = t1 - t2;
            temp += (t*t);  // 비콘 p1과 p2 사이의 거리의 제곱
        }
        double distanceBetweenP1AndP2 = Math.sqrt(temp);    // 비콘 p1과 p2 사이의 거리
        for (int i = 0; i < p1.length; i++) {
            t1 = p2[i];
            t2 = p1[i];
            ex[i] = (t1 - t2)/distanceBetweenP1AndP2;    // p1과 p2를 꼭지점으로 하는 직각삼각형의 cos과 sin
        }
        for (int i = 0; i < p3.length; i++) {
            t1 = p3[i];
            t2 = p1[i];
            differenceBetweenP1AndP3[i] = t1 - t2;   // p3와 p1의 x,y좌표 차이
        }
        for (int i = 0; i < ex.length; i++) {
            t1 = ex[i];
            t2 = differenceBetweenP1AndP3[i];
            ival += (t1*t2);        // ival = p1p3x차이*cos + p1p3y차이*cos
        }
        for (int  i = 0; i < p3.length; i++) {
            t1 = p3[i];
            t2 = p1[i];
            t3 = ex[i] * ival;  // cos * (x차이*cos + y차이*cos)
            t  = t1 - t2 -t3;   // p3p1좌표차이 - cos * (x차이*cos + y차이*cos)
            p3p1i += (t*t);     // (p1p3x차이 - cos * (p1p3x차이*cos + p1p3y차이*cos))^2 + (p1p3y차이 - cos * (p1p3x차이*cos + p1p3y차이*cos))^2
        }
        for (int i = 0; i < p3.length; i++) {
            t1 = p3[i];
            t2 = p1[i];
            t3 = ex[i] * ival;  // cos * (x차이*cos + y차이*cos)
            ey[i] = (t1 - t2 - t3)/Math.sqrt(p3p1i);
        }
        for (int i = 0; i < ey.length; i++) {
            t1 = ey[i];
            t2 = differenceBetweenP1AndP3[i];
            jval += (t1*t2);
        }
        xval = (Math.pow(d1, 2) - Math.pow(d2, 2) + Math.pow(distanceBetweenP1AndP2, 2))/(2*distanceBetweenP1AndP2);
        yval = ((Math.pow(d1, 2) - Math.pow(d3, 2) + Math.pow(ival, 2) + Math.pow(jval, 2))/(2*jval)) - ((ival/jval)*xval);

        t1 = p1[0];
        t2 = ex[0] * xval;
        t3 = ey[0] * yval;
        calculatedCoordinate.put ("x", (float)(t1 + t2 + t3));

        t1 = p1[1];
        t2 = ex[1] * xval;
        t3 = ey[1] * yval;
        calculatedCoordinate.put("y", (float)(t1 + t2 + t3));

        return calculatedCoordinate;
    }

    public static Trilateration getInstance(){
        return Holder.instance;
    }

    private Trilateration(){}

    private static class Holder{
        private static final Trilateration instance = new Trilateration();
    }
}
