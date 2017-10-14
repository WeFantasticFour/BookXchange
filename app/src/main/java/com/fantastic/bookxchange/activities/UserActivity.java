package com.fantastic.bookxchange.activities;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.BookFragmentPagerAdapter;
import com.fantastic.bookxchange.fragments.BaseBookListFragment;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.User;
import com.fantastic.bookxchange.utils.DataTest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserActivity extends BaseActivity implements BaseBookListFragment.BookListClickListenr {

    ImageView ivProfile;
    TextView tvLocation;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Get user
       // user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        user = DataTest.fakeData().get(0);

        ivProfile = findViewById(R.id.ivProfile);
        tvLocation = findViewById(R.id.tvLocation);

        setupToolbar();
        setupTab();
        setupView();
    }

    private void setupView(){

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(UserActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(user.getLocation().latitude, user.getLocation().longitude, 1);
            tvLocation.setText(addresses.get(0).getLocality() + ", " + addresses.get(0).getAdminArea());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Glide.with(this)
                .load(R.drawable.photo_test)
                .asBitmap()
                .centerCrop()
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

    private void setupToolbar() {
        enableToolbarBackButton();
        setTitle(user.getName());
    }

    private void setupTab() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BookFragmentPagerAdapter(getSupportFragmentManager(),
                UserActivity.this));

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }



    @Override
    public void onClickListener(Book book) {
        //TODO Itent to Book Detail page
    }
}
