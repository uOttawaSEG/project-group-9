package com.example.otams;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Shows available tutoring sessions for a specific course
 * Students can request sessions from this screen
 */
public class AvailableSessionsActivity extends AppCompatActivity {

    private static final String TAG = "AvailableSessions";

    private TextView courseTitle;
    private RecyclerView sessionsRecyclerView;
    private TextView noSessionsText;
    private AvailableSessionAdapter adapter;
    private List<AvailableSession> availableSessions;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String courseName;
    private int pendingChecks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_sessions);

        Log.d(TAG, "onCreate called");

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        courseName = getIntent().getStringExtra("course");
        availableSessions = new ArrayList<>();

        Log.d(TAG, "Loading sessions for course: " + courseName);

        initializeViews();
        setupRecyclerView();
        loadAvailableSessions();

        // Handle back press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void initializeViews() {
        courseTitle = findViewById(R.id.courseTitle);
        sessionsRecyclerView = findViewById(R.id.sessionsRecyclerView);
        noSessionsText = findViewById(R.id.noSessionsText);

        if (courseName != null) {
            courseTitle.setText(courseName);
        }

        Log.d(TAG, "Views initialized");
    }

    private void setupRecyclerView() {
        adapter = new AvailableSessionAdapter(availableSessions, this::requestSession);
        sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sessionsRecyclerView.setAdapter(adapter);
        Log.d(TAG, "RecyclerView setup complete");
    }

    private void loadAvailableSessions() {
        TimeZone timeZone = TimeZone.getTimeZone("America/Toronto");
        Calendar today = Calendar.getInstance(timeZone);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        int todayInt = today.get(Calendar.YEAR) * 10000 +
                (today.get(Calendar.MONTH) + 1) * 100 +
                today.get(Calendar.DAY_OF_MONTH);

        Log.d(TAG, "Today's date as int: " + todayInt);
        Log.d(TAG, "Querying for course: " + courseName);

        db.collection("availabilitySlots")
                .whereEqualTo("course", courseName)
                .whereGreaterThanOrEqualTo("date", todayInt)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Query SUCCESS! Found " + queryDocumentSnapshots.size() + " slots");

                    availableSessions.clear();
                    pendingChecks = queryDocumentSnapshots.size();

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "No slots found for this course");
                        updateEmptyView();
                        return;
                    }

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            Log.d(TAG, "Processing document ID: " + document.getId());

                            String slotId = document.getId();
                            String tutorEmail = document.getString("tutorEmail");
                            String tutorId = document.getString("tutorId");

                            if (document.get("date") == null) {
                                Log.e(TAG, "Missing date field in document: " + slotId);
                                pendingChecks--;
                                continue;
                            }
                            if (document.get("startTime") == null) {
                                Log.e(TAG, "Missing startTime field in document: " + slotId);
                                pendingChecks--;
                                continue;
                            }
                            if (document.get("endTime") == null) {
                                Log.e(TAG, "Missing endTime field in document: " + slotId);
                                pendingChecks--;
                                continue;
                            }

                            int date = document.getLong("date").intValue();
                            int startTime = document.getLong("startTime").intValue();
                            int endTime = document.getLong("endTime").intValue();
                            boolean requiresApproval = document.getBoolean("requiresApproval") != null
                                    ? document.getBoolean("requiresApproval") : false;

                            Log.d(TAG, "Slot: " + slotId + ", Date: " + date + ", Time: " + startTime + "-" + endTime);

                            // Check if slot is already booked
                            checkIfSlotBooked(slotId, isBooked -> {
                                pendingChecks--;
                                Log.d(TAG, "Slot " + slotId + " is booked: " + isBooked + " (pending checks: " + pendingChecks + ")");


                                if (isBooked) {
                                    if(pendingChecks == 0){
                                        updateEmptyView();   
                                    }
                                    return;
                                }

                                db.collection("tutors")
                                    .document(tutorId)
                                    .get()
                                    .addOnSuccessListener(tutDoc -> {
                                        Tutor tutor = tutDoc.toObject(Tutor.class);

                                        if(tutor == null){
                                            Log.e(TAG, "Tutor object null");
                                            return;
                                        }

                                        AvailableSession session = new AvailableSession(slotId, tutor, date, startTime, endTime, courseName, requiresApproval);

                                        session.setTutorAverageRating(tutor.calculateAverageRating());
                                        session.setTutorTotalRatings(tutor.getTotalRatings());
                                        
                                        availableSessions.add(session);
                                        Log.d(TAG, "Added session! Total now: " + availableSessions.size());
                                        
                                        runOnUiThread(() -> {
                                            adapter.notifyDataSetChanged();
                                            updateEmptyView();
                                        });

                                        if (pendingChecks == 0) {
                                            Log.d(TAG, "All checks complete. Final count: " + availableSessions.size());
                                            runOnUiThread(this::updateEmptyView);
                                        }
                                    })

                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Query FAILED", e);
                                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        updateEmptyView();
                                    });
                            });
            } catch (Exception e){
                Log.e(TAG, "Error", e);
                pendingChecks--;
            }
        }

        })
        .addOnFailureListener(e -> {
            Log.e(TAG, "Query FAILED", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            updateEmptyView();
        });
    }
    

    private void checkIfSlotBooked(String slotId, OnSlotCheckListener listener) {
        Log.d(TAG, "Checking booking status for slot: " + slotId);

        db.collection("sessionRequests")
                .whereEqualTo("slotId", slotId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean isBooked = false;

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String status = doc.getString("status");
                        Log.d(TAG, "Found request for slot " + slotId + " with status: " + status);

                        if ("pending".equals(status) || "approved".equals(status)) {
                            isBooked = true;
                            break;
                        }
                    }

                    listener.onChecked(isBooked);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error checking bookings for slot: " + slotId, e);
                    listener.onChecked(false);
                });
    }

    private void requestSession(AvailableSession session) {
        Log.d(TAG, "Requesting session: " + session.getSlotId());

        if (auth.getCurrentUser() == null) {
            Toast.makeText(this, "You must be logged in to request a session", Toast.LENGTH_SHORT).show();
            return;
        }

        String studentId = auth.getCurrentUser().getUid();
        String studentEmail = auth.getCurrentUser().getEmail();

        db.collection("users")
                .document(studentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Log.d(TAG, "Found user in users collection");
                        createSessionRequest(session, studentId, studentEmail, documentSnapshot);
                    } else {
                        Log.d(TAG, "User not in users collection, checking requests");
                        db.collection("requests")
                                .whereEqualTo("userId", studentId)
                                .whereEqualTo("status", "approved")
                                .get()
                                .addOnSuccessListener(querySnapshot -> {
                                    if (!querySnapshot.isEmpty()) {
                                        Log.d(TAG, "Found user in requests collection");
                                        createSessionRequest(session, studentId, studentEmail,
                                                querySnapshot.getDocuments().get(0));
                                    } else {
                                        Log.d(TAG, "No user info found, using email only");
                                        createSessionRequestWithEmail(session, studentId, studentEmail);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e(TAG, "Error checking requests", e);
                                    createSessionRequestWithEmail(session, studentId, studentEmail);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error getting user document", e);
                    createSessionRequestWithEmail(session, studentId, studentEmail);
                });
    }

    private void createSessionRequest(AvailableSession session, String studentId, String studentEmail,
                                      com.google.firebase.firestore.DocumentSnapshot documentSnapshot) {
        String firstName = documentSnapshot.getString("firstName");
        String lastName = documentSnapshot.getString("lastName");
        String phoneNumber = documentSnapshot.getString("phoneNumber");

        Map<String, Object> request = new HashMap<>();
        request.put("slotId", session.getSlotId());
        request.put("studentId", studentId);
        request.put("studentEmail", studentEmail);
        request.put("studentFirstName", firstName != null ? firstName : "");
        request.put("studentLastName", lastName != null ? lastName : "");
        request.put("studentPhone", phoneNumber != null ? phoneNumber : "");
        request.put("tutorId", session.getTutorId());
        request.put("tutorEmail", session.getTutorEmail());
        request.put("course", session.getCourse());
        request.put("date", session.getDate());
        request.put("startTime", session.getStartTime());
        request.put("endTime", session.getEndTime());
        request.put("requiresApproval", session.isRequiresApproval());
        request.put("status", session.isRequiresApproval() ? "pending" : "approved");
        request.put("timestamp", Timestamp.now());

        Log.d(TAG, "Saving session request: " + request);
        saveSessionRequest(request, session.isRequiresApproval(), firstName, lastName, phoneNumber);
    }

    private void createSessionRequestWithEmail(AvailableSession session, String studentId, String studentEmail) {
        Map<String, Object> request = new HashMap<>();
        request.put("slotId", session.getSlotId());
        request.put("studentId", studentId);
        request.put("studentEmail", studentEmail);
        request.put("studentFirstName", "");
        request.put("studentLastName", "");
        request.put("studentPhone", "");
        request.put("tutorId", session.getTutorId());
        request.put("tutorEmail", session.getTutorEmail());
        request.put("course", session.getCourse());
        request.put("date", session.getDate());
        request.put("startTime", session.getStartTime());
        request.put("endTime", session.getEndTime());
        request.put("requiresApproval", session.isRequiresApproval());
        request.put("status", session.isRequiresApproval() ? "pending" : "approved");
        request.put("timestamp", Timestamp.now());

        Log.d(TAG, "Saving session request with email only: " + request);
        saveSessionRequest(request, session.isRequiresApproval(), "", "", "");
    }

    private void saveSessionRequest(Map<String, Object> request, boolean requiresApproval,
                                    String firstName, String lastName, String phoneNumber) {
        db.collection("sessionRequests")
                .add(request)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Session request saved: " + documentReference.getId());

                    // If instant booking (no approval required), create Session immediately
                    if (!requiresApproval) {
                        createSessionForInstantBooking(request, firstName, lastName, phoneNumber);
                    }

                    String message = requiresApproval
                            ? "Session request sent! Waiting for tutor approval."
                            : "Session booked successfully!";
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to save session request", e);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void createSessionForInstantBooking(Map<String, Object> request,
                                                String firstName, String lastName, String phoneNumber) {
        Log.d(TAG, "Creating Session for instant booking");

        // Create Student object
        Map<String, Object> student = new HashMap<>();
        student.put("firstName", firstName != null ? firstName : "");
        student.put("lastName", lastName != null ? lastName : "");
        student.put("email", request.get("studentEmail"));
        student.put("phoneNumber", phoneNumber != null ? phoneNumber : "");
        student.put("userId", request.get("studentId"));

        // Create Session document
        Map<String, Object> session = new HashMap<>();
        session.put("tutorId", request.get("tutorId"));
        session.put("tutorEmail", request.get("tutorEmail"));
        session.put("student", student);
        session.put("course", request.get("course"));
        session.put("date", request.get("date"));
        session.put("startTime", request.get("startTime"));
        session.put("endTime", request.get("endTime"));
        session.put("status", "approved");
        session.put("timestamp", Timestamp.now());

        db.collection("Sessions")
                .add(session)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Session created successfully: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating Session document", e);
                });
    }

    private void updateEmptyView() {
        Log.d(TAG, "Updating view. Sessions: " + availableSessions.size());

        if (availableSessions.isEmpty()) {
            noSessionsText.setVisibility(View.VISIBLE);
            sessionsRecyclerView.setVisibility(View.GONE);
            Log.d(TAG, "Showing 'no sessions' message");
        } else {
            noSessionsText.setVisibility(View.GONE);
            sessionsRecyclerView.setVisibility(View.VISIBLE);
            Log.d(TAG, "Showing sessions list");
        }
    }

    interface OnSlotCheckListener {
        void onChecked(boolean isBooked);
    }
}