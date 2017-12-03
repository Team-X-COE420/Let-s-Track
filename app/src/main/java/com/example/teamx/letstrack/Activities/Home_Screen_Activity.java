package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamx.letstrack.Application.Primary_User;
import com.example.teamx.letstrack.ExternalInterface.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;

public class Home_Screen_Activity extends Activity implements View.OnClickListener {

    Button settings;
    Button action;

    TextView message;
    TextView codeexists;

    Primary_User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        File f = new File("/data/data/com.example.teamx.letstrack/shared_prefs/Current_User.xml");
        if (f.exists()) {
            current_user = DatabaseHelper.readPreference(getSharedPreferences("Current_User", MODE_PRIVATE));
        } else {
            readuserfromfirestore();
        }
        message = (TextView) findViewById(R.id.txtmessage);
        codeexists = (TextView) findViewById(R.id.txtCodeExists);

        codeexists.setEnabled(false);

        action = (Button) findViewById(R.id.btnaction);

        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            message.setText(R.string.verifyemail);
            action.setText("Resend verification Email");
        } else if (!current_user.isPhone_verified()) {
            message.setText(R.string.verifyphone);
            action.setText("Resend code");
            codeexists.setEnabled(true);
            codeexists.setVisibility(View.VISIBLE);
        } else {
            action.setVisibility(View.INVISIBLE);
            action.setEnabled(false);
            message.setVisibility(View.INVISIBLE);
            message.setText("Null");
        }

        codeexists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Home_Screen_Activity.this, Verify_phone_activity.class));
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home_Screen_Activity.this, "Button Clicked", Toast.LENGTH_SHORT)
                        .show();
                if (message.getText().equals(getResources().getString(R.string.verifyemail))) {
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Firebase", "Verification email sent");
                            Toast.makeText(Home_Screen_Activity.this, "Verification Email Sent!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Firebase", "Failed to send verification email");
                            Toast.makeText(Home_Screen_Activity.this, "Failed to send verification email!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } else if (message.getText().equals(getResources().getString(R.string.verifyphone))) {
                    current_user.getP_verification().sendVerificationtext();
                    DatabaseHelper.writeSharedPreference(getSharedPreferences("Current_User", MODE_PRIVATE), current_user);
                    startActivity(new Intent(Home_Screen_Activity.this, Verify_phone_activity.class));
                }
            }
        });


        settings = (Button) findViewById(R.id.buttonSettings);


        settings.setOnClickListener(this);
    }

    private void readuserfromfirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(FirebaseAuth.getInstance().getUid().toString());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null) {
                        Log.d("Firestore", "Document Retrieved");
                    } else {
                        Log.d("Firestore", "Document does not exist");
                    }

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Settings_Activity.class));
    }
}
