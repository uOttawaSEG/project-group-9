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
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        radioGroupRole = findViewById(R.id.radioGroupRole);
        radioStudent = findViewById(R.id.radioStudent);
        radioTutor = findViewById(R.id.radioTutor);

        buttonReg.setOnClickListener(view -> {
            progressBar.setVisibility(View.VISIBLE);
            buttonReg.setEnabled(false);

            String email = editTextEmail.getText() == null ? "" : editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText() == null ? "" : editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                    || radioGroupRole.getCheckedRadioButtonId() == -1) {
                toast("Enter email, password, and select a role.");
                progressBar.setVisibility(View.GONE);
                buttonReg.setEnabled(true);
                return;
            }

            final String role = (radioGroupRole.getCheckedRadioButtonId() == R.id.radioStudent)
                    ? "Student" : "Tutor";

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Map<String, Object> mail = new HashMap<>();
                            mail.put("to", email);

                            Map<String, Object> templateData = new HashMap<>();
                            templateData.put("role", role);
                            templateData.put("displayEmail", email);
                            mail.put("templateData", templateData);
                            mail.put("uid", user.getUid());
                            mail.put("createdAt", Timestamp.now());

                            db.collection("mail")
                                    .add(mail)
                                    .addOnSuccessListener(ref -> {
                                        ref.get().addOnCompleteListener(getTask -> {
                                            if (getTask.isSuccessful() && getTask.getResult() != null && getTask.getResult().exists()) {
                                                toast("Confirmation email sent.");
                                            } else {
                                                toast("Failed to send confirmation email.");
                                            }
                                            mAuth.signOut();
                                            startActivity(new Intent(RegisterPage.this, LoginPage.class));
                                            finish();
                                        });
                                    })
                                    .addOnFailureListener(e -> {
                                        toastLong("Failed to queue email: " + e.getMessage());
                                        progressBar.setVisibility(View.GONE);
                                        buttonReg.setEnabled(true);
                                    });
                        } else {
                            toastLong("Account created, but user is null.");
                            progressBar.setVisibility(View.GONE);
                            buttonReg.setEnabled(true);
                        }
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