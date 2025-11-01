package com.example.otams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Fragment that displays a list of pending registration requests for the current tutor.
 * It uses a RecyclerView to display the requests in a list format.
 */
public class RequestsFragment extends Fragment {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<Request, RequestsFragment.RequestViewHolder> adapter;
    private TextView noRequestsText;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.requestsRecyclerView);

        noRequestsText = view.findViewById(R.id.noRequestsText);

        // Set up RecyclerView
        setupRecyclerView();

        return view;
    }
    //ViewHolder for the student requests for the tutor
    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        private final TextView textStudentName, textCourseCode, textEmail;
        public final Button approveButton, rejectButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textStudentName = itemView.findViewById(R.id.textStudentName);
            textCourseCode = itemView.findViewById(R.id.textCourseCode);
            textEmail = itemView.findViewById(R.id.textEmail);
            approveButton = itemView.findViewById(R.id.buttonApprove);
            rejectButton = itemView.findViewById(R.id.buttonReject);
        }

        public void bind(Request request) {
            //Bind the data to the ViewHolder
            User student = request.getUser();
            if (student != null) {
                String fullName = (student.getFirstName() != null ? student.getFirstName() : "") + " " + (student.getLastName() != null ? student.getLastName() : "");
                textStudentName.setText(fullName.trim());
                //Set the course code in the future
                //textCourseCode.setText(request.getCourseCode());
                textEmail.setText(student.getEmail());
            }
        }
    }

    // Start listening for changes in the Firestore query
    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    // Stop listening for changes in the Firestore query
    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }


    private void setupRecyclerView() {
        //Get the current logged-in tutor's ID
        String currentTutorId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Query to get pending registration requests for the current tutor
        Query query = db.collection("requests")
                .whereEqualTo("tutorId", currentTutorId)
                .whereEqualTo("approval", false);

        //Configure the FirestoreRecyclerOptions
        FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        //Create the adapter
        adapter = new FirestoreRecyclerAdapter<Request, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i, @NonNull Request request) {
                //Bind the data to the ViewHolder
                requestViewHolder.bind(request);

                //Get the request ID
                String requestId = getSnapshots().getSnapshot(i).getId();

                //Approve the request
                requestViewHolder.approveButton.setOnClickListener(v -> {
                    //Approve the request
                    db.collection("requests").document(requestId)
                            .update("approval", true)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Request approved", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to approve request", Toast.LENGTH_SHORT).show();
                            });
                });

                //Reject the request
                requestViewHolder.rejectButton.setOnClickListener(v -> {
                    db.collection("requests").document(requestId)
                            .update("approval", false)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Request rejected", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to reject request", Toast.LENGTH_SHORT).show();
                            });
                });

                //Attach the adapter to the RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }


            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_session, parent, false);
                return new RequestViewHolder(view);
            }
        };
    }


}