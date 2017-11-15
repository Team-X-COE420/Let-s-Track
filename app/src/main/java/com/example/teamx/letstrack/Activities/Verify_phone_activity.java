package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teamx.letstrack.Application.PhoneVerification;
import com.example.teamx.letstrack.Application.Primary_User;

public class Verify_phone_activity extends Activity {

    EditText editTextCode;
    Button buttonCodeSubmit;

    SharedPreferences saved_value;

    Primary_User current_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        editTextCode = (EditText) findViewById(R.id.editTextCode);

        buttonCodeSubmit = (Button) findViewById(R.id.buttonCodeSubmit);

        buttonCodeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify_phone();
            }
        });

        saved_value = getSharedPreferences("Current_User", MODE_PRIVATE);
    }

    private void verify_phone()
    {
        final String code = editTextCode.getText().toString().trim();
        if (code.isEmpty())
            Toast.makeText(this, "Enter code", Toast.LENGTH_SHORT);
        else {
            Primary_User current_user = new Primary_User(saved_value.getString("Email", ""), saved_value.getString("Contact_No.", ""), saved_value.getString("Password", ""));

            String c = saved_value.getString("Code", "");

            PhoneVerification p = new PhoneVerification(saved_value.getString("Contact_No.", ""));
            p.setCode(c);
            current_user.verify_phone(p.verifyCode(code));

            if (current_user.isPhone_verified()) {
                Toast.makeText(this, "Phone Verified", Toast.LENGTH_SHORT);
                startActivity(new Intent(this, Home_Screen_Activity.class));
                finish();
            } else
                Toast.makeText(this, "Incorrect code", Toast.LENGTH_SHORT);
        }

    }
}
