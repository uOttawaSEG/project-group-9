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

public class AvailableSessionAdapter extends RecyclerView.Adapter<AvailableSessionAdapter.ViewHolder> {

    private List<AvailableSession> sessions;
    private OnSessionRequestListener requestListener;

    public interface OnSessionRequestListener {
        void onRequestSession(AvailableSession session);
    }

    public AvailableSessionAdapter(List<AvailableSession> sessions, OnSessionRequestListener requestListener) {
        this.sessions = sessions;
        this.requestListener = requestListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_available_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AvailableSession session = sessions.get(position);

        holder.tutorText.setText("Tutor: " + session.getTutorEmail());
        holder.dateText.setText(formatDate(session.getDate()));
        holder.timeText.setText(formatTimeRange(session.getStartTime(), session.getEndTime()));
        holder.approvalText.setText(session.isRequiresApproval() ? "Requires Approval" : "Instant Booking");

        holder.requestButton.setOnClickListener(v -> {
            if (requestListener != null) {
                requestListener.onRequestSession(session);
            }
        });
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
        TextView tutorText;
        TextView dateText;
        TextView timeText;
        TextView approvalText;
        Button requestButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tutorText = itemView.findViewById(R.id.tutorText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            approvalText = itemView.findViewById(R.id.approvalText);
            requestButton = itemView.findViewById(R.id.requestButton);
        }
    }
}