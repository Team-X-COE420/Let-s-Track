package com.example.teamx.letstrack.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Settings_Activity extends AppCompatActivity {

    Button changePass;
    Button changeContact;
    Button changePosition;
    Button Logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        changePass = (Button) findViewById(R.id.buttonChangePassword);
        changeContact = (Button) findViewById(R.id.buttonUpdateContacts);
        changePosition = (Button) findViewById(R.id.buttonUpdatePositions);
        Logout = (Button) findViewById(R.id.buttonLogout);

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
                Toast.makeText(Settings_Activity.this, "Change/Modify position activity is opened", Toast.LENGTH_SHORT).show();
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent in = new Intent(Settings_Activity.this, Sign_In_Activity.class);
                Settings_Activity.this.finish();
                startActivity(in);
            }
        });
    }
}
