package com.maknaeontop.location;

import com.maknaeontop.dto.Beacon;

import java.util.HashMap;
import java.util.Map;

public class Trilateration {
    public static Trilateration getInstance(){
        return Holder.instance;
    }

    public HashMap<String, Float> calculateTrilateration(Beacon beacon1, Beacon beacon2, Beacon beacon3){
        float x1 = beacon1.getX();
        float y1 = beacon1.getY();
        float d1 = beacon1.getAccuracy();

        float x2 = beacon2.getX();
        float y2 = beacon2.getY();
        float d2 = beacon2.getAccuracy();

        float x3 = beacon3.getX();
        float y3 = beacon3.getY();
        float d3 = beacon3.getAccuracy();

        double tmp1 = (Math.pow(x3,2) - Math.pow(x2,2) + Math.pow(y3,2) - Math.pow(y2,2)
                + Math.pow(d3,2) - Math.pow(d2,2)) / 2;
        double tmp2 = (Math.pow(x1,2) - Math.pow(x2,2) + Math.pow(y1,2) - Math.pow(y2,2)
                + Math.pow(d2,2) - Math.pow(d1,2)) / 2;

        float y = (float) (((tmp1 * (x2 - x3)) - (tmp2 * (x2 - x1))) / (((y1 - y2) * (x2 - x3))
                        - ((y3 - y2) * (x2 - x1))));
        float x = (float) (((y * (y1 - y2)) - tmp1) / (x2 - x1));

        return createHashMap(x,y);
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

        return new HashMap<String,Float>(){{
            put("xMean", xMean);
            put("yMean", yMean);
        }};
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
