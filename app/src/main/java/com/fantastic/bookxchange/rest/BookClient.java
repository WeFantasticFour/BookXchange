package com.fantastic.bookxchange.rest;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by gretel on 10/20/17.
 */

public class BookClient {

    private AsyncHttpClient client;

    public BookClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return JsonKeys.API_BASE_URL + relativeUrl;
    }


    public void getBooks(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl(JsonKeys.END_POINT);
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
