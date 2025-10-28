package com.example.otams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginPage extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.btn_login);
        textView = findViewById(R.id.registerNow);


        textView.setOnClickListener(view -> {
            startActivity(new Intent(LoginPage.this, RegisterPage.class));
            finish();
        });

        buttonLogin.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            // Admin login
            if (email.equals("admin@example.com") && password.equals("admin123")) {
                Toast.makeText(this, "Logged in as Administrator", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginPage.this, AdministratorPage.class));
                finish();
                return;
            }

            Log.d("LoginPage", "Attempting to sign in with email: " + email);
            // Firebase login for Student/Tutor
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            checkUserStatusAndRedirect(userId);
                        } else {
                            Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });
        });
    }

    private void checkUserStatusAndRedirect(String userId) {
        Log.d("LoginFlow", "Querying 'requests' collection for user with userId: " + userId);
        // Query the 'requests' collection to find the document where the 'userId' field matches.
        db.collection("requests")
                .whereEqualTo("userId", userId)
                .limit(1) // We only expect one document per user.
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // This block executes if the database query itself was successful.

                    if (!querySnapshot.isEmpty()) {
                        // Request document for this user was found.
                        String status = querySnapshot.getDocuments().get(0).getString("status");
                        Log.d("LoginFlow", "Request document found. User status is: " + status);

                        // Check the value of the 'status' field to decide where to send the user.
                        if ("approved".equals(status)) {
                            // The user's registration was approved by the administrator.
                            // Get the role from the same document to send them to the correct home screen.
                            String role = querySnapshot.getDocuments().get(0).getString("role");
                            Log.d("LoginFlow", "User is approved. Role is: " + role);
                            Toast.makeText(this, "Login Successful. Welcome!", Toast.LENGTH_SHORT).show();

                            if ("Student".equals(role)) {
                                startActivity(new Intent(LoginPage.this, StudentHome.class));
                            } else if ("Tutor".equals(role)) {
                                startActivity(new Intent(LoginPage.this, TutorHome.class));
                            }
                            finish(); // Finish LoginPage so the user cannot go back to it.

                        } else if ("rejected".equals(status)) {
                            // User's registration was rejected.
                            Log.d("LoginFlow", "User is rejected. Redirecting to RejectedPage.");
                            startActivity(new Intent(LoginPage.this, RejectedPage.class));
                            finish();

                        } else {
                            // Status is "pending" or another unexpected value.
                            Log.d("LoginFlow", "User is pending. Redirecting to PendingPage.");
                            startActivity(new Intent(LoginPage.this, PendingPage.class));
                            finish();
                        }
                    } else {
                        // User exists in Firebase Authentication, but has no request document.
                        // Can happen if they created an account but never completed the profile setup form.
                        Log.e("LoginFlow", "No request document found for user: " + userId);
                        Toast.makeText(this, "Profile setup is not complete. Please finish your registration.", Toast.LENGTH_LONG).show();
                        // You could optionally sign them out here or send them back to the RegisterPage.
                    }
                })
                .addOnFailureListener(e -> {
                    // Block executes if the database query fails (e.g., no internet, permissions error).
                    Log.e("LoginFlow", "Firestore query to 'requests' collection failed for user: " + userId, e);
                    Toast.makeText(this, "Error checking account status: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}





