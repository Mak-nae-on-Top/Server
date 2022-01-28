package com.maknaeontop.location;

import com.maknaeontop.Beacon;

public class Trilateration {
    public static Trilateration getInstance(){
        return Holder.instance;
    }

    public float[] calculateTrilateration(Beacon beacon1, Beacon beacon2, Beacon beacon3){
        // TODO: calculate 3D trilateration

        float x1 = beacon1.getX();
        float y1 = beacon1.getY();
        float z1 = beacon1.getZ();
        float d1 = beacon1.getDistance();

        float x2 = beacon2.getX();
        float y2 = beacon2.getY();
        float z2 = beacon1.getZ();
        float d2 = beacon2.getDistance();

        float x3 = beacon3.getX();
        float y3 = beacon3.getY();
        float z3 = beacon1.getZ();
        float d3 = beacon3.getDistance();

        double tmp1 = (Math.pow(x3,2) - Math.pow(x2,2) + Math.pow(y3,2) - Math.pow(y2,2)
                + Math.pow(d3,2) - Math.pow(d2,2)) / 2;
        double tmp2 = (Math.pow(x1,2) - Math.pow(x2,2) + Math.pow(y1,2) - Math.pow(y2,2)
                + Math.pow(d2,2) - Math.pow(d1,2)) / 2;

        float y = (float) (((tmp1 * (x2 - x3)) - (tmp2 * (x2 - x1))) / (((y1 - y2) * (x2 - x3))
                        - ((y3 - y2) * (x2 - x1))));
        float x = (float) (((y * (y1 - y2)) - tmp1) / (x2 - x1));

        return new float[]{x, y};
    }

    private Trilateration(){}

    private static class Holder{
        private static final Trilateration instance = new Trilateration();
    }
}
