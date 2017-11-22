package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.teamx.letstrack.Application.Primary_User;
import com.example.teamx.letstrack.ExternalInterface.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

public class Home_Screen_Activity extends Activity implements View.OnClickListener {

    Button settings;
    TextView Verify;

    Primary_User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        current_user = DatabaseHelper.readPreference(getSharedPreferences("Current_User", MODE_PRIVATE));

        settings = (Button) findViewById(R.id.buttonSettings);
        Verify = (TextView) findViewById(R.id.VerifyEmail);

        Verify.setVisibility(View.INVISIBLE);

        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            displayemailverified();
        }
        if (!current_user.isPhone_verified()) {
            displayPhoneverified();
        }


        settings.setOnClickListener(this);
    }

    void displayemailverified() {
        settings.setEnabled(false);
        settings.setVisibility(View.INVISIBLE);

        Verify.setVisibility(View.VISIBLE);

    }

    void displayPhoneverified() {
        settings.setEnabled(false);
        settings.setVisibility(View.INVISIBLE);

        Verify.setText("Verify Contact Number");
        Verify.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Settings_Activity.class));
    }
}
