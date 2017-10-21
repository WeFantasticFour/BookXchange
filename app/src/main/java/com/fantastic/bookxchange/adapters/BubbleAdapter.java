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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Message;

import java.util.List;

/**
 * Created by m3libea on 10/13/17.
 */

public class BubbleAdapter extends RecyclerView.Adapter<BubbleAdapter.ViewHolder>{

    private List<Message> messages;
    private Context context;


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

        public void bind(Message message){
            tvDate.setText(message.getRelativeDate());
            tvMessage.setText(message.getText());


            if(message.getSenderUser().getUrlProfileImage()!= null) {
                ivProfile.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(message.getSenderUser().getUrlProfileImage())
                        .asBitmap()
                        .centerCrop()
                        .into(new BitmapImageViewTarget(ivProfile) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                ivProfile.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }else{
                ivProfile.setVisibility(View.GONE);
            }
        }
    }
    public BubbleAdapter(Context context, List<Message> m) {
        this.context = context;
        this.messages = m;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_bubble, parent, false);

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
}
