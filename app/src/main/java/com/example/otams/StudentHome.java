package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;
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


public class StudentHome extends AppCompatActivity {
    private TextInputEditText editTextFirstName, editTextLastName, editTextNumber, editTextProgram;
    private Button signUp, logOut;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null){
        	toast("No user signed in. Login.");
        	startActivity(new Intent(StudentHome.this, LoginPage.class));
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


        signUp.setOnClickListener(view -> saveUserInfo());

        logOut.setOnClickListener(view -> {
        	mAuth.signOut();
        	toast("Log out successful.");
        	startActivity(new Intent(StudentHome.this, LoginPage.class));
        	finish();
        });
    }

    private void saveUserInfo(){
    	String firstName = text(editTextFirstName);
    	String lastName = text(editTextLastName);
    	String number = text(editTextNumber);
    	String program = text(editTextProgram);

    	if(firstName.isEmpty() || lastName.isEmpty() || number.isEmpty()|| program.isEmpty()){
    		toast("Fill in all fields.");
    		return;
    	}

    	progressBar.setVisibility(View.VISIBLE);

    	String uid = user.getUid();

    	HashMap<String,Object> userUpdates = new HashMap<>();
    	userUpdates.put("firstName", firstName);
    	userUpdates.put("lastName", lastName);
    	userUpdates.put("phoneNumber", number);
    	userUpdates.put("program", program);
        userUpdates.put("role", "Student");
    	userUpdates.put("profileCompleteAt", Timestamp.now());

    	
    	db.collection("users").document(uid)
    			.update(userUpdates)
    			.addOnSuccessListener(unused -> {
    				progressBar.setVisibility(View.GONE);
    				toast("Profile creation succesful");
    				createRequest(uid, firstName, lastName, email. number, program);
    			})
    			.addOnFailureListener(e -> {
    				progressBar.setVisibility(View.GONE);
    				toastLong("Failed to create profile: " + e.getMessage());
    			});
    }

    private void createRequest(String uid, String firstName, String lastName, String phoneNumber, String email, String program){
    	Map<String,Object> request = new HashMap<>();
    	request.put("userId", uid);
        request.put("role", "Student");
    	request.put("firstName", firstName);
    	request.put("lastName", lastName);
        request.put("email", email);
        request.put("phoneNumber", phoneNumber);
        request.put("program", program);
    	request.put("timestamp", Timestamp.now());
    	request.put("approved", false);

    	db.collection("requests")
    			.add(request)
    			.addOnSuccessListener(docRef -> toast("Request submitted."))
    			.addOnFailureListener(e -> toastLong("Failed to submit: " + e.getMessage()))
    }


    private String text(TextInputEditText input){
    	return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private void toast(String msg) {
        Toast.makeText(StudentHome.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void toastLong(String msg) {
        Toast.makeText(StudentHome.this, msg, Toast.LENGTH_LONG).show();
    }
}

