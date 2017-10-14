package com.fantastic.bookxchange.fragments;

import android.os.Bundle;

public class NearListFragment extends BaseBookListFragment {


    public NearListFragment() {
        // Required empty public constructor
    }

    public static NearListFragment newInstance() {
        return new NearListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        readyListener.onReadyListener(FragmentType.NEAR);
    }
}
