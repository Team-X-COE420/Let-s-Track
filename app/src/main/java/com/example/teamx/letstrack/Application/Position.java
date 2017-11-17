package com.example.teamx.letstrack.Application;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public class Position {
    private String Name;
    private com.example.teamx.letstrack.Application.LatLng Location;

    public String getPosition_Name() {
        return Name;
    }

    public Position(String name, com.example.teamx.letstrack.Application.LatLng location) {
        Name = name;
        Location = location;
    }

    public void setName(String name) {
        Name = name;
    }

    public com.example.teamx.letstrack.Application.LatLng getLocation() {
        return Location;
    }

    public void setLocation(com.example.teamx.letstrack.Application.LatLng location) {
        Location = location;
    }
}
