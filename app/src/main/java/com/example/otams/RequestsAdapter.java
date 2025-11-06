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
/**
 * RequestsAdapter
 *
 * RecyclerView adapter responsible for displaying a list of {@link RegistrationRequest} objects
 * to an administrator. Each request shows basic user details (name, email, role, etc.)
 * and includes "Approve" and "Reject" buttons that trigger corresponding actions
 * via the {@link OnRequestActionListener} interface.
 *
 * This class is primarily used in administrative views such as {@link AdministratorPage}
 * to review pending student and tutor registration requests.
 *
 * Flow:
 *  - Binds each Firestore {@link RegistrationRequest} to a layout item (item_registration_request.xml).
 *  - Displays contextual information (e.g., "Program" for students, "Degree" and "Courses" for tutors).
 *  - Provides event callbacks for approval and rejection.
 *
 */
public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestViewHolder> {

    private Context context;
    private List<RegistrationRequest> requests;
    private OnRequestActionListener listener;
    /**
     * Interface defining callbacks for administrative actions.
     * Implemented by the parent activity (e.g., {@link AdministratorPage}).
     */
    public interface OnRequestActionListener {
        /**
         * Called when an administrator approves a registration request.
         * @param request the {@link RegistrationRequest} being approved
         */
        void onApprove(RegistrationRequest request);
        void onReject(RegistrationRequest request);
    }
    /**
     * Constructor for RequestsAdapter.
     *
     * @param context  current context (usually the parent activity)
     * @param requests list of {@link RegistrationRequest} objects
     * @param listener event listener for request actions
     */
    public RequestsAdapter(Context context, List<RegistrationRequest> requests,
                           OnRequestActionListener listener) {
        this.context = context;
        this.requests = requests;
        this.listener = listener;
    }
    /**
     * Inflates the layout for each item in the RecyclerView.
     *
             * @param parent   the parent ViewGroup
     * @param viewType unused view type parameter
     * @return a new {@link RequestViewHolder} instance
     */
    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_registration_request, parent, false);
        return new RequestViewHolder(view);
    }
    /**
     * Binds a {@link RegistrationRequest} to its corresponding ViewHolder.
     * Populates text fields, sets button visibility, and attaches event listeners.
     *
     * @param holder   ViewHolder containing references to layout elements
     * @param position index of the current item in the dataset
     */
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
    /**
     * Returns the number of registration requests in the dataset.
     *
     * @return total number of items in the RecyclerView
     */
    @Override
    public int getItemCount() {
        return requests.size();
    }

    /**
     * Inner class representing the ViewHolder for each item in the RecyclerView.
     * Holds references to the layout elements for performance and reuse.
     */
    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textRole, textEmail, textPhone, textProgramOrDegree, textCourses;
        Button buttonApprove, buttonReject;

        /**
         * Constructor that binds UI components from the item layout.
         *
         * @param itemView the root view of the inflated item layout
         */
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