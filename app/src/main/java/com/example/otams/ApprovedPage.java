package com.example.otams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
/**
 * ApprovedPage
 *
 * Activity displayed when a user's registration request has been approved.
 * Informs the user that their account has been successfully verified by an administrator.
 * Includes a "Back" button to return to the {@link LoginPage} for normal login access.
 *
 * Layout file: {@code activity_approved_screen.xml}
 *
 */
public class ApprovedPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_screen);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ApprovedPage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });
    }
}