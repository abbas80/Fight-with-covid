package com.example.fightwithcovid;

public class coordinates {
   private long PhoneNo;
    private double lat;
    private double lon;

    public coordinates()
    {
        PhoneNo=0;
        lat=0;
        lon=0;
    }
    public coordinates(double lat, double lon,long phoneNo) {
        PhoneNo = phoneNo;
        this.lat = lat;
        this.lon = lon;
    }

    public long getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(long phoneNo) {
        PhoneNo = phoneNo;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
