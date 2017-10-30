package com.fantastic.bookxchange.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Chat;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.utils.DefaultValueEventListener;
import com.fantastic.bookxchange.utils.GlideUtils;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m3libea on 10/13/17.
 */

public class BubbleAdapter extends RecyclerView.Adapter<BubbleAdapter.ViewHolder> {

    private List<Chat> chats;
    private Context context;

    public BubbleAdapter(Context context) {
        this.context = context;
        this.chats = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tweetView = LayoutInflater.from(context)
                .inflate(R.layout.item_bubble, parent, false);
        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Chat chat = chats.get(position);
        holder.bind(chat);
    }

    public void addChat(Chat chat) {
        chats.add(chat);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvMessage;
        ImageView ivProfile;

        public ViewHolder(View itemView) {
            super(itemView);

            tvDate = itemView.findViewById(R.id.tvDate);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }

        public void bind(Chat chat) {
            tvDate.setText(chat.getDate());
            tvMessage.setText(chat.getMessage());
            FirebaseDatabase.getInstance().getReference("users")
                    .child(chat.getFrom())
                    .addValueEventListener((DefaultValueEventListener) dataSnapshot -> {
                        User user = dataSnapshot.getValue(User.class);
                        if (user == null || user.getUrlProfileImage() == null) {
                            return;
                        }
                        GlideUtils.loadImageCircular(context, ivProfile, user.getUrlProfileImage());
                    });
        }
    }
}
