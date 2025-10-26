package com.example.otams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
            Intent intent = new Intent(getApplicationContext(), LoginPage.class);
            startActivity(intent);
            finish();
        });

        buttonReg.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);

            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(RegisterPage.this, "Please enter email", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(RegisterPage.this, "Please enter password", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }

            int selectedId = radioGroupRole.getCheckedRadioButtonId();
            if (selectedId == -1) {
                Toast.makeText(RegisterPage.this, "Please select a role (Student or Tutor)", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            User user;

                            if (selectedId == R.id.radioStudent) {
                                user = new Student(email);
                                user.setRole("Student");
                            } else {
                                user = new Tutor(email);
                                user.setRole("Tutor");
                            }

                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterPage.this, "Account created", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(RegisterPage.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_LONG).show());
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                            if (error.contains("email address is already in use")) {
                                Toast.makeText(RegisterPage.this, "This email is already registered. Please log in.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterPage.this, "Authentication failed: " + error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        });
    }
}