package com.example.teamx.letstrack.Application;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

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

    PhoneVerification p_verification;

    public Primary_User(String email_ID, String contact_No, String Password) {
        super(email_ID, contact_No);
        password=Password;

        p_verification=new PhoneVerification(contact_No);
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

    public void verify_phone(Boolean verify)
    {
        phone_verified=verify;
    }
    public void updatePositionTag(LatLng location)
    {

    }
}
