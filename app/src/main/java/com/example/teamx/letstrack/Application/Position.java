package com.example.teamx.letstrack.Application;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public class Position {
    private String Name;
    private LatLng Location;

    public String getPosition_Name() {
        return Name;
    }

    public Position(String name, LatLng location) {
        Name = name;
        Location = location;
    }

    public void setName(String name) {
        Name = name;
    }

    public LatLng getLocation() {
        return Location;
    }

    public void setLocation(LatLng location) {
        Location = location;
    }
}
