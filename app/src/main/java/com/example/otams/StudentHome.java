package com.example.otams;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
/**
 * StudentHome
 *
 * Activity representing the student's main home screen after a successful login or registration.
 * Currently includes a logout function that signs the user out of Firebase Authentication
 * and redirects them back to the {@link LoginPage}.
 *
 */
public class StudentHome extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     * Sets up the layout, initializes UI components, and handles user interactions.
     *
     * @param savedInstanceState previously saved instance state (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(StudentHome.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(StudentHome.this, LoginPage.class));
            finish();
        });
    }
}