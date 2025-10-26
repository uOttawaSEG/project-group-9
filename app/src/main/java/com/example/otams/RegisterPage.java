package com.example.otams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;
    RadioGroup radioGroupRole;
    RadioButton radioStudent, radioTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        textView = findViewById(R.id.loginNow);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        radioStudent = findViewById(R.id.radioStudent);
        radioTutor = findViewById(R.id.radioTutor);

        textView.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            finish();
        });

        buttonReg.setOnClickListener(view -> {
            progressBar.setVisibility(android.view.View.VISIBLE);

            String email = editTextEmail.getText() == null ? "" : editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText() == null ? "" : editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                toast("Please enter email");
                progressBar.setVisibility(android.view.View.GONE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                toast("Please enter password");
                progressBar.setVisibility(android.view.View.GONE);
                return;
            }

            int selectedId = radioGroupRole.getCheckedRadioButtonId();
            if (selectedId == -1) {
                toast("Please select a role (Student or Tutor)");
                progressBar.setVisibility(android.view.View.GONE);
                return;
            }

            final String role = (selectedId == R.id.radioStudent) ? "Student" : "Tutor";

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(android.view.View.GONE);
                            String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            if (error != null && error.contains("email address is already in use")) {
                                toastLong("This email is already registered. Please log in.");
                            } else {
                                toastLong("Authentication failed: " + error);
                            }
                            return;
                        }

                        String uid = mAuth.getCurrentUser().getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Write a flat, explicit document
                        Map<String, Object> userDoc = new HashMap<>();
                        userDoc.put("email", email);
                        userDoc.put("role", role);
                        userDoc.put("createdAt", Timestamp.now());

                        db.collection("users").document(uid)
                                .set(userDoc)
                                .addOnCompleteListener(setTask -> {
                                    progressBar.setVisibility(android.view.View.GONE);
                                    if(setTask.isSuccessful()) {
                                        toast("Account created as " + role);
                                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                        finish();
                                    } else {
                                        if (setTask.getException() != null) {
                                            toastLong("Failed to save user data: " + setTask.getException().getMessage());
                                        } else {
                                            toastLong("Failed to save user data.");
                                        }
                                    }
                                });
                    });
        });
    }

    private void toast(String msg) {
        Toast.makeText(RegisterPage.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void toastLong(String msg) {
        Toast.makeText(RegisterPage.this, msg, Toast.LENGTH_LONG).show();
    }
}
