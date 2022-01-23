package com.maknaeontop.location;

public class Trilateration {
    public Trilateration getInstance(){
        return Holder.instance;
    }



    private Trilateration(){}

    private static class Holder{
        private static final Trilateration instance = new Trilateration();
    }
}
