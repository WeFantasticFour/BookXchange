package com.fantastic.bookxchange.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gretel on 10/17/17.
 */

public class SearchRequest {
    private int page = 1;
    private String query = "Tolkien";

    public void setPage(int page)
    {
        this.page = page;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public Map<String, String> toQuery(){
        Map<String, String> options = new HashMap<>();
        options.put("page", String.valueOf(page));
        options.put("q", query);
        return options;
    }


}
