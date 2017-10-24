package com.fantastic.bookxchange.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Message;
import com.fantastic.bookxchange.models.User;

import java.util.List;

/**
 * Created by m3libea on 10/13/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    MessageListListener mListener;
    private List<Message> messages;
    private Context context;


    public MessageAdapter(Context context, List<Message> m) {
        this.context = context;
        this.messages = m;
    }

    public void setListener(MessageListListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_message, parent, false);

        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public interface MessageListListener {
        void onClickListener(User user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvDate;
        TextView tvMessage;
        ImageView ivProfile;
        LinearLayout llMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            llMessage = itemView.findViewById(R.id.llMessage);
        }

        public void bind(Message message) {
            tvUsername.setText(message.getSenderUser().getName());
            tvDate.setText(message.getRelativeDate());
            tvMessage.setText(message.getText());

            llMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickListener(message.getSenderUser());
                }
            });

            if (message.getSenderUser().getUrlProfileImage() != null) {
                ivProfile.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(message.getSenderUser().getUrlProfileImage())
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.ic_person_24dp)
                        .into(new BitmapImageViewTarget(ivProfile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivProfile.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                ivProfile.setVisibility(View.GONE);
            }
        }
    }
}
