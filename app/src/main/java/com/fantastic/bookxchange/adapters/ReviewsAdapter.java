package com.fantastic.bookxchange.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.models.Review;

import java.util.List;

/**
 * Created by m3libea on 10/13/17.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private List<Review> reviews;
    private Context context;


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvDate;
        RatingBar rbStars;
        TextView tvReview;
        ImageView ivProfile;

        public ViewHolder(View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvReview = itemView.findViewById(R.id.tvReview);
            rbStars = itemView.findViewById(R.id.rbStars);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }

        public void bind(Review review){
            tvUsername.setText(review.getAuthor().getName());
            tvDate.setText(review.getFormattedDate());
            tvReview.setText(review.getReview());
            rbStars.setRating(review.getStars());
            LayerDrawable stars = (LayerDrawable) rbStars.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null), PorterDuff.Mode.SRC_ATOP);


            if(review.getAuthor().getUrlProfileImage()!= null){
                ivProfile.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(review.getAuthor().getUrlProfileImage())
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
    public ReviewsAdapter(Context context, List<Review> r) {
        this.context = context;
        this.reviews = r;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_review, parent, false);

        return new ViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
