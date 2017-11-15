package com.example.teamx.letstrack.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home_Screen_Activity extends Activity implements View.OnClickListener {

    Button settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        settings = (Button) findViewById(R.id.buttonSettings);

        settings.setOnClickListener(this);
    }

    void displayemailverified() {

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, Settings_Activity.class));
    }
}
