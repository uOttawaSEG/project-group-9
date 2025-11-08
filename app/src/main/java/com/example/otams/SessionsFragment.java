package com.example.otams;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Fragment showing tutor's upcoming and past sessions
 */
public class SessionsFragment extends Fragment {

    private static final String TAG = "SessionsFragment";

    private RecyclerView upcomingRecyclerView;
    private RecyclerView pastRecyclerView;
    private SessionAdapter upcomingAdapter;
    private SessionAdapter pastAdapter;
    private List<Session> upcomingSessions;
    private List<Session> pastSessions;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public SessionsFragment() {
        // empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upcomingSessions = new ArrayList<>();
        pastSessions = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);

        upcomingRecyclerView = view.findViewById(R.id.upcomingSessionsRecyclerView);
        pastRecyclerView = view.findViewById(R.id.pastSessionsRecyclerView);

        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        upcomingAdapter = new SessionAdapter(upcomingSessions);
        pastAdapter = new SessionAdapter(pastSessions);

        upcomingRecyclerView.setAdapter(upcomingAdapter);
        pastRecyclerView.setAdapter(pastAdapter);

        loadSessions();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        loadSessions();
    }

    private void loadSessions() {
        String tutorId = auth.getCurrentUser().getUid();

        // Get today's date as integer
        Calendar today = Calendar.getInstance();
        int todayInt = today.get(Calendar.YEAR) * 10000 +
                (today.get(Calendar.MONTH) + 1) * 100 +
                today.get(Calendar.DAY_OF_MONTH);

        Log.d(TAG, "Loading sessions for tutorId: " + tutorId + ", today: " + todayInt);

        // Load all sessions for this tutor
        db.collection("Sessions")
                .whereEqualTo("tutorId", tutorId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    upcomingSessions.clear();
                    pastSessions.clear();

                    Log.d(TAG, "Found " + queryDocumentSnapshots.size() + " total sessions");

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            String sessionId = document.getId();


                            Map<String, Object> studentMap = (Map<String, Object>) document.get("student");
                            Student student = null;
                            if (studentMap != null) {
                                String firstName = (String) studentMap.get("firstName");
                                String lastName = (String) studentMap.get("lastName");
                                String email = (String) studentMap.get("email");
                                student = new Student(email);
                                student.setFirstName(firstName != null ? firstName : "");
                                student.setLastName(lastName != null ? lastName : "");
                            }

                            String course = document.getString("course");
                            int date = document.getLong("date").intValue();
                            int startTime = document.getLong("startTime").intValue();
                            int endTime = document.getLong("endTime").intValue();
                            String status = document.getString("status");

                            String tutorEmail = document.getString("tutorEmail");
                            Tutor tutor = new Tutor(tutorEmail);

                            Session session = new Session(tutor, student);
                            session.setCourse(course);
                            session.setApproval(status != null ? status : "approved");

                            session.setDate(date);
                            session.setStartTime(startTime);
                            session.setEndTime(endTime);


                            if (date >= todayInt) {
                                upcomingSessions.add(session);
                                Log.d(TAG, "Added to upcoming: " + course + " on " + date);
                            } else {
                                pastSessions.add(session);
                                Log.d(TAG, "Added to past: " + course + " on " + date);
                            }

                        } catch (Exception e) {
                            Log.e(TAG, "Error processing session document", e);
                        }
                    }

                    Log.d(TAG, "Upcoming: " + upcomingSessions.size() + ", Past: " + pastSessions.size());

                    upcomingAdapter.notifyDataSetChanged();
                    pastAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading sessions", e);
                });
    }

    private void cancelSession(Session session) {
        String tutorId = auth.getCurrentUser().getUid();

        // Find this session in Firestore and delete it
        db.collection("Sessions")
                .whereEqualTo("tutorId", tutorId)
                .whereEqualTo("course", session.getCourse())
                .whereEqualTo("date", session.getDate())
                .whereEqualTo("startTime", session.getStartTime())
                .whereEqualTo("endTime", session.getEndTime())
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        querySnapshot.getDocuments().get(0).getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Remove locally
                                    upcomingSessions.remove(session);
                                    upcomingAdapter.notifyDataSetChanged();
                                    Log.d(TAG, "Session canceled successfully");
                                })
                                .addOnFailureListener(e ->
                                        Log.e(TAG, "Error canceling session", e));
                    } else {
                        Log.w(TAG, "Session not found for cancel");
                    }
                })
                .addOnFailureListener(e ->
                        Log.e(TAG, "Error finding session for cancel", e));
    }


    private class SessionAdapter extends RecyclerView.Adapter<SessionViewHolder> {
        private List<Session> sessions;

        public SessionAdapter(List<Session> sessions) {
            this.sessions = sessions;
        }

        @NonNull
        @Override
        public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_session, parent, false);
            return new SessionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
            Session session = sessions.get(position);

            Student student = session.getStudent();
            if (student != null) {
                String name = student.getFirstName() + " " + student.getLastName();
                holder.textStudentName.setText(name.trim().isEmpty() ? "Student" : name);
            } else {
                holder.textStudentName.setText("Student");
            }

            holder.textCourseCode.setText(session.getCourse() != null ? session.getCourse() : "N/A");
            holder.textTimeSlot.setText(formatDate(session.getDate()) + "\n" +
                    formatTimeRange(session.getStartTime(), session.getEndTime()));
            holder.textStatus.setText(session.getApproval() != null ? session.getApproval() : "approved");

            if (sessions == upcomingSessions) {
                holder.buttonReject.setVisibility(View.VISIBLE);
                holder.buttonReject.setText("Cancel");

                holder.buttonReject.setOnClickListener(v -> cancelSession(session));
            } else {
                holder.buttonReject.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return sessions.size();
        }
    }

    private class SessionViewHolder extends RecyclerView.ViewHolder {
        TextView textStudentName, textCourseCode, textTimeSlot, textStatus;
        TextView buttonReject;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            textStudentName = itemView.findViewById(R.id.textStudentName);
            textCourseCode = itemView.findViewById(R.id.textCourseCode);
            textTimeSlot = itemView.findViewById(R.id.textTimeSlot);
            textStatus = itemView.findViewById(R.id.textStatus);
            buttonReject = itemView.findViewById(R.id.buttonReject);
        }
    }

    private String formatDate(int dateInt) {
        int year = dateInt / 10000;
        int month = (dateInt % 10000) / 100;
        int day = dateInt % 100;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);

        java.text.SimpleDateFormat dateFormat =
                new java.text.SimpleDateFormat("EEEE, MMMM d, yyyy", java.util.Locale.getDefault());
        return dateFormat.format(cal.getTime());
    }

    private String formatTimeRange(int startTimeMinutes, int endTimeMinutes) {
        String startTime = formatTime(startTimeMinutes);
        String endTime = formatTime(endTimeMinutes);
        return startTime + " - " + endTime;
    }

    private String formatTime(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format(java.util.Locale.getDefault(), "%02d:%02d", hours, minutes);
    }
}