package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdministratorHome extends AppCompatActivity {
    TextView welcomeText;
    Button logoutButton, rejectedRequestsButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        mAuth = FirebaseAuth.getInstance();
        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);

        rejectedRequestsButton = findViewById(R.id.button3);

        rejectedRequestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdministratorHome.this, RejectedRequestsPage.class);
            startActivity(intent);
        });

        // Display welcome message
        if (mAuth.getCurrentUser() != null) {
            welcomeText.setText("Welcome, Administrator!");
        }

        rejectedRequestsButton.setOnClickListener(v -> {
            Intent rejectedButton = new Intent(AdministratorHome.this, RejectedRequestsPage.class);
            startActivity(rejectedButton);
        });

        // Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            finish();
        });
    }
}


