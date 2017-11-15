package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teamx.letstrack.Application.Primary_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_Activity extends Activity implements View.OnClickListener {

    private static String TAG;

    private Button ButtonRegister;

    private EditText EditTextEmail;
    private EditText EditTextPassword;
    private EditText EditTextConfirmPassword;
    private EditText EditTextPhone;

    private android.app.ProgressDialog ProgressDialog;

    private FirebaseAuth mAuth;

    private Primary_User user;

    private SharedPreferences shared_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButtonRegister = (Button) findViewById(R.id.buttonRegister);

        EditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditTextPassword = (EditText) findViewById(R.id.editTextPassword);
        EditTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        EditTextPhone = (EditText) findViewById(R.id.editTextPhone);

        ProgressDialog = new ProgressDialog(this);

        ButtonRegister.setOnClickListener(this);

        shared_pref = getSharedPreferences("Current_User", MODE_PRIVATE);
    }

    private void Register_User() {
        requestSmsPermission();
        final String email = EditTextEmail.getText().toString().trim();
        final String password = EditTextPassword.getText().toString().trim();
        final String Confirmpassword = EditTextConfirmPassword.getText().toString().trim();
        final String phone = EditTextPhone.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //email field is empty
            Toast.makeText(this, "Please enter an email ID", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            //password field is empty
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(Confirmpassword) || !Confirmpassword.equals(password)) {
            //Confirm password field is empty
            Toast.makeText(this, "Passwords do not match! Try again", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(phone)) {
            //Contact Number field is empty
            Toast.makeText(this, "Please enter a contact number", Toast.LENGTH_SHORT).show();
            return;
        } else if (!android.util.Patterns.PHONE.matcher(phone).matches()) {
            Toast.makeText(this, "Please enter a valid contact number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email ID", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog.setMessage("Registering...");
        ProgressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register_Activity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    mAuth.getCurrentUser().sendEmailVerification();
                    user = new Primary_User(email, phone, password);
                    user.getP_verification().sendVerificationtext();

                    startActivity(new Intent(Register_Activity.this, Verify_phone_activity.class));
                } else
                    Toast.makeText(Register_Activity.this, "Registration unSuccessful!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


    public void onPause() {

        if (user != null) {
            SharedPreferences.Editor edit = shared_pref.edit();
            edit.putString("Email", user.getEmail_ID());
            edit.putString("Contact_No", user.getContact_No());
            edit.putBoolean("Phone_verified", user.isPhone_verified());
            edit.putString("Code", user.getP_verification().getCode());
        }

        super.onPause();
    }

    @Override
    public void onClick(View v) {
        Register_User();
    }
}
