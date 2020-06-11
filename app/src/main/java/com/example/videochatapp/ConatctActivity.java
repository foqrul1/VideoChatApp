package com.example.videochatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ConatctActivity extends AppCompatActivity {
    private BottomNavigationView navView;
    private RecyclerView mContactList;
    private ImageView findPeopleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BottomNavigationView navView = findViewById(R.id.nav_view);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findPeopleBtn = findViewById(R.id.findPeoplebtn);
        mContactList = findViewById(R.id.contact_List);
        mContactList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConatctActivity.this, FindPeopleActivity.class);
                startActivity(intent);
            }

        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.navigation_home:
                    Intent intent = new Intent(ConatctActivity.this, ConatctActivity.class);
                    startActivity(intent);
                    break;
                case R.id.navigation_notifications:
                    Intent NotificationIntent = new Intent(ConatctActivity.this, NotoficationActivity.class);
                    startActivity(NotificationIntent);
                    break;
                case R.id.navigation_settings:
                    Intent settingsIntent = new Intent(ConatctActivity.this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    break;
                case R.id.navigation_logout:
                    Intent logoutIntent = new Intent(ConatctActivity.this, RegisterActivity.class);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(logoutIntent);
                    finish();
                    break;

            }
            return true;
        }
    };

}
