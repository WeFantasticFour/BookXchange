package com.fantastic.bookxchange.fragments;

import android.os.Bundle;

import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.rest.BookClient;
import com.fantastic.bookxchange.rest.JsonKeys;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gretel on 10/20/17.
 */

public class SearchListFragment extends BaseBookListFragment {

    private BookClient client;

    public SearchListFragment() {

    }

    public static SearchListFragment newInstance(String query) {

        Bundle args = new Bundle();

        SearchListFragment fragment = new SearchListFragment();
        args.putString("q", query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        readyListener.onReadyListener(FragmentType.SEARCH);
        String query = getArguments().getString("q");
        fetchBooks(query);
    }

    public void fetchBooks(String query) {
        client = new BookClient();
        client.getBooks(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs;
                    if (response != null) {
                        docs = response.getJSONArray(JsonKeys.DOCS);
                        final ArrayList<Book> books = Book.fromJson(docs);
                        pushData(books);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

        });
    }

}
