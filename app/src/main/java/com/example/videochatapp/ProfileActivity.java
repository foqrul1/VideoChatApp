package com.example.videochatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private String recieverUserID = "", recieverUserImage= "", recieverUserName = "";
    private TextView nameProfile;
    private ImageView background_profile_view;
    private Button addFriend, declineFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        recieverUserID = getIntent().getExtras().get("visit_user_id").toString();
        recieverUserImage = getIntent().getExtras().get("profile_image").toString();
        recieverUserName = getIntent().getExtras().get("profile_name").toString();

        background_profile_view = findViewById(R.id.background_profile_view);
        nameProfile = findViewById(R.id.name_profile);
        addFriend = findViewById(R.id.add_friends);
        declineFriend = findViewById(R.id.cancel_friends);

        Picasso.get().load(recieverUserImage).into(background_profile_view);
        nameProfile.setText(recieverUserName);
    }
}
