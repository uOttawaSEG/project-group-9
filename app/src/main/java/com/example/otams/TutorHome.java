package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
/**
 * TutorHome
 *
 * Activity representing the tutor's home screen after successfully logging in or registering.
 * Currently provides a simple logout feature that signs the user out of Firebase Authentication
 * and redirects them back to the {@link LoginPage}
 *
 */
public class TutorHome extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     * Initializes the layout and sets up button event listeners.
     *
     * @param savedInstanceState previously saved instance state (unused here)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(TutorHome.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TutorHome.this, LoginPage.class));
            finish();
        });
    }
}