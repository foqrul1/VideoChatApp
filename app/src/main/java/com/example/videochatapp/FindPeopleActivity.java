 package com.example.videochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

 public class FindPeopleActivity extends AppCompatActivity {
    private RecyclerView findFriendList;
    private EditText searchEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);

        searchEt = findViewById(R.id.editText_search);
        findFriendList = findViewById(R.id.findFriends);
        findFriendList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }
     public static class NotificationViewHolder extends RecyclerView.ViewHolder {
         TextView userNametxt;
         Button videoCallBtn;
         ImageView profileImageView;
         RelativeLayout cardView;


         public NotificationViewHolder(@NonNull View itemView) {
             super(itemView);

             userNametxt = itemView.findViewById(R.id.contact);
             videoCallBtn = itemView.findViewById(R.id.call_btn);
             profileImageView = itemView.findViewById(R.id.image_contact);
             cardView = itemView.findViewById(R.id.cardView1);
         }
     }

}
