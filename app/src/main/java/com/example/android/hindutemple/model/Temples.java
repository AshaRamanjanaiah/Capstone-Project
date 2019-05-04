package com.example.android.hindutemple.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Temples implements Parcelable {

    private String templeId;
    private String templeName;
    private String templeLocation;
    private String templeImageUri;

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
    @NonNull
    public String toString() {
        return templeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(templeId);
        parcel.writeString(templeName);
        parcel.writeString(templeLocation);
        parcel.writeString(templeImageUri);
    }

    private Temples(Parcel in){
        templeId = in.readString();
        templeName = in.readString();
        templeLocation = in.readString();
        templeImageUri = in.readString();
    }

    public static final Parcelable.Creator<Temples> CREATOR = new Creator<Temples>() {

        @Override
        public Temples createFromParcel(Parcel source) {
            return new Temples(source);
        }

        @Override
        public Temples[] newArray(int size) {
            return new Temples[size];
        }
    };
}
