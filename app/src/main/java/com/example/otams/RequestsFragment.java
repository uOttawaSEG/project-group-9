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
 * @author Imane Moussa Marou [imous061@uotttawa.ca]
 */
public class RequestsFragment extends Fragment {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter<Request, RequestsFragment.RequestViewHolder> adapter;

    public RequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.requestsRecyclerView);

        TextView noRequestsText = view.findViewById(R.id.noRequestsText);

        setupRecyclerView();

        return view;
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        private final TextView textStudentName;
        private final TextView textEmail;
        public final Button approveButton, rejectButton;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textStudentName = itemView.findViewById(R.id.textStudentName);
            TextView textCourseCode = itemView.findViewById(R.id.textCourseCode);
            textEmail = itemView.findViewById(R.id.textEmail);
            approveButton = itemView.findViewById(R.id.buttonApprove);
            rejectButton = itemView.findViewById(R.id.buttonReject);
        }

        public void bind(Request request) {

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

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }


    private void setupRecyclerView() {

        String currentTutorId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Query query = db.collection("requests")
                .whereEqualTo("tutorId", currentTutorId)
                .whereEqualTo("approval", false);

        FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Request, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i, @NonNull Request request) {

                requestViewHolder.bind(request);

                String requestId = getSnapshots().getSnapshot(i).getId();

                requestViewHolder.approveButton.setOnClickListener(v -> {
                    db.collection("requests").document(requestId)
                            .update("approval", true)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Request approved", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to approve request", Toast.LENGTH_SHORT).show();
                            });
                });

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