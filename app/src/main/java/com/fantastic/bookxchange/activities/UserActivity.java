package com.fantastic.bookxchange.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.fantastic.bookxchange.R;
import com.fantastic.bookxchange.adapters.BookFragmentPagerAdapter;
import com.fantastic.bookxchange.fragments.BaseBookListFragment;
import com.fantastic.bookxchange.fragments.BookDetailFragment;
import com.fantastic.bookxchange.fragments.ExchangeListFragment;
import com.fantastic.bookxchange.fragments.ShareListFragment;
import com.fantastic.bookxchange.fragments.WishListFragment;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.User;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class UserActivity extends BaseActivity implements BaseBookListFragment.BookListClickListener, BaseBookListFragment.BookListReadyListener {

    ImageView ivProfile;
    TextView tvLocation;

    User user;
    private BookFragmentPagerAdapter aPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Get user
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

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

//        Glide.with(this)
//                .load(R.drawable.photo_test)
//                .asBitmap()
//                .centerCrop()
//                .into(new BitmapImageViewTarget(ivProfile) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(getResources(), resource);
//                        circularBitmapDrawable.setCircular(true);
//                        ivProfile.setImageDrawable(circularBitmapDrawable);
//                    }
//                });
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
        //TODO Itent to Book Detail page


        BookDetailFragment bookDetailFragment = new BookDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("book", Parcels.wrap(book));
        bookDetailFragment.setArguments(bundle);

    }

    @Override
    public void onReadyListener(BaseBookListFragment.FragmentType type) {

        switch (type){
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
}
