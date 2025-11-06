package com.example.otams;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Main
 *
 * Entry point of the application.
 * This activity checks whether a user is currently authenticated using Firebase Authentication.
 * If no user is signed in, it redirects to the {@link LoginPage}.
 * Otherwise, the app proceeds based on user authentication state (handled elsewhere).

 * Layout file: {@code activity_main.xml}
 *
 */
public class Main extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    /**
     * Called when the activity is first created.
     * Configures edge-to-edge display, initializes Firebase, and redirects if user is not logged in.
     *
     * @param savedInstanceState previously saved instance state (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            finish();
        }
    }
}