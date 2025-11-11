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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Handles the user login process for all user roles (Administrator, Student, Tutor).
 * This activity manages UI input, manages authentication to Firebase,
 * and determines the user's registration status and role for redirection.
 *
 * co-author Sophia Hopkins
 */

public class LoginPage extends AppCompatActivity {
    //---UI ELEMENTS---
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonLogin;
    ProgressBar progressBar;
    TextView textView;

    //---FIREBASE COMPONENTS---
    FirebaseAuth mAuth; //Authentication instance
    FirebaseFirestore db; // Database instance


    /**
     *Called when the activity is first created. Initializes UI components and Firebase Instances.
     * @param savedInstanceState Contains data most recently supplied in onSaveInstanceState
     */
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


        buttonLogin.setOnClickListener(view -> attemptLogin());
    }

        private void attemptLogin(){
            progressBar.setVisibility(View.VISIBLE);
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();


            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }



            if (email.equals("admin@example.com") && password.equals("admin123")) {
                Toast.makeText(this, "Logged in as Administrator", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginPage.this, AdministratorPage.class));
                finish();
                return;
            }

            Log.d("LoginPage", "Attempting to sign in with email: " + email);


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
        }


    /**
     * Questions the Firestore 'requests' collection using the authenticated userId to find
     * the user's registration statues (approved, rejected, pending) and role, then redirects.
     *
     * @param userId the UID of the authenticated user for Firebase Auth.
     */
    private void checkUserStatusAndRedirect(String userId) {
        Log.d("LoginFlow", "Querying 'requests' collection for user with userId: " + userId);

        db.collection("requests")
                .whereEqualTo("userId", userId)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {


                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);

                        String status = doc.getString("status");
                        String role = doc.getString("role");
                        String email = mAuth.getCurrentUser().getEmail();


                        final User loggedInUser;
                        if ("Student".equals(role)) {
                            loggedInUser = new Student(email);
                        }else if ("Tutor".equals(role)) {
                            loggedInUser = new Tutor(email);
                        }else {
                            loggedInUser = new User(email) {
                            };
                        }


                        loggedInUser.setRole(role);
                        loggedInUser.setStatus(status);

                        if ("approved".equals(loggedInUser.getStatus())) {
                            Toast.makeText(this, "Login Successful. Welcome!", Toast.LENGTH_SHORT).show();

                            Intent intent;
                            if (loggedInUser instanceof Student) {
                                intent = new Intent(LoginPage.this, StudentHome.class);
                            } else { // Must be Tutor
                                intent = new Intent(LoginPage.this, TutorHome.class);
                            }

                            startActivity(intent);
                            finish();

                        } else if ("rejected".equals(loggedInUser.getStatus())) {
                            startActivity(new Intent(LoginPage.this, RejectedPage.class));
                            finish();

                        } else {
                            startActivity(new Intent(LoginPage.this, PendingPage.class));
                            finish();
                        }
                    } else {
                        Log.e("LoginFlow", "No request document found for user: " + userId);
                        Toast.makeText(this, "Profile setup is not complete. Please finish your registration.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LoginFlow", "Firestore query failed for user: " + userId, e);
                    Toast.makeText(this, "Error checking account status: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}





