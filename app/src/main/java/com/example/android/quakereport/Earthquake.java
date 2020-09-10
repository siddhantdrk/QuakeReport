package com.example.android.quakereport;

public class Earthquake {
    private double magnitude;
    private String place;
    private long miliTime;
    private String url;
    Earthquake(double magnitude, String place, long miliTime, String url){
        this.magnitude=magnitude;
        this.place=place;
        this.miliTime=miliTime;
        this.url=url;
    }

    public double getMagnitude(){
        return magnitude;
    }

    public String getPlace() {
        return place;
    }

    public long getmiliTime(){
        return miliTime;
    }

    public String getUrl() { return url; }
}
