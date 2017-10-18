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
import java.util.List;

/**
 * Created by gretel on 10/17/17.
 */

public class BookDeserializer implements JsonDeserializer<Book> {
    @Override
    public Book deserialize(JsonElement json,
                                    Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        JsonArray bookResponse = json.getAsJsonObject().getAsJsonArray(JsonKeys.BOOK_RESPONSE_ARRAY);



        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonElement jsonTitle = jsonObject.get(JsonKeys.BOOK_TITLE);
        final String title = jsonTitle.getAsString();

        final JsonArray jsonAuthors = jsonObject.get(JsonKeys.BOOK_AUTHOR).getAsJsonArray();
        final String[] authors = new String[jsonAuthors.size()];
        for (int i = 0; i < authors.length ; i++) {
            final JsonElement jsonAuthor = jsonAuthors.get(i);
            authors[i] = jsonAuthor.getAsString();
        }

        final JsonArray jsonIsbns = jsonObject.get(JsonKeys.BOOK_ISBN).getAsJsonArray();
        final String[] isbns = new String[jsonIsbns.size()];
        for (int i = 0; i < isbns.length ; i++) {
            final JsonElement jsonIsbn = jsonIsbns.get(i);
            isbns[i] = jsonIsbn.getAsString();
        }

        final JsonArray jsonEditionKeys = jsonObject.get(JsonKeys.EDITION_KEY).getAsJsonArray();
        final String[] editionKeys = new String[jsonEditionKeys.size()];
        for (int i = 0; i < editionKeys.length ; i++) {
            final JsonElement jsonEditionKey = jsonEditionKeys.get(i);
            editionKeys[i] = jsonEditionKey.getAsString();
        }

        final JsonArray jsonPublishers = jsonObject.get(JsonKeys.BOOK_PUBLISHER).getAsJsonArray();
        final String[] publishers = new String[jsonPublishers.size()];
        for (int i = 0; i < publishers.length ; i++) {
            final JsonElement jsonPublisher = jsonPublishers.get(i);
            publishers[i] = jsonPublisher.getAsString();
        }



        final Book book = new Book();
        book.setIsbn(isbns);
        book.setAuthors(authors);
        book.setTitle(title);
        book.setEditionKeys(editionKeys);
        book.setPublishers(publishers);
       // book.setUrlPicture(urlPicture);

        return book;


    }

}

