package com.maknaeontop.location;

import com.maknaeontop.dto.Beacon;

import java.util.HashMap;
import java.util.Map;

public class Trilateration {
    public static Trilateration getInstance(){
        return Holder.instance;
    }

    public HashMap<String, Float> calculateTrilateration(Beacon beacon1, Beacon beacon2, Beacon beacon3){
        float[] P1 = {beacon1.getX(), beacon1.getY()};
        float d1 = beacon1.getAccuracy();

        float[] P2 = {beacon2.getX(), beacon2.getY()};
        float d2 = beacon2.getAccuracy();

        float[] P3 = {beacon3.getX(), beacon3.getY()};
        float d3 = beacon3.getAccuracy();

        double[] ex   = new double[2];
        double[] ey   = new double[2];
        double[] p3p1 = new double[2];
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
        double exx;
        double d;
        double eyy;

        for (int i = 0; i < P1.length; i++) {
            t1   = P2[i];
            t2   = P1[i];
            t    = t1 - t2;
            temp += (t*t);  // 비콘 p1과 p2 사이의 거리의 제곱
        }
        d = Math.sqrt(temp);    // 비콘 p1과 p2 사이의 거리
        for (int i = 0; i < P1.length; i++) {
            t1    = P2[i];
            t2    = P1[i];
            exx   = (t1 - t2)/(Math.sqrt(temp));
            ex[i] = exx;    // p1과 p2를 꼭지점으로 하는 직각삼각형의 cos과 sin
        }
        for (int i = 0; i < P3.length; i++) {
            t1      = P3[i];
            t2      = P1[i];
            t3      = t1 - t2;
            p3p1[i] = t3;   // p3와 p1의 x,y좌표 차이
        }
        for (int i = 0; i < ex.length; i++) {
            t1 = ex[i];
            t2 = p3p1[i];
            ival += (t1*t2);
        }
        for (int  i = 0; i < P3.length; i++) {
            t1 = P3[i];
            t2 = P1[i];
            t3 = ex[i] * ival;
            t  = t1 - t2 -t3;
            p3p1i += (t*t);
        }
        for (int i = 0; i < P3.length; i++) {
            t1 = P3[i];
            t2 = P1[i];
            t3 = ex[i] * ival;
            eyy = (t1 - t2 - t3)/Math.sqrt(p3p1i);
            ey[i] = eyy;
        }
        for (int i = 0; i < ey.length; i++) {
            t1 = ey[i];
            t2 = p3p1[i];
            jval += (t1*t2);
        }
        xval = (Math.pow(d1, 2) - Math.pow(d2, 2) + Math.pow(d, 2))/(2*d);
        yval = ((Math.pow(d1, 2) - Math.pow(d3, 2) + Math.pow(ival, 2) + Math.pow(jval, 2))/(2*jval)) - ((ival/jval)*xval);

        t1 = P1[0];
        t2 = ex[0] * xval;
        t3 = ey[0] * yval;
        float triptx = (float) (t1 + t2 + t3);

        t1 = P1[1];
        t2 = ex[1] * xval;
        t3 = ey[1] * yval;
        float tripty = (float) (t1 + t2 + t3);

        return createHashMap(triptx,tripty);
    }

    public HashMap<String, Float> calculateMeanXY(float x, float y){
        // 100번 측정하였을 때 Mean값 구하기 (2회 측정 필요)

        float xData = 0; // 측정 x 위치 평균 구하기 위한 변수
        float yData = 0; // 측정 y 위치 평균 구하기 위한 변수

        int n = 0;
        while (n<100) {
            xData += createHashMap(x, y).get("x");
            yData += createHashMap(x, y).get("y");
            n += 1;
        }
        float xMean = xData/100;
        float yMean = yData/100;

        return createHashMap(xMean, yMean);
    }

    public HashMap<String, Float> createModel(float realX, float realY){
        // xMean 값과 yMean값 두개를 이용하여 모델 만들기(측정 평균 값을 실제 값과 맞추기 위하여)
        // * f(x) = realX, x = x1, f(y) = realY, y = y1
        // 1. realX == ax1 + b , realY == ay1 + b
        // 2. realX-realY == a(x1 - y1)
        // 3. a == (realX - realY)/(x1 - y1)
        // 4. b == realX - ax1
        // 5. f(x) == (realX - realY)/(x1 - y1)*x + realX - ax1

        float x1 = calculateMeanXY(realX, realY).get("xMean");
        float y1 = calculateMeanXY(realX, realY).get("yMean");

        float a = (realX - realY)/(x1 - y1);
        float b  = realX - a * x1;

        return new HashMap<String,Float>(){{
            put("a",a);
            put("b",b);
        }};
    }

    public Map<String, Float> useModel(float x, float y){
        float a = createModel(x, y).get("a");
        float b  = createModel(x, y).get("b");

        float finalX = x*a + b;
        float finalY = y*a + b;

        return new HashMap<String,Float>(){{
            put("finalX",finalX);
            put("finalY",finalY);
        }};
    }
    private HashMap<String, Float> createHashMap(float x, float y){
        return new HashMap<String,Float>(){{
            put("x",x);
            put("y",y);
        }};
    }

    private Trilateration(){}

    private static class Holder{
        private static final Trilateration instance = new Trilateration();
    }
}
