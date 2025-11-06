package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * RejectedRequestsPage
 *
 * Activity displaying a list of previously rejected user registration requests
 * (both students and tutors). The data is retrieved from the Firestore
 * "requests" collection, filtered by entries with a "rejected" status.
 *
 */
public class RejectedRequestsPage extends AppCompatActivity {
    TextView welcomeText;
    Button registrationRequestsButton;
    Button logoutButton;
    Button rejectedRequestsButton;
    ListView rejectedRequestsList;
    FirebaseFirestore db;

    private List<String> rejectedUsers = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    /**
     * Lifecycle method called when the activity is first created.
     * Initializes Firestore, UI components, and sets up click listeners.
     *
     * @param savedInstanceState previous saved state (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_rejection_request_page);

        db = FirebaseFirestore.getInstance();
        registrationRequestsButton = findViewById(R.id.Move2RegReq);
        rejectedRequestsButton = findViewById(R.id.Move2RejectReq);
        rejectedRequestsList = findViewById(R.id.rejectedRequestsList);
        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);

        welcomeText.setText("Previously Rejected User Registrations");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rejectedUsers);
        rejectedRequestsList.setAdapter(adapter);


        //Navigation back to the Pending Requests (AdministratorPage)
        registrationRequestsButton.setOnClickListener(v -> {
            Intent regRequest = new Intent(RejectedRequestsPage.this, AdministratorPage.class);
            startActivity(regRequest);
            finish();
        });

        rejectedRequestsButton.setOnClickListener(v -> {
            fetchRejectedRequests();
        });

        logoutButton.setOnClickListener(v -> {
            Toast.makeText(this, "Admin logged out of instance.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RejectedRequestsPage.this, LoginPage.class));
            finish();
        });

        fetchRejectedRequests();
    }

    /**
     * Fetches all registration requests with a "rejected" status
     * from the Firestore "requests" collection and updates the ListView.
     */
    private void fetchRejectedRequests() {
        db.collection("requests")
                .whereEqualTo("status", "rejected")
                .get()
                .addOnCompleteListener(task -> {
                    rejectedUsers.clear();
                    adapter.clear();

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            RegistrationRequest request = document.toObject(RegistrationRequest.class);

                            String displayLine = request.getEmail() + " (" + request.getRole() + ")";
                            adapter.add(displayLine);
                        }

                        if (rejectedUsers.isEmpty()) {
                            adapter.add("No previously rejected registration requests found.");
                        }

                    } else {
                        Toast.makeText(this, "Error fetching rejected requests: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        adapter.add("Failed to load requests.");
                    }

                    adapter.notifyDataSetChanged();
                });
        }
    }
