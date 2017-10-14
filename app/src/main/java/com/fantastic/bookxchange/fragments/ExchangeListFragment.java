package com.fantastic.bookxchange.fragments;

import android.os.Bundle;

public class ExchangeListFragment extends BaseBookListFragment{


    public ExchangeListFragment() {
        // Required empty public constructor
    }

    public static ExchangeListFragment newInstance() {
        return new ExchangeListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void fetchData() {
        //TODO ExchangeList from User
    }
}
