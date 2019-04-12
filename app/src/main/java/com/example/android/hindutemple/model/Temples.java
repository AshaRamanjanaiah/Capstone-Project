package com.example.android.hindutemple.model;

public class Temples {

    String templeId;
    String templeName;
    String templeLocation;
    String templeImageUri;

    public Temples(){

    }

    public Temples(String templeId, String templeName, String templeLocation, String templeImageUri) {
        this.templeId = templeId;
        this.templeName = templeName;
        this.templeLocation = templeLocation;
        this.templeImageUri = templeImageUri;
    }

    public String getTempleId() {
        return templeId;
    }

    public String getTempleName() {
        return templeName;
    }

    public String getTempleLocation() {
        return templeLocation;
    }

    public String getTempleImageUri() {
        return templeImageUri;
    }

    @Override
    public String toString() {
        return templeName;
    }
}
