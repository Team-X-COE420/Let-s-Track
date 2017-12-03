package com.example.teamx.letstrack.ExternalInterface;

import java.util.ArrayList;

/**
 * Created by arany on 02/12/2017.
 */

public interface DatabaseHandler {

    void UpdateUI(boolean res);

    void DisplayContactRequests(ArrayList<String> req_emails);
}
