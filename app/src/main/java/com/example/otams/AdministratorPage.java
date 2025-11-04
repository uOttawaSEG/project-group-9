package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author
 */
public class AdministratorPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private Button registrationRequestsButton;
    private Button rejectedRequestsButton;
    private Button logoutButton;
    private TextView welcomeText;
    private TextView emptyStateText;

    private RequestsAdapter adapter;
    private List<RegistrationRequest> requestsList;
    private String currentView = "pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize Views
        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);
        registrationRequestsButton = findViewById(R.id.Move2);
        rejectedRequestsButton = findViewById(R.id.button3);
        recyclerView = findViewById(R.id.requestsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);

        // Initialize list
        requestsList = new ArrayList<>();

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RequestsAdapter(this, requestsList, new RequestsAdapter.OnRequestActionListener() {
            @Override
            public void onApprove(RegistrationRequest request) {
                approveRequest(request);
            }

            @Override
            public void onReject(RegistrationRequest request) {
                rejectRequest(request);
            }
        });
        recyclerView.setAdapter(adapter);

        // Set Welcome Message
        welcomeText.setText("Welcome, Administrator!");

        // Set Listeners
        registrationRequestsButton.setOnClickListener(view -> {
            currentView = "pending";
            loadPendingRequests();
        });

        rejectedRequestsButton.setOnClickListener(view -> {
            currentView = "rejected";
            loadRejectedRequests();
        });

        logoutButton.setOnClickListener(view -> {
            Toast.makeText(this, "Administrator logged out.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdministratorPage.this, LoginPage.class));
            finish();
        });

        // Load pending requests by default
        loadPendingRequests();
    }

    private void loadPendingRequests() {
        Log.d("AdminPage", "Loading pending requests...");

        db.collection("requests")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    requestsList.clear();

                    Log.d("AdminPage", "Total documents: " + queryDocumentSnapshots.size());

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String status = document.getString("status");

                        Log.d("AdminPage", "Doc ID: " + document.getId());
                        Log.d("AdminPage", "  Status: [" + status + "]");

                        // Check for pending status
                        if (status != null && "pending".equalsIgnoreCase(status)) {
                            // Log all available fields in the document
                            Log.d("AdminPage", "  >>> Available fields: " + document.getData().keySet());

                            // MANUAL MAPPING
                            RegistrationRequest request = new RegistrationRequest();
                            request.setRequestId(document.getId());
                            request.setFirstName(document.getString("firstName"));
                            request.setLastName(document.getString("lastName"));
                            request.setEmail(document.getString("email"));
                            request.setPhoneNumber(document.getString("phoneNumber"));
                            request.setRole(document.getString("role"));
                            request.setStatus(document.getString("status"));
                            request.setUserId(document.getString("userId"));
                            request.setProgram(document.getString("program"));
                            request.setDegree(document.getString("degree"));

                            // Handle courses array
                            Object coursesObj = document.get("courses");
                            if (coursesObj instanceof List) {
                                request.setCourses((List<String>) coursesObj);
                            }

                            // Debug log after mapping
                            Log.d("AdminPage", "  >>> Mapped FirstName: " + request.getFirstName());
                            Log.d("AdminPage", "  >>> Mapped LastName: " + request.getLastName());
                            Log.d("AdminPage", "  >>> Mapped Phone: " + request.getPhoneNumber());
                            Log.d("AdminPage", "  >>> Mapped Program: " + request.getProgram());
                            Log.d("AdminPage", "  >>> Mapped Degree: " + request.getDegree());

                            requestsList.add(request);
                        }
                    }

                    Log.d("AdminPage", "Total pending requests: " + requestsList.size());

                    adapter.notifyDataSetChanged();

                    if (requestsList.isEmpty()) {
                        emptyStateText.setVisibility(View.VISIBLE);
                        emptyStateText.setText("No pending requests");
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyStateText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminPage", "Error loading requests", e);
                    Toast.makeText(this, "Error loading requests: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void loadRejectedRequests() {
        Log.d("AdminPage", "Loading rejected requests...");

        db.collection("requests")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    requestsList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String status = document.getString("status");

                        // Check for rejected status
                        if ("rejected".equalsIgnoreCase(status)) {
                            // MANUAL MAPPING
                            RegistrationRequest request = new RegistrationRequest();
                            request.setRequestId(document.getId());
                            request.setFirstName(document.getString("firstName"));
                            request.setLastName(document.getString("lastName"));
                            request.setEmail(document.getString("email"));
                            request.setPhoneNumber(document.getString("phoneNumber"));
                            request.setRole(document.getString("role"));
                            request.setStatus(document.getString("status"));
                            request.setUserId(document.getString("userId"));
                            request.setProgram(document.getString("program"));
                            request.setDegree(document.getString("degree"));

                            // Handle courses array
                            Object coursesObj = document.get("courses");
                            if (coursesObj instanceof List) {
                                request.setCourses((List<String>) coursesObj);
                            }

                            requestsList.add(request);
                        }
                    }

                    Log.d("AdminPage", "Total rejected requests: " + requestsList.size());

                    adapter.notifyDataSetChanged();

                    if (requestsList.isEmpty()) {
                        emptyStateText.setVisibility(View.VISIBLE);
                        emptyStateText.setText("No rejected requests");
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        emptyStateText.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AdminPage", "Error loading rejected requests", e);
                    Toast.makeText(this, "Error loading rejected requests: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void approveRequest(RegistrationRequest request) {
        User newUser = null;

        if("Student".equals(request.getRole())){
            Student student = new Student(request.getEmail());
            student.setProgram(request.getProgram());
            newUser = student;
        } else if("Tutor".equals(request.getRole())){
            Tutor tutor = new Tutor(request.getEmail());
            tutor.setDegree(request.getDegree());
            tutor.setCourses(request.getCourses());
            newUser = tutor;
        } else if("Administrator".equals(request.getRole())){
            Administrator admin = new Administrator(request.getEmail());
            newUser = admin;
        }

        if(newUser == null){
            Toast.makeText(this, "Error. Role type is invalid.", Toast.LENGTH_SHORT).show();
            return;
        }


        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setRole(request.getRole());


        db.collection("users")
                .document(request.getUserId())
                .set(newUser)
                .addOnSuccessListener(aVoid -> {
                    db.collection("requests")
                        .document(request.getRequestId())
                        .update("status","approved")
                        .addOnSuccessListener(aVoid2 -> {
                            Toast.makeText(this, "Request approved successfully", Toast.LENGTH_SHORT).show();
                            // Reload current view
                            if ("pending".equals(currentView)) {
                                loadPendingRequests();
                            } else {
                                loadRejectedRequests();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this,"Failed to update request status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to approve: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void rejectRequest(RegistrationRequest request) {
        db.collection("requests")
                .document(request.getRequestId())
                .update("status", "rejected")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request rejected", Toast.LENGTH_SHORT).show();
                    loadPendingRequests();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to reject: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}