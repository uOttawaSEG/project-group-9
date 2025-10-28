package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;


public class TutorRegistrationPage extends AppCompatActivity {
    private TextInputEditText editTextFirstName, editTextLastName, editTextNumber, editTextDegree, editTextCourses;
    private Button signUp, logOut;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_registration);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null){
            toast("No user signed in. Login.");
            startActivity(new Intent(TutorRegistrationPage.this, LoginPage.class));
            finish();
            return;
        }

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextNumber = findViewById(R.id.editTextPhone);
        editTextDegree = findViewById(R.id.editTextDegree);
        editTextCourses = findViewById(R.id.editTextCourses);

        signUp = findViewById(R.id.signUpButton);
        logOut = findViewById(R.id.logoutButton);
        progressBar = findViewById(R.id.progressBar);


        signUp.setOnClickListener(view -> {
            String firstName = text(editTextFirstName);
            String lastName = text(editTextLastName);
            String number = text(editTextNumber);
            String courses = text(editTextCourses);
            String degree = text(editTextDegree);
            String uid = user.getUid();
            String email = user.getEmail();

            if(firstName.isEmpty() || lastName.isEmpty() || number.isEmpty()|| email.isEmpty() || degree.isEmpty() || courses.isEmpty()){
                toast("Fill in all fields.");
                return;
            }

            List<String> coursesList = Arrays.asList(courses.split("\\s*,\\s*"));
            progressBar.setVisibility(View.VISIBLE);
            createRequest(uid,firstName,lastName,number,email,degree,coursesList);
        });

        logOut.setOnClickListener(view -> {
            mAuth.signOut();
            toast("Log out successful.");
            startActivity(new Intent(TutorRegistrationPage.this, LoginPage.class));
            finish();
        });
    }

    private void createRequest(String uid, String firstName, String lastName, String phoneNumber, String email, String degree, List<String> courses){
        Map<String,Object> request = new HashMap<>();
        request.put("userId", uid);
        request.put("role", "Tutor");
        request.put("firstName", firstName);
        request.put("lastName", lastName);
        request.put("email", email);
        request.put("phoneNumber", phoneNumber);
        request.put("degree", degree);
        request.put("courses", courses);
        request.put("timestamp", Timestamp.now());
        request.put("status", "pending");

        db.collection("requests")
                .add(request)
                .addOnSuccessListener(docRef -> {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(TutorRegistrationPage.this, PendingPage.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    toastLong("Failed to submit: " + e.getMessage());
                });
    }


    private String text(TextInputEditText input){
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private void toast(String msg) {
        Toast.makeText(TutorRegistrationPage.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void toastLong(String msg) {
        Toast.makeText(TutorRegistrationPage.this, msg, Toast.LENGTH_LONG).show();
    }
}