package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Change_Password_Activity extends Activity implements View.OnClickListener {

    EditText oldpass, newpassword, confirm_newpass;

    Button submit;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldpass = (EditText) findViewById(R.id.editTextCurrentPassword);
        newpassword = (EditText) findViewById(R.id.editTextNewPassword);
        confirm_newpass = (EditText) findViewById(R.id.editTextConfirmNewPassword);

        submit = (Button) findViewById(R.id.buttonConfirm);

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        change_password();
    }

    private void change_password() {
        String current = oldpass.getText().toString().trim();
        final String newpass = newpassword.getText().toString().trim();
        String confirmnewpass = confirm_newpass.getText().toString().trim();


        if (TextUtils.isEmpty(newpass)) {
            //password field is empty
            Toast.makeText(this, "Please enter the new password", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(current)) {
            //password field is empty
            Toast.makeText(this, "Please enter your current password", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(confirmnewpass) || !confirmnewpass.equals(newpass)) {
            //Confirm password field is empty
            Toast.makeText(this, "Passwords do not match! Try again", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mAuth = FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            String email = user.getEmail();
            AuthCredential cred = EmailAuthProvider.getCredential(email, current);
            user.reauthenticate(cred).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newpass);
                        Toast.makeText(Change_Password_Activity.this, "Password change successful!", Toast.LENGTH_SHORT).show();
                        Change_Password_Activity.this.finish();
                    } else
                        Toast.makeText(Change_Password_Activity.this, "Password change unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
