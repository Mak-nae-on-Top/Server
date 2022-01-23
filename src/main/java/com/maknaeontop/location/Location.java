package com.maknaeontop.location;

public class Location {
    private Location(){}

    public Location getInstance() {
        return Holder.instance;
    }

    // LazyHolder
    private static class Holder{
        private static final Location instance = new Location();
    }

    public String findLocation(){
        return "testing";
    }
}
