package com.example.teamx.letstrack.ExternalInterface;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.teamx.letstrack.Application.Contact;
import com.example.teamx.letstrack.Application.Contact_Status;
import com.example.teamx.letstrack.Application.LatLng;
import com.example.teamx.letstrack.Application.Position;
import com.example.teamx.letstrack.Application.Primary_User;
import com.example.teamx.letstrack.Application.UIConnector;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aranyak on 08-Nov-17.
 */

public class DatabaseHelper {


    //Collection/SubCollection names:

    private static final String Users = "Users";
    private static final String Contacts = "Pending";
    private static final String Sent = "Sent";
    private static final String Position = "Position";
    private static final String Accepted = "Accepted";
    private static final String Pending = "Pending";

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

    public static Primary_User readPreference(SharedPreferences shared_pref) {
        Primary_User current_user = new Primary_User("", "", "");

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

    public static void writeUserToDatabase(final UIConnector a, Primary_User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usermap = new HashMap<>();
        usermap.put("Email", user.getEmail_ID());
        usermap.put("phone_verified", user.isPhone_verified());
        usermap.put("Contact No", user.getP_verification().getPhone());

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

    //Add location to Firestore
    public static void Add_Location(Position loc) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection(Users)
                .document(FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getEmail());

        LatLng location = loc.getLocation();
        GeoPoint point = new GeoPoint(location.latitude, location.longitude);

        Map<String, Object> map = new HashMap<>();

        map.put(loc.getPosition_Name(), point);

        doc.set(map, SetOptions.merge());
    }

    public static void Get_Location(final Position loc) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection(Users)
                .document(FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getEmail());
    }

    //??????????????
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
                        String phone = (String) doc.get("Contact No");
                        contact.setContact_number(phone);
                    }
                });
        return contact;
    }

    //Function to observe change in contacts position
    public static void updateContactPosition(UIConnector a) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection(Users)
                .document(FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getEmail());

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList contacts = (ArrayList) task.getResult().get(Accepted);
            }
        });
    }

    //Function to update the database with users current position
    public static void updatePosition(String Tag) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Users)
                .document(FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getEmail())
                .collection(Position)
                .document(Position)
                .set(Tag);
    }

    private static Firebase_Contact get_contact_info_from_phone(String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Firebase_Contact contact = new Firebase_Contact();


        if (phone.startsWith("00"))
            phone = "+" + phone.substring(2);
        else if (phone.startsWith("05"))
            phone = "+971" + phone.substring(1);

        contact.setContact_number(phone);

        db.collection(Users)
                .whereEqualTo("Contact No", phone)
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

    public static void sendRequest(final UIConnector a, final String receiver_email) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Firebase_Contact contact;

        final ArrayList<String> sentreq_email = new ArrayList<String>();

        final String sender_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DocumentReference doc = db.collection(Users)
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail())
                .collection(Sent)
                .document(Sent);

        doc.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList temp = (ArrayList) task.getResult()
                                .get(Sent);
                        for (Object email : temp) {
                            sentreq_email.add((String) email);
                        }
                    }
                });

        sentreq_email.add(receiver_email);

        doc.set(sentreq_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    final ArrayList<String> Pendingemail = new ArrayList<>();
                    DocumentReference doc2 = db.collection(Users)
                            .document(receiver_email)
                            .collection(Pending)
                            .document(Pending);

                    doc2.get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    ArrayList temp = (ArrayList) task.getResult()
                                            .get(Pending);
                                    for (Object email : temp) {
                                        Pendingemail.add((String) email);
                                    }
                                }
                            });
                    Pendingemail.add(FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getEmail());
                    doc2.set(Pendingemail);
                    a.UpdateUI(true);
                }

                else
                    a.UpdateUI(false);
            }

        });

    }


    public static void contact_requests(final UIConnector a) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference doc = db.collection(Users)
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail())
                .collection(Pending)
                .document(Pending);
    }

    public static void readUserinfo(final String email, final Primary_User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Users).document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    user.setContact_No(doc.getString("Contact No"));
                    user.verify_phone(doc.getBoolean("phone_verified"));
                    user.setEmail_ID(email);
                    //TODO read locations and contact info
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
