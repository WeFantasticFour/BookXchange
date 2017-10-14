package com.fantastic.bookxchange.api;

import com.fantastic.bookxchange.models.BookResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gretel on 10/12/17.
 */

public interface BookApiService {

    @GET("books/?bibkeys={bibkeys}&jscmd=details&format=json")
    Call<BookResponse> getBookbyISBN(@Query("bibkeys") String bib_key);


}
