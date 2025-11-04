package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;


public class StudentRegistrationPage extends AppCompatActivity {
    private TextInputEditText editTextFirstName, editTextLastName, editTextNumber, editTextProgram;
    private String email;
    private Button signUp, logOut;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_registration);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null){
        	toast("No user signed in. Login.");
        	startActivity(new Intent(StudentRegistrationPage.this, LoginPage.class));
        	finish();
        	return;
        }

        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextNumber = findViewById(R.id.phoneNum);
        editTextProgram = findViewById(R.id.programOfStudy);

        signUp = findViewById(R.id.signUpButton);
        logOut = findViewById(R.id.logoutButton);
        progressBar = findViewById(R.id.progressBar);


        signUp.setOnClickListener(view -> {
            String firstName = text(editTextFirstName);
            String lastName = text(editTextLastName);
            String number = text(editTextNumber);
            String email = user.getEmail();
            String program = text(editTextProgram);

            if(firstName.isEmpty() || lastName.isEmpty() || number.isEmpty()|| email.isEmpty()|| program.isEmpty()){
                toast("Fill in all fields.");
                return;
            }

            String uid = user.getUid();
            
            progressBar.setVisibility(View.VISIBLE);

            createRequest(uid, firstName, lastName, number, email, program);
        });

        logOut.setOnClickListener(view -> {
        	mAuth.signOut();
        	toast("Log out successful.");
        	startActivity(new Intent(StudentRegistrationPage.this, LoginPage.class));
        	finish();
        });
    }

    private void createRequest(String uid, String firstName, String lastName, String phoneNumber, String email, String program){
    	RegistrationRequest request = new RegistrationRequest();
    	request.setUserId(uid);
        request.setRole("Student");
    	request.setFirstName(firstName);
    	request.setLastName(lastName);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setProgram(program);
    	request.setStatus("pending");
        //request.setTimestamp(Timestamp.now());

    	db.collection("requests")
    			.add(request)
    			.addOnSuccessListener(docRef -> {
                    String reqId = docRef.getId();
                    docRef.update("requestId",reqId)
                        .addOnSuccessListener(aVoid -> {
                            sendConfirmation(email, "Student");
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(StudentRegistrationPage.this, PendingPage.class));
                            finish();
                        });
                })
    			.addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    toastLong("Failed to submit: " + e.getMessage());
                });
    }


    private void sendConfirmation(String email, String role){
        Map<String, Object> mail = new HashMap<>();
        mail.put("to", email);

        Map<String, Object> template = new HashMap<>();
        template.put("role", role);
        template.put("displayEmail", email);

        mail.put("templateData", template);
        mail.put("createdAt", Timestamp.now());

        db.collection("mail")
                .add(mail)
                .addOnSuccessListener(docRef -> {
                    Log.d("RegisterFlow", "Successfully saved to 'mail' collection. Now redirecting to profile page.");
                    toast("Confirmation email sent.");
                })
                .addOnFailureListener(e -> {
                    Log.e("Email", "Failed to save user data: " + e.getMessage());
                });
    }


    private String text(TextInputEditText input){
    	return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private void toast(String msg) {
        Toast.makeText(StudentRegistrationPage.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void toastLong(String msg) {
        Toast.makeText(StudentRegistrationPage.this, msg, Toast.LENGTH_LONG).show();
    }
}

