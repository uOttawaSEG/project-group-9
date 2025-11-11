package com.example.otams;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment showing pending session requests for tutors
 */
public class RequestsFragment extends Fragment {

    private static final String TAG = "RequestsFragment";

    private RecyclerView requestsRecyclerView;
    private TextView noRequestsText;
    private SessionRequestAdapter adapter;
    private List<SessionRequest> sessionRequests;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionRequests = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        initializeViews(view);
        setupRecyclerView();
        loadPendingRequests();

        return view;
    }

    private void initializeViews(View view) {
        requestsRecyclerView = view.findViewById(R.id.requestsRecyclerView);
        noRequestsText = view.findViewById(R.id.noRequestsText);
    }

    private void setupRecyclerView() {
        adapter = new SessionRequestAdapter(sessionRequests, new SessionRequestAdapter.OnRequestActionListener() {
            @Override
            public void onApprove(SessionRequest request) {
                approveRequest(request);
            }

            @Override
            public void onReject(SessionRequest request) {
                rejectRequest(request);
            }
        });

        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestsRecyclerView.setAdapter(adapter);
    }

    private void loadPendingRequests() {
        String tutorId = auth.getCurrentUser().getUid();

        db.collection("sessionRequests")
                .whereEqualTo("tutorId", tutorId)
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    sessionRequests.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String requestId = document.getId();
                        String studentId = document.getString("studentId");
                        String studentEmail = document.getString("studentEmail");
                        String studentFirstName = document.getString("studentFirstName");
                        String studentLastName = document.getString("studentLastName");
                        String studentPhone = document.getString("studentPhone");
                        String course = document.getString("course");
                        int date = document.getLong("date").intValue();
                        int startTime = document.getLong("startTime").intValue();
                        int endTime = document.getLong("endTime").intValue();

                        SessionRequest request = new SessionRequest(
                                requestId, studentId, studentEmail, studentFirstName,
                                studentLastName, studentPhone, course, date, startTime, endTime
                        );
                        sessionRequests.add(request);
                    }

                    adapter.notifyDataSetChanged();
                    updateEmptyView();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error loading requests: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void approveRequest(SessionRequest request) {
        Log.d(TAG, "Approving request: " + request.getRequestId());

        // First, update the request status to approved
        db.collection("sessionRequests")
                .document(request.getRequestId())
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Request status updated to approved");

                    // Then create the Session document
                    createSession(request);

                    // Remove from UI
                    sessionRequests.remove(request);
                    adapter.notifyDataSetChanged();
                    updateEmptyView();
                    Toast.makeText(getContext(), "Session approved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error approving request", e);
                    Toast.makeText(getContext(), "Error approving: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void createSession(SessionRequest request) {
        Log.d(TAG, "Creating Session document for approved request");

        // Get tutor info
        String tutorId = auth.getCurrentUser().getUid();
        String tutorEmail = auth.getCurrentUser().getEmail();

        // Create Student object
        Map<String, Object> student = new HashMap<>();
        student.put("firstName", request.getStudentFirstName());
        student.put("lastName", request.getStudentLastName());
        student.put("email", request.getStudentEmail());
        student.put("phoneNumber", request.getStudentPhone());
        student.put("userId", request.getStudentId());

        // Create Session document
        Map<String, Object> session = new HashMap<>();
        session.put("tutorId", tutorId);
        session.put("tutorEmail", tutorEmail);
        session.put("student", student);
        session.put("course", request.getCourse());
        session.put("date", request.getDate());
        session.put("startTime", request.getStartTime());
        session.put("endTime", request.getEndTime());
        session.put("status", "approved");
        session.put("timestamp", Timestamp.now());

        db.collection("Sessions")
                .add(session)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Session created successfully: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating Session document", e);
                    Toast.makeText(getContext(), "Warning: Session approved but not added to Sessions list",
                            Toast.LENGTH_LONG).show();
                });
    }

    private void rejectRequest(SessionRequest request) {
        db.collection("sessionRequests")
                .document(request.getRequestId())
                .update("status", "rejected")
                .addOnSuccessListener(aVoid -> {
                    sessionRequests.remove(request);
                    adapter.notifyDataSetChanged();
                    updateEmptyView();
                    Toast.makeText(getContext(), "Session rejected", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error rejecting: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateEmptyView() {
        if (sessionRequests.isEmpty()) {
            noRequestsText.setVisibility(View.VISIBLE);
            requestsRecyclerView.setVisibility(View.GONE);
        } else {
            noRequestsText.setVisibility(View.GONE);
            requestsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}