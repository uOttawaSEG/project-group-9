package com.example.otams;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class TutorHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        //TabLayout Code
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        Button logoutButton = findViewById(R.id.logoutButton);

        TutorPagerAdapter tutorPagerAdapter = new TutorPagerAdapter(this);
        viewPager.setAdapter(tutorPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0 : tab.setText("Availability"); break;
                case 1 : tab.setText("Requests"); break;
                case 2 : tab.setText("Sessions"); break;
            }
        }).attach();

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(TutorHome.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TutorHome.this, LoginPage.class));
            finish();
        });
    }
}