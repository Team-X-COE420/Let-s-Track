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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Home_Screen_Activity extends Activity implements View.OnClickListener {

    Button settings;
    Button action;

    TextView message;

    Primary_User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        current_user = DatabaseHelper.readPreference(getSharedPreferences("Current_User", MODE_PRIVATE));

        message = (TextView) findViewById(R.id.txtmessage);

        action = (Button) findViewById(R.id.btnaction);

        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            message.setText(R.string.verifyemail);
            action.setText("Resend verification Email");
        } else if (!current_user.isPhone_verified()) {
            message.setText(R.string.verifyphone);
            action.setText("Resend code");
        } else {
            action.setVisibility(View.INVISIBLE);
            action.setEnabled(false);
            message.setVisibility(View.INVISIBLE);
            message.setText("");
        }

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText().equals(R.string.verifyemail)) {
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("", "Verification email sent");
                            Toast.makeText(Home_Screen_Activity.this, "Verification Email Sent!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("", "Failed to send verification email");
                            Toast.makeText(Home_Screen_Activity.this, "Failed to send verification email!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } else if (message.getText().equals(R.string.verifyphone)) {
                    current_user.getP_verification().sendVerificationtext();
                    DatabaseHelper.writeSharedPreference(getSharedPreferences("Current_User", MODE_PRIVATE), current_user);
                    startActivity(new Intent(Home_Screen_Activity.this, Verify_phone_activity.class));
                }
            }
        });


        settings = (Button) findViewById(R.id.buttonSettings);


        settings.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Settings_Activity.class));
    }
}
