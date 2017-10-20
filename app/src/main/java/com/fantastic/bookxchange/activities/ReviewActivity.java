package com.fantastic.bookxchange.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.ReviewsAdapter;
import com.fantastic.bookxchange.models.Review;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.utils.ListDivider;

import org.parceler.Parcels;

import java.util.List;

public class ReviewActivity extends BaseActivity {

    private RecyclerView rvReviews;
    private ReviewsAdapter aReviews;

    private User user;
    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        user = Parcels.unwrap(getIntent().getExtras().getParcelable("user"));

        reviews = user.getReviews();

        setupRecyclerView();
        setupToolbar();
    }

    private void setupToolbar() {
        enableToolbarBackButton();
        showToolbarBackButton();
        setTitle(user.getName());
    }

    private void setupRecyclerView() {
        rvReviews = findViewById(R.id.rvReviews);
        aReviews = new ReviewsAdapter(this, reviews);
        rvReviews.setAdapter(aReviews);

        LinearLayoutManager lyManager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(lyManager);
        //Line between rows
        ListDivider line = new ListDivider(this);
        rvReviews.addItemDecoration(line);
    }
}
