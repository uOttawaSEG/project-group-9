package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otams.StudentSession;
import com.example.otams.StudentSessionAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Fragment showing student's booked/requested sessions
 */
public class StudentSessionsFragment extends Fragment implements StudentSessionAdapter.OnSessionInteractionListener {

    private static final String TAG = "StudentSessions";

    private RecyclerView upcomingRecyclerView;
    private RecyclerView pastRecyclerView;
    private TextView noUpcomingText;
    private TextView noPastText;

    private StudentSessionAdapter upcomingAdapter;
    private StudentSessionAdapter pastAdapter;
    private List<StudentSession> upcomingSessions;
    private List<StudentSession> pastSessions;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public StudentSessionsFragment() {
        // Required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upcomingSessions = new ArrayList<>();
        pastSessions = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_sessions, container, false);

        initializeViews(view);
        setupRecyclerViews();
        loadStudentSessions();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStudentSessions();
    }

    private void initializeViews(View view) {
        upcomingRecyclerView = view.findViewById(R.id.upcomingSessionsRecyclerView);
        pastRecyclerView = view.findViewById(R.id.pastSessionsRecyclerView);
        noUpcomingText = view.findViewById(R.id.noUpcomingText);
        noPastText = view.findViewById(R.id.noPastText);
    }

    private void setupRecyclerViews() {
        upcomingAdapter = new StudentSessionAdapter(upcomingSessions, this, null);
        pastAdapter = new StudentSessionAdapter(pastSessions, null, this::openRatingDialog); // No interaction for past

        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        upcomingRecyclerView.setAdapter(upcomingAdapter);
        pastRecyclerView.setAdapter(pastAdapter);
    }

    private void loadStudentSessions() {
        String studentId = auth.getCurrentUser().getUid();

        TimeZone timeZone = TimeZone.getTimeZone("America/Toronto");
        Calendar now = Calendar.getInstance(timeZone);
        int todayInt = now.get(Calendar.YEAR) * 10000 +
                (now.get(Calendar.MONTH) + 1) * 100 +
                now.get(Calendar.DAY_OF_MONTH);
        int currentTimeInMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);

        Log.d(TAG, "Loading sessions for studentId: " + studentId);

        // Load all session requests for this student
        db.collection("sessionRequests")
                .whereEqualTo("studentId", studentId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    upcomingSessions.clear();
                    pastSessions.clear();

                    Log.d(TAG, "Found " + queryDocumentSnapshots.size() + " session requests");

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        try {
                            String requestId = document.getId();
                            String slotId = document.getString("slotId");
                            String status = document.getString("status");
                            String course = document.getString("course");
                            String tutorEmail = document.getString("tutorEmail");
                            int date = document.getLong("date").intValue();
                            int startTime = document.getLong("startTime").intValue();
                            int endTime = document.getLong("endTime").intValue();
                            Long ratingLong = document.getLong("rating");
                            int rating = -1;
                            if (ratingLong != null) {
                                rating = ratingLong.intValue();
                            }

                            StudentSession session = new StudentSession(
                                    requestId, slotId, course, tutorEmail, date,
                                    startTime, endTime, status
                            );

                            session.setRating(rating);

                            // Determine if past or upcoming
                            boolean isPast = false;
                            if (date < todayInt) {
                                isPast = true;
                            } else if (date == todayInt && startTime <= currentTimeInMinutes) {
                                isPast = true;
                            }

                            if (isPast) {
                                pastSessions.add(session);
                            } else {
                                upcomingSessions.add(session);
                            }

                        } catch (Exception e) {
                            Log.e(TAG, "Error processing session document", e);
                        }
                    }

                    upcomingAdapter.notifyDataSetChanged();
                    pastAdapter.notifyDataSetChanged();
                    updateEmptyViews();

                    Log.d(TAG, "Upcoming: " + upcomingSessions.size() + ", Past: " + pastSessions.size());
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading sessions", e);
                    Toast.makeText(getContext(), "Error loading sessions: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void cancelSession(StudentSession session) {
        // Check if session is within 24 hours
        TimeZone timeZone = TimeZone.getTimeZone("America/Toronto");
        Calendar sessionDateTime = Calendar.getInstance(timeZone);
        int year = session.getDate() / 10000;
        int month = (session.getDate() % 10000) / 100;
        int day = session.getDate() % 100;
        int hour = session.getStartTime() / 60;
        int minute = session.getStartTime() % 60;

        sessionDateTime.set(year, month - 1, day, hour, minute);

        Calendar now = Calendar.getInstance(timeZone);
        long hoursUntilSession = (sessionDateTime.getTimeInMillis() - now.getTimeInMillis()) / (1000 * 60 * 60);

        if (hoursUntilSession < 24) {
            Toast.makeText(getContext(),
                    "Cannot cancel: Session is less than 24 hours away",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Update status to cancelled
        db.collection("sessionRequests")
                .document(session.getRequestId())
                .update("status", "cancelled")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Session cancelled successfully", Toast.LENGTH_SHORT).show();
                    loadStudentSessions(); // Refresh the list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error cancelling: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateEmptyViews() {
        if (upcomingSessions.isEmpty()) {
            noUpcomingText.setVisibility(View.VISIBLE);
            upcomingRecyclerView.setVisibility(View.GONE);
        } else {
            noUpcomingText.setVisibility(View.GONE);
            upcomingRecyclerView.setVisibility(View.VISIBLE);
        }

        if (pastSessions.isEmpty()) {
            noPastText.setVisibility(View.VISIBLE);
            pastRecyclerView.setVisibility(View.GONE);
        } else {
            noPastText.setVisibility(View.GONE);
            pastRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCancel(StudentSession session) {
        cancelSession(session);
    }

    @Override
    public void onAddToCalendar(StudentSession session) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "Tutoring Session: " + session.getCourse());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Tutoring session for " + session.getCourse() + " with " + session.getTutorEmail());

        TimeZone timeZone = TimeZone.getTimeZone("America/Toronto");

        Calendar startTime = Calendar.getInstance(timeZone);
        int year = session.getDate() / 10000;
        int month = (session.getDate() % 10000) / 100;
        int day = session.getDate() % 100;
        int startHour = session.getStartTime() / 60;
        int startMinute = session.getStartTime() % 60;
        startTime.set(year, month - 1, day, startHour, startMinute);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.getTimeInMillis());

        Calendar endTime = Calendar.getInstance(timeZone);
        int endHour = session.getEndTime() / 60;
        int endMinute = session.getEndTime() % 60;
        endTime.set(year, month - 1, day, endHour, endMinute);
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        
        intent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, "America/Toronto");

        if(intent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "No calendar app found. Please install a calendar app to use this feature.", Toast.LENGTH_LONG).show();
        }
    }

    private void openRatingDialog(StudentSession session) {
        View dialogView = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_rate_session, null);

        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);

        new AlertDialog.Builder(requireContext())
                .setTitle("Rate this session")
                .setView(dialogView)
                .setPositiveButton("Submit", (dialog, which) -> {
                    int stars = (int) ratingBar.getRating();
                    if (stars == 0) {
                        Toast.makeText(getContext(),
                                "Please select a rating", Toast.LENGTH_SHORT).show();
                    } else {
                        rateSessions(session, stars);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void rateSessions(StudentSession session, int stars) {
        db.collection("sessionRequests")
                .document(session.getRequestId())
                .update("rating", stars)
                .addOnSuccessListener(aVoid -> {
                    session.setRating(stars);
                    int position = pastSessions.indexOf(session);
                    if (position != -1) {
                        pastAdapter.notifyItemChanged(position);
                    }
                    Toast.makeText(getContext(),
                            "Rating submitted!",
                            Toast.LENGTH_SHORT).show();
                    updateTutorRating(session.getTutorEmail(), stars);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),
                            "Error submitting rating: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateTutorRating(String tutorEmail, int stars) {
        db.collection("tutors")
                .whereEqualTo("email", tutorEmail)
                .limit(1)
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        DocumentSnapshot doc = query.getDocuments().get(0);
                        Tutor tutor = doc.toObject(Tutor.class);
                        if (tutor == null) return;
                        tutor.addRating(stars);
                        doc.getReference().update(
                                "totalRatingPoints", tutor.getTotalRatingPoints(),
                                "totalRatings", tutor.getTotalRatings()
                        );
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(),
                            "Error updating tutor rating: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}