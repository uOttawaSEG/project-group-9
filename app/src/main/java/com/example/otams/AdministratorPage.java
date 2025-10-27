package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewGroup;
import android.widget.View;
import android.widget.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdministratorPage extends AppCompatActivity {
    TextView welcomeText;
    Button logoutButton, rejectedRequestsButton;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    RequestsAdapter adapter;
    List<DocumentSnapshot> pendingRequests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_home);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        welcomeText = findViewById(R.id.welcomeText);
        logoutButton = findViewById(R.id.logoutButton);

        rejectedRequestsButton = findViewById(R.id.button3);
        recyclerView = findViewById(R.id.recyclerViewRequests);
        adapter = new RequestsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchRequests();

        // Display welcome message
        if (mAuth.getCurrentUser() != null) {
            welcomeText.setText("Welcome, Administrator!");
        }

        rejectedRequestsButton.setOnClickListener(v -> {
            Intent rejectedButton = new Intent(AdministratorPage.this, RejectedRequestsPage.class);
            startActivity(rejectedButton);
        });

        // Logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), LoginPage.class));
            finish();
        });
    }

    private void fetchRequests(){
        db.collection("requests")
                .whereEqualTo("approved", false)
                .get()
                .addOnSuccessListener((QuerySnapshot snapshots) -> {
                    pendingRequests.clear();
                    pendingRequests.addAll(snapshots.getDocuments());
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> toast("Failed to fetch requests: " + e.getMessage()));
    }

    private class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>{
        @Override
        @NonNull
        public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.request_card, parent, false);
            return new RequestViewHolder(view);
        } 

        @Override
        public void onBindViewHolder(@NonNull RequestViewHolder holder, int pos){
            DocumentSnapshot req = pendingRequests.get(pos);
            holder.name.setText(req.getString("firstName") + " " + req.getString("lastName"));
            holder.role.setText(req.getString("role"));
            holder.email.setText("Email: " + req.getString("email"));
            holder.phone.setText("Phone " + req.getString("phoneNumber"));

            if("Student".equals(req.getString("role"))){
                holder.programOrDegree.setText("Program: " + req.getString("program"));
                holder.courses.setVisibility(View.GONE);
            } else if("Tutor".equals(req.getString("role"))){
                holder.programOrDegree.setText("Degree: " + req.getString("degree"));
                List<String> courses = (List<String>) req.get("courses");
                if(courses != null && !courses.isEmpty()){
                    holder.courses.setText("Courses: " + String.join(", ", courses));
                    holder.courses.setVisibility(View.VISIBLE);
                } else { holder.courses.setVisibility(View.GONE);}
            }
            holder.approveButton.setOnClickListener(v -> approveRequest(req, pos));
            holder.denyButton.setOnClickListener(v -> denyRequest(req, pos));      
        }

        @Override
        public int getItemCount(){
            return pendingRequests.size();
        }

        class RequestViewHolder extends RecyclerView.ViewHolder{
            TextView name, role, email, phone, programOrDegree, courses;
            Button approve, deny;

            RequestViewHolder(@NonNull View itemView){
                super(itemView);
                name = itemView.findViewById(R.id.textName);
                role = itemView.findViewById(R.id.textRole);
                email = itemView.findViewById(R.id.textEmail);
                phone = itemView.findViewById(R.id.textPhone);
                programOrDegree = itemView.findViewById(R.id.textProgramOrDegree);
                courses = itemView.findViewById(R.id.textCourses);
                approve = itemView.findViewById(R.id.buttonApprove);
                deny = itemView.findViewById(R.id.buttonReject);
            }
        }

        private void approveRequest(DocumentSnapshot req, int pos){
            db.collection("requests").document(req.getId())
                    .update("approved", true)
                    .addOnSuccessListener(unused -> {
                        toast("Request approved.");
                        pendingRequests.remove(pos);
                        notifyItemRemoved(pos);
                    })
                    .addOnFailureListener(e -> toast("Failed to approve: " + e.getMessage()));
        }

        private void denyRequest(DocumentSnapshot req, int pos){
            Map<String,Object> data = req.getData();
            db.collection("rejectedRequests")
                    .document(req.getId())
                    .set(data)
                    .addOnSuccessListener(unused -> db.collection("requests").document(req.getId()))
                            .delete()
                            .addOnSuccessListener(unused -> {
                                toast("Request rejected.");
                                pendingRequests.remove(pos);
                                notifyItemRemoved(pos);
                            })
                    .addOnFailureListener(e -> toast("Failed to approve: " + e.getMessage()));
        }
    }

    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

