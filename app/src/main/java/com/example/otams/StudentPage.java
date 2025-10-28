package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.auth.FirebaseAuth;

public class StudentPage extends AppCompatActivity {
    TextView welcomeText;
    Button logoutButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        logoutButton = findViewById(R.id.logoutButton);
        mAuth=FirebaseAuth.getInstance();

        // Display welcome message
        if (mAuth.getCurrentUser() == null) {
            startActivity(new Intent(this,LoginPage.class));
            finish();
            return;
        }

        welcomeText.setText("Welcome, Student!");
        // Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            finish();
        });
    }
}