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
import android.util.Log;
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
import com.fantastic.bookxchange.rest.BookClient;
import com.fantastic.bookxchange.utils.FirebaseUtils;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class UserActivity extends BaseActivity implements BaseBookListFragment.BookListClickListener,
        BaseBookListFragment.BookListReadyListener,
        ReviewFragment.ReviewDialogListener,
        MessageFragment.MessageListener {

    private final String TAG = "User Activity";
    private ImageView ivProfile;
    private TextView tvLocation;
    private RatingBar ratingBar;
    private TextView tvRN;
    private FloatingActionMenu faMenu;
    private com.github.clans.fab.FloatingActionButton faMessage, faReview;
    private User user;
    private BookFragmentPagerAdapter aPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

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

        if(user.getLocation() != null) {
            setLocation(user);
        }else{
            BookClient client = new BookClient();
            client.getLocation(user.getZip(), new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject object = (JSONObject) response.getJSONArray("results").get(0);
                        JSONObject locObject = object.getJSONObject("geometry").getJSONObject("location");
                        user.setLocation(new LatLng(locObject.getDouble("lat"), locObject.getDouble("lng")));
                        setLocation(user);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            });
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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            FloatingActionMenu faM = findViewById(R.id.faMenu);

            faM.setVisibility(View.GONE);
        }else{
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
        }

        
        Glide.with(this)
                .load(user.getUrlProfileImage())
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.ic_person_24dp)
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

    public void setLocation(User user){
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
        user.addReview(review);
        setReviewNumber();
        ratingBar.setRating(user.getRating());
        FirebaseUtils.loadOneUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), me -> {
            if (me == null) {
                me = new User();
                me.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                me.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            }
            review.setAuthor(me);
            FirebaseUtils.saveReview(user, review, task -> {
                if (task.getException() != null) {
                    snakebar(ratingBar, task.getException().getMessage());
                } else {
                    snakebar(ratingBar, "Your Content has been saved!");
                }
            });
        });
    }

    @Override
    public void onAddMessage(String s) {
        //TODO send message
        toast(s);
    }


}
