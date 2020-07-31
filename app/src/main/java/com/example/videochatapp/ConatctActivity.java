package com.example.videochatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ConatctActivity extends AppCompatActivity {
    BottomNavigationView navView;
    RecyclerView myContactList;
    ImageView findPeopleBtn;
    private DatabaseReference contactsRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String userName="", profileImage="";
    private String calledBy="";

    private RecyclerView recyclerView_story;
    private StoryAdapter storyAdapter;
    private List<Story> storyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BottomNavigationView navView = findViewById(R.id.nav_view);

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        findPeopleBtn = findViewById(R.id.find_people_btn);
        myContactList = findViewById(R.id.contact_list);
        myContactList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView_story = findViewById(R.id.recycler_view_story);
        recyclerView_story.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_story.setLayoutManager(linearLayoutManager);
        storyList =  new ArrayList<>();
        storyAdapter = new StoryAdapter(getApplicationContext(), storyList);
        recyclerView_story.setAdapter(storyAdapter);




        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findPeopleIntent = new Intent(ConatctActivity.this, FindPeopleActivity.class);
                startActivity(findPeopleIntent);
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.navigation_home:
                    Intent mainIntent = new Intent(ConatctActivity.this, ConatctActivity.class);
                    startActivity(mainIntent);
                    break;

                case R.id.navigation_settings:
                    Intent settingsIntent = new Intent(ConatctActivity.this, SettingsActivity.class);
                    startActivity(settingsIntent);
                    break;

                case R.id.navigation_notifications:
                    Intent notificationsIntent = new Intent(ConatctActivity.this, NotoficationActivity.class);
                    startActivity(notificationsIntent);
                    break;

                case R.id.navigation_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent logoutIntent = new Intent(ConatctActivity.this, RegisterActivity.class);
                    startActivity(logoutIntent);
                    finish();
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();


        checkForReceivingCall();


        validateUser();


        FirebaseRecyclerOptions<Contacts> options
                = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(contactsRef.child(currentUserId), Contacts.class).build();

        FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int i, @NonNull Contacts contacts) {
                final String listUserId = getRef(i).getKey();

                usersRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            userName = dataSnapshot.child("name").getValue().toString();
                            profileImage = dataSnapshot.child("image").getValue().toString();

                            holder.userNameTxt.setText(userName);
                            Picasso.get().load(profileImage).into(holder.profileImageView);

                        }

                        holder.callBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent callingIntent = new Intent(ConatctActivity.this, CallingActivity.class);
                                callingIntent.putExtra("visit_user_id", listUserId);
                                finish();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_design, parent, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };
        myContactList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class ContactsViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTxt;
        Button callBtn;
        ImageView profileImageView;


        public ContactsViewHolder(@NonNull View itemView){
            super(itemView);

            userNameTxt = itemView.findViewById(R.id.name_contact);
            callBtn = itemView.findViewById(R.id.call_btn);
            profileImageView = itemView.findViewById(R.id.image_contact);
        }
    }

    private void validateUser() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    Intent settingIntent = new Intent(ConatctActivity.this, SettingsActivity.class);
                    startActivity(settingIntent);
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkForReceivingCall() {

        usersRef.child(currentUserId).child("Ringing").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("ringing")){
                    calledBy = dataSnapshot.child("ringing").getValue().toString();
                    Intent callingIntent = new Intent(ConatctActivity.this, CallingActivity.class);
                    callingIntent.putExtra("visit_user_id", calledBy);
                    startActivity(callingIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void readStory(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long timecurrent  = System.currentTimeMillis();
                storyList.clear();
                storyList.add(new Story("", 0, 0, "",
                        FirebaseAuth.getInstance().getCurrentUser().getUid()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        })
    }

}
