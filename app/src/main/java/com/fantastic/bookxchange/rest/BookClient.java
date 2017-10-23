package com.fantastic.bookxchange.rest;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by gretel on 10/20/17.
 */

public class BookClient {

    private AsyncHttpClient client;
    private static final String GOOGLE_URL = "https://maps.googleapis.com/maps/api/geocode/json?&key=AIzaSyAXe8NalmwBC-hs8lsr_FfJfMZSb_E6vFc";

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

    public void getLocation(final String zip, JsonHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("address", zip);
        client.get(GOOGLE_URL, params, handler);
    }

}
