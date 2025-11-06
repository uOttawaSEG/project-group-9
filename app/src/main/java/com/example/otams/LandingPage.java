package com.example.otams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
/**
 * LandingPage
 *
 * The first screen displayed when the app is launched.
 * Acts as a simple welcome or introductory page with a "Get Started" button that
 * navigates the user to the {@link LoginPage}.
 *
 * Layout file: {@code activity_landing.xml}
 *
 */
public class LandingPage extends AppCompatActivity {

    Button btnGetStarted;

    /**
     * Lifecycle method called when the activity is created.
     * Initializes layout and sets up navigation logic for the "Get Started" button.
     *
     * @param savedInstanceState previously saved instance state (unused)
     */
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