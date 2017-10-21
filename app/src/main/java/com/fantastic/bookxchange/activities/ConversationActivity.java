package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.BubbleAdapter;
import com.fantastic.bookxchange.models.Message;
import com.fantastic.bookxchange.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by m3libea on 10/21/17.
 */

public class ConversationActivity extends BaseActivity {

    private RecyclerView rvMessages;
    private BubbleAdapter aBubble;

    User user;
    ArrayList<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        messages = Parcels.unwrap(getIntent().getExtras().getParcelable("messages"));
        user = messages.get(0).getSenderUser();
        setupRecyclerView();
        setupToolbar();
    }

    private void setupToolbar() {
        enableToolbarBackButton();
        showToolbarBackButton();
        setTitle(user.getName());
    }

    private void setupRecyclerView() {
        rvMessages = findViewById(R.id.rvList);
        aBubble = new BubbleAdapter(this, messages);
        rvMessages.setAdapter(aBubble);

        LinearLayoutManager lyManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(lyManager);
    }
}
