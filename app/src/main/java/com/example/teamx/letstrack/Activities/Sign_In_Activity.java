package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamx.letstrack.Application.Primary_User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_In_Activity extends Activity implements View.OnClickListener {

    private Button ButtonSignIn;

    private EditText EditTextEmail;
    private EditText EditTextPassword;

    private TextView TextViewRegister;
    private TextView TextViewForgotPassword;

    private android.app.ProgressDialog ProgressDialog;

    private SharedPreferences shared_pref;

    private Primary_User current_user;

    private static String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        ButtonSignIn=(Button) findViewById(R.id.buttonSignIn);

        EditTextEmail=(EditText)findViewById(R.id.editTextEmail);
        EditTextPassword=(EditText)findViewById(R.id.editTextPassword);

        TextViewRegister=(TextView)findViewById(R.id.textViewSignUp);
        TextViewForgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);

        shared_pref=getSharedPreferences("Current_User",MODE_PRIVATE);

        ButtonSignIn.setOnClickListener(this);
        TextViewRegister.setOnClickListener(this);
        TextViewForgotPassword.setOnClickListener(this);

        ProgressDialog=new ProgressDialog(this);

        current_user = null;

    }

    private void Sign_in()
    {
        String email=EditTextEmail.getText().toString().trim();
        String password=EditTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            //email field is empty
            Toast.makeText(this,"Please enter an email ID", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(TextUtils.isEmpty(password)) {
            //password field is empty
            Toast.makeText(this,"Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog.setMessage("Signing In... ");
        ProgressDialog.show();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ProgressDialog.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(Sign_In_Activity.this,"Sign-in Successful",Toast.LENGTH_SHORT).show();
                    //TODO Retrieve User information from file/database
                    startActivity(new Intent(Sign_In_Activity.this,Home_Screen_Activity.class));
                    finish();
                }
                else
                    Toast.makeText(Sign_In_Activity.this,"Sign-in unsuccessful",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void onPause() {

        if (current_user != null) {
            //DatabaseHelper.writeSharedPreference(shared_pref,current_user);
            //DatabaseHelper.writetofile(Sign_In_Activity.this,current_user);
        }
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if(v==ButtonSignIn)
        {
            Sign_in();
        }
        else if(v==TextViewRegister)
        {
            startActivity(new Intent(this,Register_Activity.class));
        }
        else
            startActivity(new Intent(this,Forgot_Password_Activity.class));

    }
}
