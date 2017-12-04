package com.example.teamx.letstrack.ExternalInterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.teamx.letstrack.Application.Contact;
import com.example.teamx.letstrack.Application.Contact_Status;
import com.example.teamx.letstrack.Application.LatLng;
import com.example.teamx.letstrack.Application.Position;
import com.example.teamx.letstrack.Application.Primary_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            ArrayList<Position> positions = user.getPosition_Array();
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
            }
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
            String[] loc = pos.split(",");
            Position p = new Position("Home", new LatLng(Double.parseDouble(loc[0]), Double.parseDouble(loc[1])));

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

    public static void writeUserToDatabase(final DatabaseHandler a, Primary_User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usermap = new HashMap<>();
        usermap.put("Email", user.getEmail_ID());
        usermap.put("phone_verified", user.isPhone_verified());
        usermap.put("Contact No.", user.getP_verification().getPhone());

        db.collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .set(usermap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Firestore", "Document Snapshot Successfully written");

                a.UpdateUI(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Firestore", "Error Writing Document", e);
                a.UpdateUI(false);

            }
        });
    }

    private static Firebase_Contact get_contact_info_from_email(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Firebase_Contact contact = new Firebase_Contact();
        contact.setEmail(email);
        db.collection("Users")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        String phone = (String) doc.get("Contact No.");
                        contact.setContact_number(phone);
                    }
                });
        return contact;
    }

    private static Firebase_Contact get_contact_info_from_phone(String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Firebase_Contact contact = new Firebase_Contact();


        if (phone.startsWith("00"))
            phone = "+" + phone.substring(2);
        else if (phone.startsWith("05"))
            phone = "+971" + phone.substring(1);

        contact.setContact_number(phone);

        db.collection("Users")
                .whereEqualTo("Contact No.", phone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot doc : task.getResult()) {
                                String email = (String) doc.get("Email");
                                contact.setEmail(email);
                            }
                        }
                    }
                });

        return contact;
    }

    public static void addcontact(final DatabaseHandler a, final String receiver_email, String receiver_phone) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Firebase_Contact contact;

        if (receiver_email == null)
            contact = get_contact_info_from_phone(receiver_phone);
        else if (receiver_phone == null)
            contact = get_contact_info_from_email(receiver_email);
        else
            contact = new Firebase_Contact(receiver_email, receiver_phone);


        final String sender_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        db.collection("Contacts")
                .document(sender_email)
                .collection("Pending")
                .document(receiver_email)
                .set(contact)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.d("Firestore", "Sent a contact request from sender to " + receiver_email);
                            db.collection("Contacts")
                                    .document(receiver_email)
                                    .collection("Pending")
                                    .document().set(contact)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                                Log.d("Firestore", "Request added to pending stack.");
                                            else
                                                Log.d("Firestore", "Request not added to pending stack");

                                        }
                                    });
                        } else {
                            Log.d("Firestore", "Failed to send a contact request to " + receiver_email);
                        }

                        a.UpdateUI(task.isSuccessful());
                    }

                });


    }

    public static void contact_requests(final DatabaseHandler a) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("Contacts").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        docRef.collection("Pending").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Log.d("Firestore", "Document received with content: " + doc.getData());

                    }
                }
            }
        });
    }

    private static class Firebase_Contact {
        String email;
        String contact_number;

        public Firebase_Contact(String email, String contact_number) {
            this.email = email;
            this.contact_number = contact_number;
        }

        public Firebase_Contact() {
            email = "";
            contact_number = "";
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }
    }

}
