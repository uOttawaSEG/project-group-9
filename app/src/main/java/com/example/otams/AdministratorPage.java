package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdministratorPage extends AppCompatActivity {

    // Member variables for Firebase and Views
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private Button rejectedRequestsButton;
    private Button logoutButton;
    private TextView welcomeText;
    // ... (Your Adapter and Request data structures)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home); // Ensure this XML is correct

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(AdministratorPage.this, LoginPage.class));
            finish();
            return;
        }

        // Initialize Views using the CORRECT IDs from XML
        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);

        // Use the standardized names:
        rejectedRequestsButton = findViewById(R.id.button3);
       //(works with the version i sent lauren of the activity_administrator_home.xml  recyclerView = findViewById(R.id.recyclerRequests);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // adapter = new RequestsAdapter(this, requestsList); // Assuming you have an Adapter
        // recyclerView.setAdapter(adapter);

        // Set Welcome Message
        welcomeText.setText("Welcome, Administrator!");

        // Set Listeners
        rejectedRequestsButton.setOnClickListener(view -> {
            startActivity(new Intent(AdministratorPage.this, RejectedRequestsPage.class));
        });

        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(this, "Administrator logged out.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdministratorPage.this, LoginPage.class));
            finish();
        });

        // FetchRequests(); // Assuming you have a method to load data
    }
}