package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.teamx.letstrack.Application.Primary_User;
import com.example.teamx.letstrack.ExternalInterface.DatabaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Home_Screen_Activity extends Activity implements View.OnClickListener {

    Button settings;
    Button resendmail;

    Primary_User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        current_user = DatabaseHelper.readPreference(getSharedPreferences("Current_User", MODE_PRIVATE));

        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            setContentView(R.layout.verifyemail);
            resendmail = (Button) findViewById(R.id.btnResendVerificationEmail);

            resendmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Home_Screen_Activity.this, "Email sent!", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                }
            });
        } else if (!current_user.isPhone_verified())
            ;
        else
            setContentView(R.layout.activity_home_screen);



        settings = (Button) findViewById(R.id.buttonSettings);


        settings.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Settings_Activity.class));
    }
}
