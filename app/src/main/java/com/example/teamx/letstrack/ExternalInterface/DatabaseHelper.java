package com.example.teamx.letstrack.ExternalInterface;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.teamx.letstrack.Application.Contact;
import com.example.teamx.letstrack.Application.Position;
import com.example.teamx.letstrack.Application.Primary_User;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public class DatabaseHelper {

    static String fileName;

    public static void writetofile(Context context, Primary_User u) {
        fileName = FirebaseAuth.getInstance().getUid();
        File file = new File(context.getFilesDir(), fileName);

        file.setWritable(true);
        try {
            if (!file.exists())
                file.createNewFile();

            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(u.getEmail_ID());
            writer.newLine();
            writer.write(u.getContact_No());
            writer.newLine();
            writer.write(u.isPhone_verified().toString());
            writer.newLine();
            if (u.isPhone_verified()) {
                ArrayList<Position> positions = u.getPosition_Array();
                for (int i = 0; i < 4; i++) {
                    writer.write(positions.get(i).getPosition_Name() +
                            " " + positions.get(i).getLocation().latitude +
                            " " + positions.get(i).getLocation().longitude);
                    writer.newLine();
                }

                ArrayList<Contact> contacts = u.getContact_Array();
                for (int i = 0; i < 5; i++) {
                    writer.write(contacts.get(i).getUserName() +
                            " " + contacts.get(i).getContact_No() +
                            " " + contacts.get(i).getEmail_ID() +
                            " " + contacts.get(i).getCurrent_position() +
                            " " + contacts.get(i).getStatus().getValue());
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        file.setWritable(false);
    }

    public static void writeSharedPreference(SharedPreferences shared_pref, Primary_User user) {
        SharedPreferences.Editor edit = shared_pref.edit();

        edit.putString("Email", user.getEmail_ID());
        edit.putString("Contact_No", user.getContact_No());

        edit.putBoolean("isPhoneVerified", user.isPhone_verified());

        if (user.isPhone_verified()) {
           /* ArrayList<Position> positions = user.getPosition_Array();
            for (int i = 0; i < 4; i++)
                edit.putString(positions.get(i).getPosition_Name(), positions.get(i).getLocation().toString());

            ArrayList<Contact> contacts = user.getContact_Array();

            for (int i = 0; i < 5; i++) {
                Contact c = contacts.get(i);
                if (!c.getUserName().isEmpty()) {
                    edit.putString("Contact_" + (i + 1) + "_Name", c.getUserName());
                    edit.putString("Contact_" + (i + 1) + "_Email", c.getEmail_ID());
                    edit.putInt("Contact_" + (i + 1) + "_Status", c.getStatus().getValue());
                    if (c.getStatus() == Contact_Status.Accepted) {
                        edit.putString("Contact_" + (i + 1) + "_ContactNo", c.getContact_No());
                        edit.putString("Contact_" + (i + 1) + "_CurrentPosition", c.getCurrent_position());
                    }
                }
            }*/
        } else {
            edit.putString("Code", user.getP_verification().getCode());
        }
        edit.commit();
    }

    public static Primary_User readPreference(SharedPreferences shared_pref)
    {
        Primary_User current_user=new Primary_User("","","");

        //TODO read from shared preference

        current_user.setEmail_ID(shared_pref.getString("Email", ""));
        current_user.setContact_No(shared_pref.getString("Contact_No", ""));
        current_user.setPhone_verified(shared_pref.getBoolean("isPhoneVerified", false));

        if (current_user.isPhone_verified()) {
            String pos = shared_pref.getString("Home", "");
            //pos.split(",");
            //Position p=new Position("Home",new LatLng());
        } else {
            current_user.getP_verification().setCode(shared_pref.getString("Code", ""));
        }

        return current_user;
    }

    public static Primary_User readFile(FirebaseAuth mAuth)
    {
        Primary_User current_user=new Primary_User("","","");
        String file_name=mAuth.getUid();

        File file=new File(file_name);
        if(file.exists())
        {
            //TODO read from file


        }
        return current_user;
    }
}
