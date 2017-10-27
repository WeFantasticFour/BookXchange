package com.fantastic.bookxchange.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Message;
import com.fantastic.bookxchange.models.Room;
import com.fantastic.bookxchange.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by m3libea on 10/13/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    MessageListListener mListener;
    private List<Message> messages;
    private List<Room> rooms;
    private Context context;


    public MessageAdapter(Context context, List<Message> m) {
        this.context = context;
        this.messages = m;
        this.rooms = new ArrayList<>();
    }

    public void setListener(MessageListListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tweetView = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Room room = rooms.get(position);
        holder.bind(room);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public void add(Room room) {
        rooms.add(room);
        notifyDataSetChanged();
    }

    public interface MessageListListener {
        void onClickListener(String  roomId);
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


        public void bind(Room room) {
            tvUsername.setText(room.getYou());
            tvDate.setText(new SimpleDateFormat("MM/dd/yyyy HH:mm").format(new Date(room.getTime())));
            tvMessage.setText(room.getLastMessage());
            llMessage.setOnClickListener(view -> mListener.onClickListener(room.getRoomId()));
            ivProfile.setVisibility(View.GONE);

            /*
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
            */
        }
    }
}
