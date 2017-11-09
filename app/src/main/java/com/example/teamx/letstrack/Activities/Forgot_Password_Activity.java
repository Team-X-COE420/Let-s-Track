package com.example.teamx.letstrack.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Activity extends AppCompatActivity implements View.OnClickListener{

    private Button ButtonResetPassword;

    private EditText EditTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ButtonResetPassword = (Button) findViewById(R.id.buttonResetPassword);
        EditTextEmail = (EditText) findViewById(R.id.editTextEmail);

        ButtonResetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ResetPassword();
    }

    private void ResetPassword()
    {
        String email = EditTextEmail.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            //email field is empty
            Toast.makeText(this,"Please enter an email ID", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,"Please enter a valid email ID", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail("email").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Forgot_Password_Activity.this,"Password Reset mail sent",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Forgot_Password_Activity.this,Sign_In_Activity.class));
                }
                else
                {
                    Toast.makeText(Forgot_Password_Activity.this,"Password Reset mail sent",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
