package com.example.teamx.letstrack.Application;

import java.util.ArrayList;

/**
 * Created by arany on 02/12/2017.
 */

public interface UIConnector {

    void UpdateUI(boolean res);

    void DisplayContactRequests(ArrayList<String> req_emails);

    void DisplayCurrentContacts(ArrayList<Contact> contacts);
}
