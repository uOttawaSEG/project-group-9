package com.example.otams;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private List<String> courses;
    private OnCourseClickListener clickListener;

    public interface OnCourseClickListener {
        void onCourseClick(String course);
    }

    public CourseAdapter(List<String> courses, OnCourseClickListener clickListener) {
        this.courses = courses;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String course = courses.get(position);
        holder.courseNameText.setText(course);

        holder.courseCard.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onCourseClick(course);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView courseCard;
        TextView courseNameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseCard = itemView.findViewById(R.id.courseCard);
            courseNameText = itemView.findViewById(R.id.courseNameText);
        }
    }
}