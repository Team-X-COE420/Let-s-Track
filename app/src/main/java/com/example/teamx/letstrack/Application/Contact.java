package com.example.teamx.letstrack.Application;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public class Contact extends User {
    private String username;
    private String current_position;
    private Contact_Status status;

    public Contact(String email_ID, String contact_No, String username, Contact_Status status) {
        super(email_ID, contact_No);
        this.username = username;
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrent_position(String current_position) {
        this.current_position = current_position;
    }

    public void setStatus(Contact_Status status) {
        this.status = status;
    }

    public String getUserName() {

        return username;
    }

    public String getCurrent_position() {
        return current_position;
    }

    public Contact_Status getStatus() {
        return status;
    }
}
