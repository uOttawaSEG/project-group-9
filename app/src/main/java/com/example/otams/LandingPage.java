package com.example.otams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LandingPage extends AppCompatActivity {

    Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        btnGetStarted = findViewById(R.id.getStarted);

        btnGetStarted.setOnClickListener(view -> {
            Intent intent = new Intent(LandingPage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });
    }
}