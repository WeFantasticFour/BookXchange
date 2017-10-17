package com.fantastic.bookxchange.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gretel on 10/17/17.
 */

public class SearchResult {

    @SerializedName("docs")
    private List<Book> books;

    public List<Book> getBooks(){
        return books;
    }

}
