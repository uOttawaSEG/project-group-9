package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * StudentHome
 *
 * Activity representing the student's main home screen after a successful login or registration.
 * Allows students to search for courses and view available tutoring sessions.
 */
public class StudentHome extends AppCompatActivity {

    private TextInputEditText searchField;
    private RecyclerView coursesRecyclerView;
    private Button logoutButton;
    private CourseAdapter courseAdapter;
    private List<String> allCourses;
    private List<String> filteredCourses;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        allCourses = new ArrayList<>();
        filteredCourses = new ArrayList<>();

        initializeViews();
        setupRecyclerView();
        loadAvailableCourses();
        setupListeners();
    }

    private void initializeViews() {
        searchField = findViewById(R.id.searchField);
        coursesRecyclerView = findViewById(R.id.coursesRecyclerView);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void setupRecyclerView() {
        courseAdapter = new CourseAdapter(filteredCourses, course -> {
            // When a course is clicked, show available sessions for that course
            Intent intent = new Intent(StudentHome.this, AvailableSessionsActivity.class);
            intent.putExtra("course", course);
            startActivity(intent);
        });

        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        coursesRecyclerView.setAdapter(courseAdapter);
    }

    private void loadAvailableCourses() {
        // Get all unique courses from availability slots
        db.collection("availabilitySlots")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Set<String> coursesSet = new HashSet<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String course = document.getString("course");
                        if (course != null && !course.isEmpty()) {
                            coursesSet.add(course);
                        }
                    }

                    allCourses.clear();
                    allCourses.addAll(coursesSet);

                    filteredCourses.clear();
                    filteredCourses.addAll(allCourses);
                    courseAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentHome.this, "Error loading courses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void setupListeners() {
        // Search functionality
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCourses(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Logout functionality
        logoutButton.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(StudentHome.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(StudentHome.this, LoginPage.class));
            finish();
        });
    }

    private void filterCourses(String query) {
        filteredCourses.clear();

        if (query.isEmpty()) {
            filteredCourses.addAll(allCourses);
        } else {
            String lowerQuery = query.toLowerCase();
            for (String course : allCourses) {
                if (course.toLowerCase().contains(lowerQuery)) {
                    filteredCourses.add(course);
                }
            }
        }

        courseAdapter.notifyDataSetChanged();
    }
}