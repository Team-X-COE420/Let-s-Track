package com.example.teamx.letstrack.Application;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public enum Contact_Status {
    Accepted(1),
    Denied(-1),
    Pending(0),
    NoContact(5);

    private final int value;

    Contact_Status(int value)
    {
        this.value=value;
    }

    public int getValue()
    {
        return getValue();
    }
}
