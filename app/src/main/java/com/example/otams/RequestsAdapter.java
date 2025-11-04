package com.example.otams;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private Context context;
    private List<RegistrationRequest> requests;
    private OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onApprove(RegistrationRequest request);
        void onReject(RegistrationRequest request);
    }

    public RequestsAdapter(Context context, List<RegistrationRequest> requests,
                           OnRequestActionListener listener) {
        this.context = context;
        this.requests = requests;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_registration_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        RegistrationRequest request = requests.get(position);

        // Debug logging
        Log.d("Adapter", "Position: " + position);
        Log.d("Adapter", "FirstName: " + request.getFirstName());
        Log.d("Adapter", "LastName: " + request.getLastName());
        Log.d("Adapter", "Email: " + request.getEmail());
        Log.d("Adapter", "Phone: " + request.getPhoneNumber());
        Log.d("Adapter", "Role: " + request.getRole());
        Log.d("Adapter", "Program: " + request.getProgram());
        Log.d("Adapter", "Degree: " + request.getDegree());

        // Set name
        String fullName = "";
        if (request.getFirstName() != null) {
            fullName += request.getFirstName();
        }
        if (request.getLastName() != null) {
            fullName += " " + request.getLastName();
        }
        holder.textName.setText(fullName.trim());

        // Set role
        if (request.getRole() != null) {
            holder.textRole.setText(request.getRole());
        } else {
            holder.textRole.setText("Unknown");
        }

        // Set email
        if (request.getEmail() != null) {
            holder.textEmail.setText(request.getEmail());
        } else {
            holder.textEmail.setText("");
        }

        // Set phone
        if (request.getPhoneNumber() != null) {
            holder.textPhone.setText(request.getPhoneNumber());
        } else {
            holder.textPhone.setText("");
        }

        // Show student or tutor specific info
        if ("Student".equals(request.getRole())) {
            if (request.getProgram() != null) {
                holder.textProgramOrDegree.setText("Program: " + request.getProgram());
            } else {
                holder.textProgramOrDegree.setText("Program: N/A");
            }
            holder.textCourses.setVisibility(View.GONE);
        } else if ("Tutor".equals(request.getRole())) {
            if (request.getDegree() != null) {
                holder.textProgramOrDegree.setText("Degree: " + request.getDegree());
            } else {
                holder.textProgramOrDegree.setText("Degree: N/A");
            }

            holder.textCourses.setVisibility(View.VISIBLE);
            holder.textCourses.setText("Courses: " + request.getCoursesAsString());
        }

        // Handle button clicks
        holder.buttonApprove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApprove(request);
            }
        });

        holder.buttonReject.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReject(request);
            }
        });

        // Hide reject button if already rejected
        if ("rejected".equals(request.getStatus())) {
            holder.buttonReject.setVisibility(View.GONE);
        } else {
            holder.buttonReject.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textRole, textEmail, textPhone, textProgramOrDegree, textCourses;
        Button buttonApprove, buttonReject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textRole = itemView.findViewById(R.id.textRole);
            textEmail = itemView.findViewById(R.id.textEmail);
            textPhone = itemView.findViewById(R.id.textPhone);
            textProgramOrDegree = itemView.findViewById(R.id.textProgramOrDegree);
            textCourses = itemView.findViewById(R.id.textCourses);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonReject = itemView.findViewById(R.id.buttonReject);
        }


    }
}