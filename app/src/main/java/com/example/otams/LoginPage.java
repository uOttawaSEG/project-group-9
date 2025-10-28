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
                            autoRedirectUser(userId);
                        } else {
                            Toast.makeText(this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }

                    });
        });
    }

    private void autoRedirectUser(String userId) {
        Log.d("AutoRedirect", "Querying 'mail' collection for user with uid: " + userId);
        // Query the 'mail' collection to find the document where the 'uid' field matches.
        db.collection("mail")
                .whereEqualTo("templateData.uid", userId)
                .limit(1) // We only expect one document per user
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Get the first (and only) document from the result
                        String role = querySnapshot.getDocuments().get(0).getString("templateData.role");
                        Log.d("AutoRedirect", "Document found. User role is: " + role);

                        if (role != null) {
                            Toast.makeText(this, "Logged in as " + role, Toast.LENGTH_SHORT).show();
                            if ("Student".equals(role)) {
                                startActivity(new Intent(LoginPage.this, StudentHome.class));
                            } else if ("Tutor".equals(role)) {
                                startActivity(new Intent(LoginPage.this, TutorHome.class));
                            }
                            finish();
                        } else {
                            Log.e("AutoRedirect", "Role is null in document for user: " + userId);
                            Toast.makeText(this, "Your profile is incomplete. Please contact support.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // This is where your current logic is failing. No document was found with that uid.
                        Log.e("AutoRedirect", "No document in 'mail' collection has uid: " + userId);
                        Toast.makeText(this, "User data not found. Please contact support.", Toast.LENGTH_LONG).show();
                        // Optional: You could call checkUserRequest(userId) here as a fallback.
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AutoRedirect", "Firestore query failed for user: " + userId, e);
                    Toast.makeText(this, "Error fetching role: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }


    private void checkUserRequest(String id) {
        db.collection("requests")
                .whereEqualTo("userId", id)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    if (!querySnapshot.isEmpty()) {
                        String status = querySnapshot.getDocuments().get(0).getString("status");
                        String role = querySnapshot.getDocuments().get(0).getString("role");

                        if ("approved".equals(status)) {
                            startActivity(new Intent(LoginPage.this, ApprovedPage.class));
                            finish();
                        } else if ("rejected".equals(status)) {
                            startActivity(new Intent(LoginPage.this, RejectedPage.class));
                            finish();
                        } else {
                            startActivity(new Intent(LoginPage.this, PendingPage.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Please complete your profile", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPage.this, LandingPage.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking request status: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}





