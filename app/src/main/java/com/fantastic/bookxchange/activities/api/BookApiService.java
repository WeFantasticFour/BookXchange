package com.fantastic.bookxchange.activities.api;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by gretel on 10/12/17.
 */

public interface BookApiService {

    @GET("search.json")
    Call<SearchResult> search(@QueryMap(encoded = true)Map<String, String> options);




}
