package com.example.videochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotoficationActivity extends AppCompatActivity {
    private RecyclerView NotificationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notofication);

        NotificationList = findViewById(R.id.myNotificationList);
        NotificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView userNametxt;
        Button acceptbtn, cancelBtn;
        ImageView profileImageView;
        RelativeLayout cardView;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            userNametxt = itemView.findViewById(R.id.name_notification);
            acceptbtn = itemView.findViewById(R.id.request_accept_btn);
            cancelBtn = itemView.findViewById(R.id.request_decline_btn);
            profileImageView = itemView.findViewById(R.id.image_notification);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
