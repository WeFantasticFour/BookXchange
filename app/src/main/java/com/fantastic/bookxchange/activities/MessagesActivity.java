package com.fantastic.bookxchange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.MessageAdapter;
import com.fantastic.bookxchange.models.Message;
import com.fantastic.bookxchange.models.Room;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.utils.DataTest;
import com.fantastic.bookxchange.utils.ListDivider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

public class MessagesActivity extends BaseActivity implements MessageAdapter.MessageListListener {

    private static final String TAG = MessagesActivity.class.getSimpleName();
    private RecyclerView rvMessages;
    private MessageAdapter messageAdapter;

    private HashMap<User, List<Message>> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fetchData();
        setupRecyclerView();
        setupToolbar();
        loadChatData();
    }

    private void loadChatData() {
        Query query = FirebaseDatabase.getInstance()
                .getReference("rooms")
                .orderByChild(me.getUid())
                .equalTo(true, me.getUid())
                .limitToLast(100);
        Log.i(TAG, "loadChatData: query :: " + query.getRef().toString());
        Log.i(TAG, "loadChatData: me.getUid() :: " + me.getUid());
        query.getRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i(TAG, "onChildAdded: SS: " + s + " dataSnapshot :: " + dataSnapshot.getValue());
                Room room = dataSnapshot.getValue(Room.class);
                //room.setRoomId(s);
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if(data.getValue() instanceof Boolean && !data.getKey().startsWith(me.getUid())){
                        String[] yous = data.getKey().split("~");
                        if(yous.length>1){
                            Log.i(TAG, "onChildAdded: yous :: "+yous[1]);
                            room.setYou(yous[1]);
                        }
                    }
                }
                messageAdapter.add(room);
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

    private void fetchData() {
        //TODO Call to endpoint on firebase, we assume that the last one came first.

        List<Message> mMessages = DataTest.getMessages();
        messages = Message.getMap(mMessages);
    }

    private void setupToolbar() {
        enableToolbarBackButton();
        showToolbarBackButton();
        setTitle("Direct Messages");
    }

    private void setupRecyclerView() {
        rvMessages = findViewById(R.id.rvList);
        messageAdapter = new MessageAdapter(this, Message.getLastUnique(messages));
        messageAdapter.setListener(this);
        rvMessages.setAdapter(messageAdapter);

        LinearLayoutManager lyManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(lyManager);
        ListDivider line = new ListDivider(this);
        rvMessages.addItemDecoration(line);
    }

    @Override
    public void onClickListener(String roomId) {
        Intent i = new Intent(this, ConversationActivity.class);
        Log.i(TAG, "onClickListener: roomId======> "+roomId);
        i.putExtra("roomId", roomId);
        startActivity(i);
    }
}
