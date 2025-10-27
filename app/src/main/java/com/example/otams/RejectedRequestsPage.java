package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RejectedRequestsPage extends AppCompatActivity {
    TextView welcomeText;
    Button registrationRequestsButton;
    ListView rejectedRequestsList;
    FirebaseFirestore db;

    private List<Map<String, Object>> rejectedUsers = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Ensures this layout contains rejectedRequestsList
        setContentView(R.layout.activity_inbox_rejection_request_page);

        db = FirebaseFirestore.getInstance();
        registrationRequestsButton = findViewById(R.id.Move2RegReq);
        rejectedRequestsList = findViewById(R.id.rejectedRequestsList);
        welcomeText = findViewById(R.id.welcomeText);

        welcomeText.setText("Previously Rejected User Registrations");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        rejectedRequestsList.setAdapter(adapter);


        //Navigation back to the Pending Requests (AdministratorPage)
        registrationRequestsButton.setOnClickListener(v -> {
            Intent regRequest = new Intent(RejectedRequestsPage.this, AdministratorPage.class);
            startActivity(regRequest);
            finish();
        });
    }

        private void fetchRejectedRequests() {
            db.collection("users")
                    // Filter by the REJECTED status
                    .whereEqualTo("status", "REJECTED")
                    .get()
                    .addOnCompleteListener(task -> {
                        rejectedUsers.clear();
                        adapter.clear();

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> userData = document.getData();
                                userData.put("uid", document.getId());
                                rejectedUsers.add(userData);

                                String displayLine = userData.get("email") + " (" + userData.get("role") + ")";
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
