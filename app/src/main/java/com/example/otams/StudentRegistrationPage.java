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

/**
 * StudentRegistrationPage
 *
 * Activity that handles student registration by collecting basic user details
 * (first name, last name, phone number, program of study) from input fields,
 * validating the data, and submitting it as a pending {@link RegistrationRequest}
 * document to Firestore.
 *
 * Flow:
 *  - Verifies that a Firebase user is currently signed in.
 *  - On "Sign Up": validates all required fields, creates and submits a Firestore request.
 *  - On success: updates the document ID, triggers confirmation email, and redirects to {@link PendingPage}.
 *  - On "Log Out": signs the user out and navigates back to {@link LoginPage}.
 */
public class StudentRegistrationPage extends AppCompatActivity {
    private TextInputEditText editTextFirstName, editTextLastName, editTextNumber, editTextProgram;
    private String email;
    private Button signUp, logOut;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    /**
     * Lifecycle method called when the activity is created.
     * Initializes Firebase instances, checks for authenticated user,
     * and sets up UI bindings and click listeners.
     *
     * @param savedInstanceState previous saved state (unused)
     */
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
    /**
     * Creates a new {@link RegistrationRequest} object for the student
     * and adds it to the "requests" collection in Firestore.
     * Upon success, updates the document with its generated ID and triggers an email confirmation.
     *
     * @param uid         Firebase user ID
     * @param firstName   student's first name
     * @param lastName    student's last name
     * @param phoneNumber student's phone number
     * @param email       student's email (from FirebaseAuth)
     * @param program     student's program of study
     */
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

    /**
     * Adds a document to the "mail" collection to trigger a confirmation email.
     * Works with Firebase Extensions or backend listeners that handle outgoing emails.
     *
     * @param email recipient email address
     * @param role  user role for template substitution
     */
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

    /**
     * Utility method that safely retrieves trimmed text from a TextInputEditText.
     *
     * @param input TextInputEditText field
     * @return trimmed text value (empty string if null)
     */
    private String text(TextInputEditText input){
    	return input.getText() == null ? "" : input.getText().toString().trim();
    }

    /**
     * Displays a short Toast message.
     *
     * @param msg message to display
     */
    private void toast(String msg) {
        Toast.makeText(StudentRegistrationPage.this, msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * Displays a long Toast message.
     *
     * @param msg message to display
     */
    private void toastLong(String msg) {
        Toast.makeText(StudentRegistrationPage.this, msg, Toast.LENGTH_LONG).show();
    }
}

