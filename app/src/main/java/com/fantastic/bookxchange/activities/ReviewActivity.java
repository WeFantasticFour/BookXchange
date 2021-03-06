package com.fantastic.bookxchange.activities;

import android.content.Intent;
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

public class ReviewActivity extends BaseActivity implements ReviewsAdapter.ReviewListener {

    private RecyclerView rvReviews;
    private ReviewsAdapter aReviews;

    private User user;
    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

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
        rvReviews = findViewById(R.id.rvList);
        aReviews = new ReviewsAdapter(this, reviews);
        aReviews.setListener(this);
        rvReviews.setAdapter(aReviews);

        LinearLayoutManager lyManager = new LinearLayoutManager(this);
        rvReviews.setLayoutManager(lyManager);
        //Line between rows
        ListDivider line = new ListDivider(this);
        rvReviews.addItemDecoration(line);
    }

    @Override
    public void onUsernameClickListener(User user) {
        Intent i = new Intent(this, UserActivity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivity(i);
    }
}
