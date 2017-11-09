package com.example.teamx.letstrack.Application;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public abstract class User {
    protected String Email_ID;
    protected String Contact_No;

    public User(String email_ID, String contact_No) {
        Email_ID = email_ID;
        Contact_No = contact_No;
    }

    public String getEmail_ID() {
        return Email_ID;
    }

    public void setEmail_ID(String email_ID) {
        Email_ID = email_ID;
    }

    public String getContact_No() {
        return Contact_No;
    }

    public void setContact_No(String contact_No) {
        Contact_No = contact_No;
    }
}
