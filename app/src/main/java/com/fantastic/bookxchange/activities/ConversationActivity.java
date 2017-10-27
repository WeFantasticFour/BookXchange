package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.BubbleAdapter;
import com.fantastic.bookxchange.models.Message;
import com.fantastic.bookxchange.models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by m3libea on 10/21/17.
 */

public class ConversationActivity extends BaseActivity {

    private static final String TAG = ConversationActivity.class.getSimpleName();
    User user;
    ArrayList<Message> messages;
    private RecyclerView rvMessages;
    private BubbleAdapter aBubble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        String roomId = getIntent().getStringExtra("roomId");
        Log.i(TAG, "onCreate: roomId === " + roomId);
        loadChatData(roomId);
        setupRecyclerView();
        setupToolbar();
    }

    private void setupToolbar() {
        enableToolbarBackButton();
        showToolbarBackButton();
        //setTitle(user.getName());
    }

    private void setupRecyclerView() {
        rvMessages = findViewById(R.id.rvList);
        aBubble = new BubbleAdapter(this, messages);
        rvMessages.setAdapter(aBubble);

        LinearLayoutManager lyManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(lyManager);
    }

    private void loadChatData(String roomId) {
        Log.i(TAG, "loadChatData: =====roomId " + roomId);
        FirebaseDatabase.getInstance()
                .getReference("messages")
                .child(roomId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded: SS: " + s + " dataSnapshot :: " + dataSnapshot.getValue());
                /*
                Room room = dataSnapshot.getValue(Room.class);
                room.setRoomId(s);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.getValue() instanceof Boolean && !data.getKey().startsWith(me.getUid())) {
                        String[] yous = data.getKey().split("~");
                        if (yous.length > 1) {
                            Log.i(TAG, "onChildAdded: yous :: " + yous[1]);
                            room.setYou(yous[1]);
                        }
                    }
                }
                */
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildChanged: SS: " + s + " dataSnapshot :: " + dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildMoved: SS: " + s + " dataSnapshot :: " + dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        query.getRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Flow.of(dataSnapshot
                        .getChildren())
                        .forEach(data -> {
                            Log.i(TAG, "onDataChange: data " + data);
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })
        */

    }
}
