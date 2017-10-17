package com.fantastic.bookxchange.api;

import com.fantastic.bookxchange.models.SearchResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by gretel on 10/17/17.
 */

public interface BookApi {

    @GET("search.json")
    Call<SearchResult> search(@QueryMap(encoded = true) Map<String, String> options);

}
