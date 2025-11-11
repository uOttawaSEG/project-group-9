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

public class SessionRequestAdapter extends RecyclerView.Adapter<SessionRequestAdapter.ViewHolder> {

    private List<SessionRequest> requests;
    private OnRequestActionListener actionListener;

    public interface OnRequestActionListener {
        void onApprove(SessionRequest request);
        void onReject(SessionRequest request);
    }

    public SessionRequestAdapter(List<SessionRequest> requests, OnRequestActionListener actionListener) {
        this.requests = requests;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pending_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SessionRequest request = requests.get(position);

        holder.studentNameText.setText(request.getStudentFullName());
        holder.studentEmailText.setText(request.getStudentEmail());
        holder.studentPhoneText.setText("Phone: " + request.getStudentPhone());
        holder.courseText.setText("Course: " + request.getCourse());
        holder.dateText.setText(formatDate(request.getDate()));
        holder.timeText.setText(formatTimeRange(request.getStartTime(), request.getEndTime()));

        holder.approveButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onApprove(request);
            }
        });

        holder.rejectButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onReject(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
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
        TextView studentNameText;
        TextView studentEmailText;
        TextView studentPhoneText;
        TextView courseText;
        TextView dateText;
        TextView timeText;
        Button approveButton;
        Button rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameText = itemView.findViewById(R.id.studentNameText);
            studentEmailText = itemView.findViewById(R.id.studentEmailText);
            studentPhoneText = itemView.findViewById(R.id.studentPhoneText);
            courseText = itemView.findViewById(R.id.courseText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}