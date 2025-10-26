package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdministratorPage extends AppCompatActivity {
    TextView welcomeText;
    Button logoutButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        mAuth = FirebaseAuth.getInstance();
        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);

        // Display welcome message
        if (mAuth.getCurrentUser() != null) {
            welcomeText.setText("Welcome, Administrator!");
        }

        // Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            finish();
        });
    }
}


