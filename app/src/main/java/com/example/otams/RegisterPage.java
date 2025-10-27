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
            progressBar.setVisibility(View.VISIBLE);

            String email = editTextEmail.getText() == null ? "" : editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText() == null ? "" : editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || radioGroupRole.getCheckedRadioButtonId() == -1) {
                toast("Enter email, password, and select a role.");
                progressBar.setVisibility(View.GONE);
                return;
            }

            int selectedId = radioGroupRole.getCheckedRadioButtonId();
            final String role = (selectedId == R.id.radioStudent) ? "Student" : "Tutor";

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            toastLong("Registration failed: " + task.getException().getMessage());
                            progressBar.setVisibility(View.GONE);
                            return;
                        }

                        String uid = mAuth.getCurrentUser().getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // 1. PRIMARY SAVE: Create user document in 'users' collection
                        Map<String, Object> userDoc = new HashMap<>();
                        userDoc.put("email", email);
                        userDoc.put("role", role);
                        userDoc.put("status", "PENDING"); // Initial status
                        userDoc.put("createdAt", Timestamp.now());

                        db.collection("users").document(uid)
                                .set(userDoc)
                                .addOnCompleteListener(setTask -> {
                                    if(setTask.isSuccessful()) {

                                        // 2. CRITICAL FIX: Create the document in the 'requests' collection
                                        Map<String, Object> requestDoc = new HashMap<>();
                                        requestDoc.put("uid", uid);
                                        requestDoc.put("email", email);
                                        requestDoc.put("role", role);
                                        requestDoc.put("status", "PENDING");
                                        requestDoc.put("requestDate", Timestamp.now());

                                        db.collection("requests").document(uid)
                                                .set(requestDoc); // This write creates the collection!

                                        progressBar.setVisibility(View.GONE);
                                        toast("Account created. Awaiting Administrator approval.");
                                        mAuth.signOut();
                                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                        finish();
                                    } else {
                                        toastLong("Database save failed: " + setTask.getException().getMessage());
                                        progressBar.setVisibility(View.GONE);
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