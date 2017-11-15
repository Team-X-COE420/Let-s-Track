package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Start_up_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

        if(user==null)
            startActivity(new Intent(this,Sign_In_Activity.class));
        else
            startActivity(new Intent(this,Home_Screen_Activity.class));
        finish();
    }

}
