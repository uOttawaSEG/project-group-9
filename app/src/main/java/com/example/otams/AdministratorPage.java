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
 * AdministratorPage
 *
 * Admin dashboard for reviewing and moderating registration requests.
 * Displays requests in a {@link RecyclerView} via {@link RequestsAdapter}, and lets
 * the admin approve or reject them. Supports two views:
 *  - Pending requests
 *  - Rejected requests
 *
 * Flow:
 *  - Loads pending requests by default.
 *  - Admin can switch to the rejected list.
 *  - Approve → creates/updates a document in "users" and marks request as "approved".
 *  - Reject → updates request's "status" to "rejected".
 *
 * Firestore:
 *  - Collection "requests": registration requests (pending/approved/rejected)
 *  - Collection "users": persisted user profiles after approval
 *  - Collection "tutors": persisted tutor profiles after approval
 *
 * Layout file: {@code activity_administrator_home.xml}
 *
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

    /**
     * Lifecycle: sets up Firebase, binds UI elements, configures the RecyclerView and listeners,
     * and loads the default dataset (pending requests).
     *
     * @param savedInstanceState previously saved instance state (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);
        registrationRequestsButton = findViewById(R.id.Move2);
        rejectedRequestsButton = findViewById(R.id.button3);
        recyclerView = findViewById(R.id.requestsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);


        requestsList = new ArrayList<>();


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


        welcomeText.setText("Welcome, Administrator!");


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


        loadPendingRequests();
    }
    /**
     * Loads and displays all requests with status "pending".
     * Populates {@link #requestsList} and updates empty-state visibility.
     */
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


                        if (status != null && "pending".equalsIgnoreCase(status)) {
                            // Log all available fields in the document
                            RegistrationRequest request = new RegistrationRequest();
                            String role = document.getString("role");
                            User user;

                            if ("Student".equals(role)) {
                                user = new Student();
                            } else if ("Tutor".equals(role)) {
                                user = new Tutor();
                            } else {
                                Log.e("AdminPage", "Unknown role in request: " + role);
                                continue; // Skip this request
                            }
                            request.setUser(user);

                            request.setRequestId(document.getId());
                            request.setFirstName(document.getString("firstName"));
                            request.setLastName(document.getString("lastName"));
                            request.setEmail(document.getString("email"));
                            request.setPhoneNumber(document.getString("phoneNumber"));
                            request.setRole(role);
                            request.setStatus(document.getString("status"));
                            request.setUserId(document.getString("userId"));
                            request.setProgram(document.getString("program"));
                            request.setDegree(document.getString("degree"));


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
    /**
     * Loads and displays all requests with status "rejected".
     * Populates {@link #requestsList} and updates empty-state visibility.
     */
    private void loadRejectedRequests() {
        Log.d("AdminPage", "Loading rejected requests...");

        db.collection("requests")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    requestsList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String status = document.getString("status");


                        if ("rejected".equalsIgnoreCase(status)) {
                            RegistrationRequest request = new RegistrationRequest();
                            String role = document.getString("role");
                            User user;

                            if ("Student".equals(role)) {
                                user = new Student();
                            } else if ("Tutor".equals(role)) {
                                user = new Tutor();
                            } else {
                                Log.e("AdminPage", "Unknown role in request: " + role);
                                continue; // Skip this request
                            }
                            request.setUser(user);

                            // MANUAL MAPPING
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

    /**
     * Approves a {@link RegistrationRequest}:
     *  - Creates a corresponding {@code users/{userId}} document with typed model (Student/Tutor/Admin).
     *  - Updates the original request's status to "approved".
     *  - Refreshes the currently selected view (pending or rejected).
     *
     * @param request the request to approve
     */
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
            tutor.setTotalRatingPoints(0);
            tutor.setTotalRatings(0);
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

        final User finalNewUser = newUser;

        db.collection("users")
                .document(request.getUserId())
                .set(newUser)
                .addOnSuccessListener(aVoid -> {
                    if (finalNewUser instanceof Tutor) {
                        Tutor tutor = (Tutor) finalNewUser;
                        db.collection("tutors")
                                .document(request.getUserId()) // même uid
                                .set(tutor)
                                .addOnFailureListener(e ->
                                        Log.e("AdminPage", "Failed to save tutor profile: " + e.getMessage())
                                );
                    }
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

    /**
     * Rejects a {@link RegistrationRequest} by setting its status to "rejected"
     * and then reloading the pending list.
     *
     * @param request the request to reject
     */
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