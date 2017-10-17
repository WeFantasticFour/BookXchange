package com.fantastic.bookxchange.api.serializer;

import com.fantastic.bookxchange.api.JsonKeys;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.SearchResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by gretel on 10/17/17.
 */

public class BookDeserializer implements JsonDeserializer<SearchResult> {
    @Override
    public SearchResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        Gson gson = new Gson();
        SearchResult searchResult = gson.fromJson(json, SearchResult.class);

        JsonArray searchResultData = json.getAsJsonObject().getAsJsonArray(JsonKeys.BOOK_RESPONSE_ARRAY);





        return null;
    }

    private ArrayList<Book> deserializerBookFromJson(JsonArray searchResultData){
        ArrayList<Book> books = new ArrayList<>();
        for (int i = 0; i < searchResultData.size(); i++) {
            JsonObject searchResultDataObjetct = searchResultData.get(i).getAsJsonObject();
            String title = searchResultDataObjetct.getAsJsonObject(JsonKeys.BOOK_TITLE).getAsString();
            String author = searchResultDataObjetct.getAsJsonObject(JsonKeys.BOOK_AUTHOR).getAsString();
            String publisher = searchResultDataObjetct.getAsJsonObject(JsonKeys.BOOK_PUBLISHER).getAsString();
            String isbn = searchResultDataObjetct.getAsJsonObject(JsonKeys.BOOK_ISBN).getAsString();
            String edition_key = searchResultDataObjetct.getAsJsonObject(JsonKeys.EDITION_KEY).getAsString();

            Book currentBook = new Book();



        }

    }
}
