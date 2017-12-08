package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Settings_Activity extends Activity {

    Button changePass;
    Button changeContact;
    Button changePosition;
    Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changePass = findViewById(R.id.buttonChangePassword);
        changeContact = findViewById(R.id.buttonUpdateContacts);
        changePosition = findViewById(R.id.buttonUpdatePositions);
        Logout = findViewById(R.id.buttonLogout);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings_Activity.this, Change_Password_Activity.class));
            }
        });

        changeContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO create Change/Modify contact activity and link
                Toast.makeText(Settings_Activity.this, "Change/Modify contact activity is opened", Toast.LENGTH_SHORT).show();
            }
        });

        changePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO create Change/Modify Position activity and link
                Log.d("", "Entering map");
                startActivity(new Intent(Settings_Activity.this, Define_Position_Activity.class));
                Toast.makeText(Settings_Activity.this, "Change/Modify position activity is opened", Toast.LENGTH_SHORT).show();
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent in = new Intent(Settings_Activity.this, Start_up_Activity.class);
                Settings_Activity.this.finish();
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }
        });
    }
}
