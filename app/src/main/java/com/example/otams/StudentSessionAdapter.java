package com.example.otams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StudentSessionAdapter extends RecyclerView.Adapter<StudentSessionAdapter.ViewHolder> {

    private List<StudentSession> sessions;
    private OnSessionInteractionListener interactionListener;

    public interface OnSessionInteractionListener {
        void onCancel(StudentSession session);
        void onAddToCalendar(StudentSession session);
    }

    public StudentSessionAdapter(List<StudentSession> sessions, OnSessionInteractionListener listener) {
        this.sessions = sessions;
        this.interactionListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentSession session = sessions.get(position);

        holder.courseText.setText(session.getCourse());
        holder.tutorText.setText("Tutor: " + session.getTutorEmail());
        holder.dateText.setText(formatDate(session.getDate()));
        holder.timeText.setText(formatTimeRange(session.getStartTime(), session.getEndTime()));

        // Set status with color
        String status = session.getStatus();
        holder.statusText.setText("Status: " + status.toUpperCase());

        switch (status) {
            case "pending":
                holder.statusText.setTextColor(0xFFFFA500); // Orange
                break;
            case "approved":
                holder.statusText.setTextColor(0xFF4CAF50); // Green
                break;
            case "rejected":
            case "cancelled":
                holder.statusText.setTextColor(0xFFF44336); // Red
                break;
        }

        // Reset visibility
        holder.cancelButton.setVisibility(View.GONE);
        holder.addToCalendarButton.setVisibility(View.GONE);

        // Show cancel button only for upcoming sessions that can be cancelled
        if (interactionListener != null && session.canCancel()) {
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setOnClickListener(v -> interactionListener.onCancel(session));
        }


        // Show calendar button only for approved sessions
        if (interactionListener != null && "approved".equals(status)) {
            holder.addToCalendarButton.setVisibility(View.VISIBLE);
            holder.addToCalendarButton.setOnClickListener(v -> interactionListener.onAddToCalendar(session));
        }
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    private String formatDate(int dateInt) {
        int year = dateInt / 10000;
        int month = (dateInt % 10000) / 100;
        int day = dateInt % 100;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
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
        return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseText;
        TextView tutorText;
        TextView dateText;
        TextView timeText;
        TextView statusText;
        Button cancelButton;
        Button addToCalendarButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseText = itemView.findViewById(R.id.courseText);
            tutorText = itemView.findViewById(R.id.tutorText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            statusText = itemView.findViewById(R.id.statusText);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            addToCalendarButton = itemView.findViewById(R.id.addToCalendarButton);
        }
    }
}