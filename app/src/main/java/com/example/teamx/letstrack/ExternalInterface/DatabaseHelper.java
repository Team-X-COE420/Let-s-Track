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

        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                GeoPoint point = (GeoPoint) task.getResult().get(loc.getPosition_Name());
                loc.setLocation(new LatLng(point.getLatitude(), point.getLongitude()));
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

    public static void sendRequest(final UIConnector a, final String receiver_email) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final Firebase_Contact contact;

        final ArrayList<String> sentreq_email = getsentrequests();


        final String sender_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        DocumentReference doc = db.collection(Users)
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail())
                .collection(Sent)
                .document(Sent);

        final DocumentReference doc2 = db.collection(Users)
                .document(receiver_email)
                .collection(Pending)
                .document(Pending);

        db.collection(Users)
                .document(receiver_email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc3 = task.getResult();
                        if (!doc3.exists())
                            a.UpdateUI(false);
                    }
                });


        sentreq_email.add(receiver_email);

        doc.set(sentreq_email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    final ArrayList<String> Pending_List = new ArrayList<>();


                    doc2.get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    ArrayList temp = (ArrayList) task.getResult()
                                            .get(Pending);
                                    for (Object email : temp) {
                                        Pending_List.add((String) email);
                                    }
                                }
                            });
                    Pending_List.add(FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getEmail());
                    Map<String, ArrayList<String>> map = new HashMap<>();
                    map.put(Pending, Pending_List);
                    doc2.set(map);
                    a.UpdateUI(true);
                } else
                    a.UpdateUI(false);
            }

        });

    }

    public static ArrayList<String> getpendingrequests() {
        final ArrayList<String> req = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(Users)
                .document(FirebaseAuth.getInstance().
                        getCurrentUser()
                        .getEmail())
                .collection(Pending)
                .document(Pending)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList temp = (ArrayList) task.getResult().get(Pending);
                        for (Object email : temp) {
                            req.add((String) email);
                        }
                    }
                });

        return req;
    }

    public static ArrayList<String> getsentrequests() {
        final ArrayList<String> sentreq_email = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        return sentreq_email;
    }

    public static void verifyPhone(boolean verify) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference doc = db.collection(Users)
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail());
        Map<String, Boolean> map = new HashMap<>();
        map.put("phone_verified", verify);
        doc.set(map, SetOptions.merge());
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

                    com.example.teamx.letstrack.Application.Position Home = new Position("Home", null);
                    DatabaseHelper.Get_Location(Home);
                    user.updatePosition(Home);
                    Position Work = new Position("Work", null);
                    Get_Location(Work);
                    user.updatePosition(Work);
                    Position Gym = new Position("Gym", null);
                    Get_Location(Gym);
                    user.updatePosition(Gym);

                }

            }
        });
    }

    public static ArrayList<Firebase_Contact> getAcceptedContacts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final ArrayList<Firebase_Contact> contacts = new ArrayList<>();

        db.collection(Users)
                .document(FirebaseAuth.getInstance().
                        getCurrentUser()
                        .getEmail())
                .collection(Accepted)
                .document(Accepted)
                .get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        ArrayList temp = (ArrayList) document.get(Accepted);
                        for (Object contact : temp)
                            contacts.add((Firebase_Contact) contact);
                    }
                });

        return contacts;
    }

    public void AcceptRequests(String email, String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference doc = db.collection(Users)
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail())
                .collection(Pending)
                .document(Pending);

        ArrayList<String> Pending_List = getpendingrequests();

        Pending_List.remove(email);

        Map<String, ArrayList<String>> map = new HashMap<>();
        map.put(Pending, Pending_List);
        doc.set(map);

        DocumentReference doc2 = db.collection(Users)
                .document(email)
                .collection(Sent)
                .document(Sent);

        ArrayList<String> Sent_List = getsentrequests();

        Sent_List.remove(FirebaseAuth.getInstance()
                .getCurrentUser()
                .getEmail());
        map.clear();
        map.put(Sent, Sent_List);

        doc2.set(map);

        final ArrayList<Firebase_Contact> contacts = getAcceptedContacts();
        contacts.add(new Firebase_Contact(email, username));

        Map<String, ArrayList<Firebase_Contact>> map2 = new HashMap<>();
        map2.put(Accepted, contacts);

        db.collection(Users)
                .document(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getEmail())
                .collection(Accepted)
                .document(Accepted)
                .set(map2);

        contacts.clear();

        DocumentReference document = db.collection(Users).document(email).collection(Accepted).document(Accepted);
        document.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                ArrayList temp = (ArrayList) document.get(Accepted);
                for (Object contact : temp)
                    contacts.add((Firebase_Contact) contact);
            }
        });

        contacts.add(new Firebase_Contact(FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));

        document.set(new HashMap<String, ArrayList<Firebase_Contact>>().put(Accepted, contacts));

    }

    private static class Firebase_Contact {
        String email;

        String username;

        public Firebase_Contact(String email, String username) {
            this.email = email;

            this.username = username;
        }

        public Firebase_Contact() {
            email = "";

            username = "";
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }

}
