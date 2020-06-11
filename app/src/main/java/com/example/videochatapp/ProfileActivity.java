package com.example.videochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private String recieverUserID = "", recieverUserImage= "", recieverUserName = "";
    private TextView nameProfile;
    private ImageView background_profile_view;
    private Button addFriend, declineFriend;

    private FirebaseAuth mAuth;
    private String senderUserID;
    private String currentState = "new";
    private DatabaseReference friendReqRef, contactsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        senderUserID = mAuth.getCurrentUser().getUid();
        friendReqRef = FirebaseDatabase.getInstance().getReference().child("Freind Rquest");
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");

        recieverUserID = getIntent().getExtras().get("visit_user_id").toString();
        recieverUserImage = getIntent().getExtras().get("profile_image").toString();
        recieverUserName = getIntent().getExtras().get("profile_name").toString();

        background_profile_view = findViewById(R.id.background_profile_view);
        nameProfile = findViewById(R.id.name_profile);
        addFriend = findViewById(R.id.add_friends);
        declineFriend = findViewById(R.id.cancel_friends);

        Picasso.get().load(recieverUserImage).into(background_profile_view);
        nameProfile.setText(recieverUserName);

        manageClickEvents();
    }
    private void manageClickEvents() {
        friendReqRef.child(senderUserID)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(recieverUserID)){
                    String requestType = dataSnapshot.child(recieverUserID)
                          .child("request_type").getValue().toString();
                    if(requestType.equals("sent")){
                        currentState = "request_sent";
                        addFriend.setText("Cancel Friend Request");
                    }else if(requestType.equals("recieve")){

                        currentState = "request_recieve";
                        addFriend.setText("Accept Friend Request");
                        declineFriend.setVisibility(View.VISIBLE);
                        declineFriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CancelFriendRequest();
                            }
                        });

                    }
                }else{
                    contactsRef.child(senderUserID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(recieverUserID)) {
                                        currentState = "friend";
                                        addFriend.setText("Delete Contact");
                                    }else{
                                        currentState = "new";
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

            if(senderUserID.equals(recieverUserID)){
                addFriend.setVisibility(View.GONE);
            }else{
                addFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(currentState.equals("new")){
                            SendFriendRequest();

                        } if(currentState.equals("request_sent")){
                            CancelFriendRequest();

                        } if(currentState.equals("request_recieve")){
                            acceptFriendRequest();

                        } if(currentState.equals("request_sent")){
                            CancelFriendRequest();

                        }
                    }
                });
            }
        }

    private void acceptFriendRequest() {
        contactsRef.child(senderUserID).child(recieverUserID)
                .child("Contact").setValue("Saved")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            contactsRef.child(recieverUserID).child(senderUserID)
                                    .child("Contact").setValue("Saved")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                friendReqRef.child(senderUserID).child(recieverUserID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    friendReqRef.child(senderUserID).child(recieverUserID)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        currentState = "Friends";
                                                                                        addFriend.setText("Delete Contact");
                                                                                        declineFriend.setVisibility(View.GONE);
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }



    private void CancelFriendRequest() {
        friendReqRef.child(senderUserID).child(recieverUserID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            friendReqRef.child(senderUserID).child(recieverUserID)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                currentState = "New";
                                                addFriend.setText("Add Friend");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void SendFriendRequest() {
        friendReqRef.child(senderUserID).child(recieverUserID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            friendReqRef.child(recieverUserID).child(senderUserID)
                                    .child("request_type").setValue("recieve")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                currentState = "request_sent";
                                                addFriend.setText("Cancel Friend Request");
                                                Toast.makeText(ProfileActivity.this, "Friend Request Send", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}


