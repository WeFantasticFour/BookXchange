package com.fantastic.bookxchange.fragments;

import android.os.Bundle;

public class WishListFragment extends BaseBookListFragment {

    public WishListFragment() {
        // Required empty public constructor
    }

    public static WishListFragment newInstance() {
        WishListFragment fragment = new WishListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        readyListener.onReadyListener(FragmentType.WISHLIST);
    }
}
