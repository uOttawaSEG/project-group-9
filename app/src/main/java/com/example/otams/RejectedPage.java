package com.example.otams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
/**
 * RejectedPage
 *
 * Activity displayed when a user's registration request has been rejected.
 * Provides a simple interface with a "Back" button that redirects the user
 * to the {@link LoginPage}.
 *
 */
public class RejectedPage extends AppCompatActivity {

    /**
     * Lifecycle method called when the activity is created.
     * Initializes the layout and sets up the Back button to return to the login page.
     *
     * @param savedInstanceState previously saved instance state (unused)
     */
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_screen);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RejectedPage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });
    }
}