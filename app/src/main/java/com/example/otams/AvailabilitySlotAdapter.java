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

public class AvailabilitySlotAdapter extends RecyclerView.Adapter<AvailabilitySlotAdapter.ViewHolder> {

    private List<Slot> slots;
    private OnSlotActionListener actionListener;

    public interface OnSlotActionListener {
        void onDeleteSlot(int position);
    }

    public AvailabilitySlotAdapter(List<Slot> slots, OnSlotActionListener actionListener) {
        this.slots = slots;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_availability_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Slot slot = slots.get(position);

        holder.dateText.setText(formatDate(slot.getDate()));
        holder.timeText.setText(formatTimeRange(slot.getStartTime(), slot.getEndTime()));

        holder.deleteButton.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDeleteSlot(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    private String formatDate(int dateInt) {
        // dateInt is in format YYYYMMDD
        int year = dateInt / 10000;
        int month = (dateInt % 10000) / 100;
        int day = dateInt % 100;

        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); // month - 1 because Calendar months are 0-indexed

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
        TextView dateText;
        TextView timeText;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.slotDateText);
            timeText = itemView.findViewById(R.id.slotTimeText);
            deleteButton = itemView.findViewById(R.id.deleteSlotButton);
        }
    }
}