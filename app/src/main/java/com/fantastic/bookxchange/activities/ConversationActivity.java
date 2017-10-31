package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.BubbleAdapter;
import com.fantastic.bookxchange.models.Chat;
import com.fantastic.bookxchange.models.Message;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.utils.DefaultChildEventListener;
import com.google.firebase.database.DataSnapshot;
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
        FirebaseDatabase.getInstance()
                .getReference("messages")
                .child(roomId).addChildEventListener(new DefaultChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Chat c = dataSnapshot.getValue(Chat.class);
                aBubble.addChat(c);
                //Set title
                if(!me.getUid().equals(c.getFrom())) {
                    setTitle(c.getFromName());
                }
            }
        });
    }
}
