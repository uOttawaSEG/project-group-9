package com.example.otams;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.widget.Button;
import androidx.core.view.WindowInsetsCompat;

public class RejectedRequestsPage extends AppCompatActivity {
    Button registrationRequestsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inbox_rejection_request_page);

        registrationRequestsButton = findViewById(R.id.Move2RegReq);

        //Once the Registration Requests button is reclicked, it will take user back to AdminHomePage
        registrationRequestsButton.setOnClickListener(v -> {
            Intent regRequest = new Intent(RejectedRequestsPage.this, AdministratorHome.class);
            startActivity(regRequest);
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}

