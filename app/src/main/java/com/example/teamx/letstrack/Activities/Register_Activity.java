package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teamx.letstrack.Application.Contact;
import com.example.teamx.letstrack.Application.Primary_User;
import com.example.teamx.letstrack.Application.UIConnector;
import com.example.teamx.letstrack.ExternalInterface.DatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Register_Activity extends Activity implements View.OnClickListener, UIConnector {

    private static String TAG;
    AlertDialog.Builder dialog;
    private Button ButtonRegister;
    private EditText EditTextEmail;
    private EditText EditTextPassword;
    private EditText EditTextConfirmPassword;
    private EditText EditTextPhone;
    private android.app.ProgressDialog ProgressDialog;
    private FirebaseAuth mAuth;

    private AlertDialog d;

    private Primary_User user;

    private SharedPreferences shared_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButtonRegister = findViewById(R.id.buttonRegister);

        EditTextEmail = findViewById(R.id.editTextEmail);
        EditTextPassword = findViewById(R.id.editTextPassword);
        EditTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        EditTextPhone = findViewById(R.id.editTextPhone);

        ProgressDialog = new ProgressDialog(this);

        dialog = new AlertDialog.Builder(Register_Activity.this);
        dialog.setCancelable(true);
        dialog.setMessage("Account with given email ID already exists. Would you like to ");
        dialog.setPositiveButton("Reset Password?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Register_Activity.this, Forgot_Password_Activity.class));
            }
        });
        dialog.setNegativeButton("Log in?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Register_Activity.this, Sign_In_Activity.class));
            }
        });

        ButtonRegister.setOnClickListener(this);

        shared_pref = getSharedPreferences("Current_User", MODE_PRIVATE);
    }

    private void Register_User() {
        requestSmsPermission();
        final String email = EditTextEmail.getText().toString().trim();
        final String password = EditTextPassword.getText().toString().trim();
        final String Confirmpassword = EditTextConfirmPassword.getText().toString().trim();
        final String contact = EditTextPhone.getText().toString().trim();
        String phone = contact;

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

        if (phone.startsWith("00"))
            phone = "+" + phone.substring(2);
        else if (phone.startsWith("05"))
            phone = "+971" + phone.substring(1);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(Register_Activity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    mAuth.getCurrentUser().sendEmailVerification();
                    user = new Primary_User(email, contact, password);
                    user.getP_verification().sendVerificationtext();
                    Log.d("FirebaseAuth", "User created with UID: " + mAuth.getCurrentUser().getUid());
                    user.Register(Register_Activity.this);

                } else {

                    Toast.makeText(Register_Activity.this, "Registration Unsuccessful!", Toast.LENGTH_SHORT).show();
                    if (!isOnline())
                        Toast.makeText(Register_Activity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        db.collection("Users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.getResult() == null) {
                                    d = dialog.create();
                                    d.show();
                                }
                            }
                        });
                    }
                    Log.d("FirebaseAuth", "User not created.");
                }
            }
        });

    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return (ni != null && ni.isConnected());
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
            DatabaseHelper.writeSharedPreference(shared_pref, user);
        }

        if (d != null)
            d.dismiss();

        super.onPause();
    }

    @Override
    public void UpdateUI(boolean res) {
        if (res) {
            Toast.makeText(this, "Document Snapshot added to the Collection!", Toast.LENGTH_SHORT)
                    .show();

            startActivity(new Intent(this, Verify_phone_activity.class));
        } else {
            Toast.makeText(this, "Failed to add document snapshot to collections", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void DisplayContactRequests(ArrayList<String> req_emails) {
        //DO NOTHING
    }

    @Override
    public void DisplayCurrentContacts(ArrayList<Contact> contacts) {
        //DO NOTHING
    }

    @Override
    public void onClick(View v) {
        Register_User();
    }


}
