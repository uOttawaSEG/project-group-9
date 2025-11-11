package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import java.util.Arrays;
import java.util.List;


public class TutorRegistrationPage extends AppCompatActivity {
    private TextInputEditText editTextFirstName, editTextLastName, editTextNumber, editTextDegree, editTextCourses;
    private Button signUp, logOut;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;

    /**
     * Android lifecycle - called when the Activity is created.
     * Initializes Firebase, resolves the current user, wires up the UI,
     * and attaches click listeners to buttons.
     *
     * @param savedInstanceState previous state (unused here)
     */
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

    /**
     * Builds a RegistrationRequest model and writes it to the "requests" collection.
     * On success, updates the document with its generated ID ("requestId"),
     * sends a confirmation email trigger, and navigates to PendingPage.
     *
     * @param uid         Firebase Auth user ID
     * @param firstName   tutor first name
     * @param lastName    tutor last name
     * @param phoneNumber contact number
     * @param email       user email (from FirebaseUser)
     * @param degree      academic degree
     * @param courses     list of courses the tutor can teach
     */
    private void createRequest(String uid, String firstName, String lastName, String phoneNumber, String email, String degree, List<String> courses){
        RegistrationRequest request = new RegistrationRequest();
        request.setUserId(uid);
        request.setRole("Tutor");
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setEmail(email);
        request.setPhoneNumber(phoneNumber);
        request.setDegree(degree);
        request.setCourses(courses);
        request.setStatus("pending");
        //request.setTimestamp(Timestamp.now());

        db.collection("requests")
                .add(request)
                .addOnSuccessListener(docRef -> {
                    String reqId = docRef.getId();
                    docRef.update("requestId",reqId)
                        .addOnSuccessListener(aVoid -> {
                            sendConfirmation(email, "Tutor");
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(TutorRegistrationPage.this, PendingPage.class));
                            finish();
                        });
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    toastLong("Failed to submit: " + e.getMessage());
                });
    }


    /**
     * Writes a document to the "mail" collection to trigger an email.
     * The exact fields required depend on your email-sending mechanism
     * (e.g., Firebase Extensions vs. custom Cloud Function).
     *
     * @param email recipient email address
     * @param role  role label to inject into the template (e.g., "Tutor")
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
     * Utility helper to safely extract trimmed text from a TextInputEditText.
     *
     * @param input the TextInputEditText field
     * @return trimmed string value; empty if null
     */
    private String text(TextInputEditText input){
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
    /**
     * Short toast message helper.
     *
     * @param msg message to display
     */
    private void toast(String msg) {
        Toast.makeText(TutorRegistrationPage.this, msg, Toast.LENGTH_SHORT).show();
    }
    /**
     * Long toast message helper.
     *
     * @param msg message to display
     */
    private void toastLong(String msg) {
        Toast.makeText(TutorRegistrationPage.this, msg, Toast.LENGTH_LONG).show();
    }
}