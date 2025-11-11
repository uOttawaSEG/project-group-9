package com.example.otams;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailabilityFragment extends Fragment {

    private DatePicker datePicker;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;
    private Spinner courseSpinner;
    private SwitchMaterial approvalSwitch;
    private Button addSlotButton;
    private RecyclerView availabilityRecyclerView;

    private AvailabilitySlotAdapter adapter;
    private List<Slot> availabilitySlots;
    private Tutor currentTutor;
    private List<String> tutorCourses;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public AvailabilityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        availabilitySlots = new ArrayList<>();
        tutorCourses = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize current tutor
        currentTutor = new Tutor(auth.getCurrentUser().getEmail());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_availability, container, false);

        initializeViews(view);
        setupRecyclerView();
        loadTutorCourses();
        setupListeners();
        loadExistingSlots();

        return view;
    }

    private void initializeViews(View view) {
        datePicker = view.findViewById(R.id.datePicker);
        startTimePicker = view.findViewById(R.id.startTimePicker);
        endTimePicker = view.findViewById(R.id.endTimePicker);
        courseSpinner = view.findViewById(R.id.courseSpinner);
        approvalSwitch = view.findViewById(R.id.approvalSwitch);
        addSlotButton = view.findViewById(R.id.addSlotButton);
        availabilityRecyclerView = view.findViewById(R.id.availabilityRecyclerView);

        // Set 24-hour format
        startTimePicker.setIs24HourView(true);
        endTimePicker.setIs24HourView(true);

        // Set minimum date to today
        datePicker.setMinDate(System.currentTimeMillis());

        // Set initial time to nearest 30-minute interval
        Calendar now = Calendar.getInstance();
        int currentMinute = now.get(Calendar.MINUTE);
        int roundedMinute = roundToNearest30(currentMinute);
        int currentHour = now.get(Calendar.HOUR_OF_DAY);

        if (currentMinute >= 45) {
            currentHour = (currentHour + 1) % 24;
        }

        startTimePicker.setHour(currentHour);
        startTimePicker.setMinute(roundedMinute);

        // Set end time to 30 minutes after start
        int endHour = currentHour;
        int endMinute = roundedMinute + 30;
        if (endMinute >= 60) {
            endMinute = 0;
            endHour = (endHour + 1) % 24;
        }

        endTimePicker.setHour(endHour);
        endTimePicker.setMinute(endMinute);
    }

    /**
     * Loads tutor's courses from Firestore and populates the spinner
     */
    private void loadTutorCourses() {
        String userId = auth.getCurrentUser().getUid();

        // First check if user is approved tutor
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists() && "Tutor".equals(documentSnapshot.getString("role"))) {
                        // Get courses from user document
                        Object coursesObj = documentSnapshot.get("courses");
                        if (coursesObj instanceof List) {
                            tutorCourses = (List<String>) coursesObj;
                            setupCourseSpinner();
                        }
                    } else {
                        // If not in users, check requests (pending tutor)
                        db.collection("requests")
                                .whereEqualTo("userId", userId)
                                .whereEqualTo("status", "approved")
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                                        Object coursesObj = doc.get("courses");
                                        if (coursesObj instanceof List) {
                                            tutorCourses = (List<String>) coursesObj;
                                            setupCourseSpinner();
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error loading courses: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Sets up the course spinner with tutor's courses
     */
    private void setupCourseSpinner() {
        if (tutorCourses.isEmpty()) {
            tutorCourses.add("No courses available");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                tutorCourses
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(adapter);
    }

    /**
     * Rounds minute to nearest 30-minute interval (0 or 30)
     */
    private int roundToNearest30(int minute) {
        if (minute < 15) return 0;
        if (minute < 45) return 30;
        return 0; // Will increment hour
    }

    private void setupRecyclerView() {
        adapter = new AvailabilitySlotAdapter(availabilitySlots, new AvailabilitySlotAdapter.OnSlotActionListener() {
            @Override
            public void onDeleteSlot(int position) {
                deleteSlot(position);
            }
        });

        availabilityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        availabilityRecyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        addSlotButton.setOnClickListener(v -> addAvailabilitySlot());
    }

    private void addAvailabilitySlot() {
        // Get selected date
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        // Get start time
        int startHour = startTimePicker.getHour();
        int startMinute = startTimePicker.getMinute();

        // Check if start minute is valid (0 or 30 only)
        if (startMinute != 0 && startMinute != 30) {
            Toast.makeText(getContext(), "Start time must be on the hour (:00) or half-hour (:30)", Toast.LENGTH_LONG).show();
            return;
        }

        int startTimeInMinutes = startHour * 60 + startMinute;

        // Get end time
        int endHour = endTimePicker.getHour();
        int endMinute = endTimePicker.getMinute();

        // Check if end minute is valid (0 or 30 only)
        if (endMinute != 0 && endMinute != 30) {
            Toast.makeText(getContext(), "End time must be on the hour (:00) or half-hour (:30)", Toast.LENGTH_LONG).show();
            return;
        }

        int endTimeInMinutes = endHour * 60 + endMinute;

        // Convert date to integer format YYYYMMDD
        int dateInt = year * 10000 + (month + 1) * 100 + day;

        // Get approval setting
        boolean requiresApproval = approvalSwitch.isChecked();

        // Get selected course
        String selectedCourse = (String) courseSpinner.getSelectedItem();
        if (selectedCourse == null || selectedCourse.equals("No courses available")) {
            Toast.makeText(getContext(), "Please select a valid course", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Calendar objects for validation
        Calendar selectedDateTime = Calendar.getInstance();
        selectedDateTime.set(year, month, day, startHour, startMinute, 0);
        selectedDateTime.set(Calendar.MILLISECOND, 0);

        Calendar now = Calendar.getInstance();

        // Validation
        if (!validateSlot(selectedDateTime, startTimeInMinutes, endTimeInMinutes, dateInt)) {
            return;
        }

        // Create multiple 30-minute slots
        int duration = endTimeInMinutes - startTimeInMinutes;
        int numberOfSlots = duration / 30;

        if (numberOfSlots < 1) {
            Toast.makeText(getContext(), "Error: End time must be at least 30 minutes after start time", Toast.LENGTH_LONG).show();
            return;
        }

        // Create each 30-minute slot
        for (int i = 0; i < numberOfSlots; i++) {
            int slotStart = startTimeInMinutes + (i * 30);
            int slotEnd = slotStart + 30;

            // Check for overlap before creating
            boolean hasOverlap = false;
            for (Slot existingSlot : availabilitySlots) {
                if (existingSlot.getDate() == dateInt) {
                    if (timesOverlap(slotStart, slotEnd, existingSlot.getStartTime(), existingSlot.getEndTime())) {
                        hasOverlap = true;
                        break;
                    }
                }
            }

            if (hasOverlap) {
                Toast.makeText(getContext(), "Error: One or more slots overlap with existing slots", Toast.LENGTH_LONG).show();
                return;
            }

            Slot slot = new Slot(currentTutor, slotStart, slotEnd, dateInt);
            availabilitySlots.add(slot);
            saveSlotToFirestore(slot, requiresApproval, selectedCourse);
        }

        adapter.notifyDataSetChanged();
        Toast.makeText(getContext(), numberOfSlots + " availability slot(s) added successfully", Toast.LENGTH_SHORT).show();
    }

    private boolean validateSlot(Calendar selectedDateTime, int startTime, int endTime, int date) {
        Calendar now = Calendar.getInstance();

        // Check if date/time is in the past
        if (selectedDateTime.before(now)) {
            Toast.makeText(getContext(), "Error: Cannot select a past date or time", Toast.LENGTH_LONG).show();
            return false;
        }

        // Check if end time is after start time
        if (endTime <= startTime) {
            Toast.makeText(getContext(), "Error: End time must be after start time", Toast.LENGTH_LONG).show();
            return false;
        }

        // Check if duration is in 30-minute intervals
        int duration = endTime - startTime;
        if (duration % 30 != 0) {
            Toast.makeText(getContext(), "Error: Duration must be in 30-minute intervals", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean timesOverlap(int start1, int end1, int start2, int end2) {
        // Two time ranges overlap if one starts before the other ends
        return start1 < end2 && start2 < end1;
    }

    private void deleteSlot(int position) {
        if (position >= 0 && position < availabilitySlots.size()) {
            Slot slot = availabilitySlots.get(position);
            availabilitySlots.remove(position);
            adapter.notifyItemRemoved(position);
            Toast.makeText(getContext(), "Availability slot deleted", Toast.LENGTH_SHORT).show();

            // Delete from Firestore
            deleteSlotFromFirestore(slot);
        }
    }

    private void saveSlotToFirestore(Slot slot, boolean requiresApproval, String course) {
        String userId = auth.getCurrentUser().getUid();

        Map<String, Object> slotData = new HashMap<>();
        slotData.put("tutorEmail", currentTutor.getEmail());
        slotData.put("date", slot.getDate());
        slotData.put("startTime", slot.getStartTime());
        slotData.put("endTime", slot.getEndTime());
        slotData.put("requiresApproval", requiresApproval);
        slotData.put("tutorId", userId);
        slotData.put("course", course);

        db.collection("availabilitySlots")
                .add(slotData)
                .addOnSuccessListener(documentReference -> {
                    // Success - slot saved
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error saving slot: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteSlotFromFirestore(Slot slot) {
        String userId = auth.getCurrentUser().getUid();

        db.collection("availabilitySlots")
                .whereEqualTo("tutorId", userId)
                .whereEqualTo("date", slot.getDate())
                .whereEqualTo("startTime", slot.getStartTime())
                .whereEqualTo("endTime", slot.getEndTime())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        queryDocumentSnapshots.getDocuments().get(0).getReference().delete();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting slot: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void loadExistingSlots() {
        String userId = auth.getCurrentUser().getUid();

        db.collection("availabilitySlots")
                .whereEqualTo("tutorId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    availabilitySlots.clear();
                    queryDocumentSnapshots.forEach(document -> {
                        int date = document.getLong("date").intValue();
                        int startTime = document.getLong("startTime").intValue();
                        int endTime = document.getLong("endTime").intValue();

                        Slot slot = new Slot(currentTutor, startTime, endTime, date);
                        availabilitySlots.add(slot);
                    });
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error loading slots: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}