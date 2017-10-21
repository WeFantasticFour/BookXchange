package com.fantastic.bookxchange.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.MessageAdapter;
import com.fantastic.bookxchange.models.Message;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.utils.DataTest;
import com.fantastic.bookxchange.utils.ListDivider;

import org.parceler.Parcels;

import java.util.HashMap;
import java.util.List;

public class MessagesActivity extends BaseActivity implements MessageAdapter.MessageListListener {

    private RecyclerView rvMessages;
    private MessageAdapter aMessages;

    private HashMap<User, List<Message>> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fetchData();

        setupRecyclerView();
        setupToolbar();
    }

    private void fetchData() {
        //TODO Call to endpoint on firebase, we assume that the last one came first.

        List<Message> mMessages =DataTest.getMessages();
        messages = Message.getMap(mMessages);
    }

    private void setupToolbar() {
        enableToolbarBackButton();
        showToolbarBackButton();
        setTitle("Direct Messages");
    }

    private void setupRecyclerView() {
        rvMessages = findViewById(R.id.rvList);
        aMessages = new MessageAdapter(this, Message.getLastUnique(messages));
        aMessages.setListener(this);
        rvMessages.setAdapter(aMessages);

        LinearLayoutManager lyManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(lyManager);
        //Line between rows
        ListDivider line = new ListDivider(this);
        rvMessages.addItemDecoration(line);
    }

    @Override
    public void onClickListener(User user) {
        Intent i = new Intent(this, ConversationActivity.class);
        i.putExtra("messages", Parcels.wrap(messages.get(user)));
        startActivity(i);
    }
}
