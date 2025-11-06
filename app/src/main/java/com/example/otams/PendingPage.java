package com.example.otams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
/**
 * PendingPage
 *
 * Activity displayed when a user's registration request is pending administrative approval.
 * Informs the user that their account has been successfully created but has not yet been reviewed.
 *
 * Layout file: {@code activity_pending_screen.xml}
 */
public class
PendingPage extends AppCompatActivity {
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_screen);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PendingPage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });
	}
}
