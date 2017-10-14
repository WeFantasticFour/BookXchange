package com.fantastic.bookxchange.fragments;

import android.os.Bundle;

public class ShareListFragment extends BaseBookListFragment{

    public ShareListFragment() {
        // Required empty public constructor
    }

    public static ShareListFragment newInstance() {
        ShareListFragment fragment = new ShareListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        readyListener.onReadyListener(FragmentType.SHARE);
    }
}
