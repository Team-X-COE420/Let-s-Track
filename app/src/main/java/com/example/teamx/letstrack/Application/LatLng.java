package com.example.teamx.letstrack.Application;

/**
 * Created by Aranyak on 17-Nov-17.
 */

/*

        WRAPPER CLASS FOR LatLng Class with suitable toString Functions
 */

public class LatLng {
    public void setLocation(com.google.android.gms.maps.model.LatLng location) {
        this.location = location;
        latitude = location.latitude;
        longitude = location.longitude;
    }

    public com.google.android.gms.maps.model.LatLng location;

    public double latitude;
    public double longitude;

    public LatLng(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;

        location = new com.google.android.gms.maps.model.LatLng(latitude, longitude);

    }

    public LatLng(com.google.android.gms.maps.model.LatLng location) {
        this.location = location;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
    }

    @Override
    public String toString() {
        return location.latitude + "," + location.longitude;
    }
}
