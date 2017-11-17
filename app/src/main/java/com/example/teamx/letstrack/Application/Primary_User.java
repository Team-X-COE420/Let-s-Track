package com.example.teamx.letstrack.Application;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public class Primary_User extends User {

    private String password;
    private ArrayList<Position> positions;
    private ArrayList<Contact> contacts;
    private Boolean phone_verified;
    private String Current_Position;

    private static final int radius = 100;

    private static final String Other = "Other";

    PhoneVerification p_verification;

    public Primary_User(String email_ID, String contact_No, String Password) {
        super(email_ID, contact_No);
        password = Password;

        positions.add(new Position("Home", new LatLng(0, 0)));
        positions.add(new Position("Work", new LatLng(0, 0)));
        positions.add(new Position("Gym", new LatLng(0, 0)));

        p_verification = new PhoneVerification(contact_No);
    }

    public PhoneVerification getP_verification() {
        return p_verification;
    }

    public ArrayList<Contact> getContact_Array() {
        return contacts;
    }

    public ArrayList<Position> getPosition_Array() {
        return positions;
    }

    public Boolean isPhone_verified() {
        return phone_verified;
    }

    public void verify_phone(Boolean verify) {
        phone_verified = verify;
    }

    public void updatePosition(Position p) {
        for (int i = 0; i < 3; i++) {
            if (positions.get(i).getPosition_Name().equalsIgnoreCase(p.getPosition_Name())) {
                positions.get(i).setLocation(p.getLocation());
                break;
            }
        }
    }

    public int AddContact(Contact c) {
        if (contacts.size() == 5)
            return -1;
        else {
            for (int i = 0; i < contacts.size(); i++) {
                if (c.getUserName().compareTo(contacts.get(i).getUserName()) == 0 || c.getEmail_ID().compareTo(contacts.get(i).getEmail_ID()) == 0)
                    return 0;

            }

            contacts.add(c);
            return 1;

        }

    }

    public int RemoveContact(String username) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getUserName().compareTo(username) == 0) {
                contacts.remove(i);
                return 1;
            }
        }

        return -1;
    }

    private boolean isPosition(LatLng center, LatLng point) {
        if (Math.pow((center.latitude - point.latitude), 2) + Math.pow((center.longitude - point.longitude), 2) <= 100)
            return true;
        else
            return false;
    }

    public String updatePositionTag(LatLng location) {
        for (int i = 0; i < 3; i++) {
            if (isPosition(positions.get(i).getLocation(), location)) {
                Current_Position = positions.get(i).getPosition_Name();
                return Current_Position;
            }
        }

        Current_Position = Other;
        return Current_Position;
    }
}
