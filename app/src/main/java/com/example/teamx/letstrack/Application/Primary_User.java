package com.example.teamx.letstrack.Application;

import java.util.ArrayList;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public class Primary_User extends User {

    private String password;
    private ArrayList<Position> positions;
    private ArrayList<Contact> contacts;

    public void setPhone_verified(Boolean phone_verified) {
        this.phone_verified = phone_verified;
    }

    private Boolean phone_verified;
    private String Current_Position;

    private static final int radius = 1;

    private static final String Other = "Other";

    PhoneVerification p_verification;

    public Primary_User(String email_ID, String contact_No, String Password) {
        super(email_ID, contact_No);
        password = Password;
        phone_verified = false;
        positions = new ArrayList<Position>();
        contacts = new ArrayList<Contact>();
        LatLng origin = new LatLng(0, 0);
        Position Home = new Position("Home", origin);
        Position Work = new Position("Work", origin);
        Position Gym = new Position("Gym", origin);
        positions.add(Home);
        positions.add(Work);
        positions.add(Gym);

        p_verification = new PhoneVerification(contact_No);
    }

    @Override
    public void setContact_No(String Contact_No) {
        super.Contact_No = Contact_No;
        p_verification.setPhone(Contact_No);
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

            c.setStatus(Contact_Status.Pending);
            contacts.add(c);
            return 1;
        }
    }

    public Contact getContact(String username) {
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getUserName().compareTo(username) == 0) {
                return contacts.get(i);
            }
        }
        return null;
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

    private boolean isPosition(com.example.teamx.letstrack.Application.LatLng center, com.example.teamx.letstrack.Application.LatLng point) {
        if (Math.pow((center.latitude - point.latitude), 2) + Math.pow((center.longitude - point.longitude), 2) <= 1)
            return true;
        else
            return false;
    }

    public String updatePositionTag(com.example.teamx.letstrack.Application.LatLng location) {
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
