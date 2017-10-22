package com.fantastic.bookxchange.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.BookFragmentPagerAdapter;
import com.fantastic.bookxchange.fragments.BaseBookListFragment;
import com.fantastic.bookxchange.fragments.BookDetailFragment;
import com.fantastic.bookxchange.fragments.ExchangeListFragment;
import com.fantastic.bookxchange.fragments.MessageFragment;
import com.fantastic.bookxchange.fragments.ReviewFragment;
import com.fantastic.bookxchange.fragments.ShareListFragment;
import com.fantastic.bookxchange.fragments.WishListFragment;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.Review;
import com.fantastic.bookxchange.models.User;
import com.github.clans.fab.FloatingActionMenu;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserActivity extends BaseActivity implements BaseBookListFragment.BookListClickListener,
        BaseBookListFragment.BookListReadyListener,
        ReviewFragment.ReviewDialogListener,
        MessageFragment.MessageListener {

    ImageView ivProfile;
    TextView tvLocation;
    RatingBar ratingBar;
    TextView tvRN;

    FloatingActionMenu faMenu;
    com.github.clans.fab.FloatingActionButton faMessage, faReview;

    User user;
    private BookFragmentPagerAdapter aPager;
    private final String TAG = "User Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Get user
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        ivProfile = findViewById(R.id.ivProfile);
        tvLocation = findViewById(R.id.tvLocation);
        ratingBar = findViewById(R.id.rbStars);
        tvRN = findViewById(R.id.tvReviewNumber);

        setupToolbar();
        setupTab();
        setupView();
    }

    private void setupView() {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(UserActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(user.getLocation().latitude, user.getLocation().longitude, 1);
            tvLocation.setText(addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea());
        } catch (IOException e) {
            tvLocation.setVisibility(View.GONE);
            e.printStackTrace();
        }

        ratingBar.setRating(user.getRating());

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null), PorterDuff.Mode.SRC_ATOP);

        if (!user.getReviews().isEmpty()) {
            setReviewNumber();
        } else {
            tvRN.setVisibility(View.GONE);
        }

        faMessage = findViewById(R.id.menu_message);
        faReview = findViewById(R.id.menu_review);

        faMessage.setOnClickListener(view -> {
            FragmentManager fm = getSupportFragmentManager();
            MessageFragment messageCompose = MessageFragment.newInstance();
            messageCompose.show(fm, "fragment_message");
        });

        faReview.setOnClickListener(view -> {
            FragmentManager fm = getSupportFragmentManager();
            ReviewFragment reviewCompose = ReviewFragment.newInstance();
            reviewCompose.show(fm, "fragment_review");
        });

        //TODO setup real Profile image

        Glide.with(this)
                .asBitmap()
                .load(R.drawable.photo_test)
                .into(new BitmapImageViewTarget(ivProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        ivProfile.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    private void setReviewNumber() {
        StringBuilder rev = new StringBuilder();
        rev.append(user.getReviews().size());
        if (user.getReviews().size() == 1) {
            rev.append(" Review");
        } else {
            rev.append(" Reviews");
        }
        tvRN.setText(rev);
    }

    private void setupToolbar() {
        enableToolbarBackButton();
        showToolbarBackButton();
        setTitle(user.getName());
    }

    private void setupTab() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        aPager = new BookFragmentPagerAdapter(getSupportFragmentManager(), UserActivity.this);
        viewPager.setAdapter(aPager);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    public void onClickListener(Book book) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("book", Parcels.wrap(book));

        FragmentManager manager = getSupportFragmentManager();
        BookDetailFragment dialog = BookDetailFragment.newInstance(book);
        dialog.setArguments(bundle);
        dialog.show(manager, "Dialog");
    }

    @Override
    public void onReadyListener(BaseBookListFragment.FragmentType type) {

        switch (type) {
            case SHARE:
                ShareListFragment fmSh = (ShareListFragment) aPager.getRegisteredFragment(0);

                fmSh.pushData(user.getShareBooks());
                break;
            case EXCHANGE:
                ExchangeListFragment fmEx = (ExchangeListFragment) aPager.getRegisteredFragment(1);

                fmEx.pushData(user.getExchangeBooks());
                break;
            case WISHLIST:
                WishListFragment fmWs = (WishListFragment) aPager.getRegisteredFragment(2);

                fmWs.pushData(user.getWishListBooks());
                break;
        }
    }

    public void openReviewActivity(View view) {
        Intent i = new Intent(this, ReviewActivity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivity(i);
    }


    @Override
    public void onAddReview(Review review) {
        //TODO save review on Firebase
        //TODO add author
        //review.setAuthor(FirebaseAuth.getInstance().getCurrentUser());

        user.addReview(review);
        setReviewNumber();
        ratingBar.setRating(user.getRating());
    }

    @Override
    public void onAddMessage(String s) {
        //TODO send message
        toast(s);
    }
}
