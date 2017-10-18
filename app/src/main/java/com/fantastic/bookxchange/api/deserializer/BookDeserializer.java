package com.fantastic.bookxchange.api.deserializer;

import com.fantastic.bookxchange.api.JsonKeys;
import com.fantastic.bookxchange.models.Book;
import com.fantastic.bookxchange.models.BookResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by gretel on 10/17/17.
 */

public class BookDeserializer implements JsonDeserializer<BookResponse> {
    @Override
    public BookResponse deserialize(JsonElement json,
                                    Type typeOfT, JsonDeserializationContext context) throws JsonParseException {



        final JsonObject jsonObject = json.getAsJsonObject();

        final JsonElement jsonBibKey = jsonObject.get(JsonKeys.BIB_KEY);
        final String bibKey = jsonBibKey.getAsString();

        final JsonElement jsonThumbnail = jsonObject.get(JsonKeys.THUMBNAIL);
        final String thumbnail = jsonThumbnail.getAsString();

        final JsonElement jsonTitle = jsonObject.get(JsonKeys.TITLE);
        final String title = jsonTitle.getAsString();

        final JsonElement jsonDescription = jsonObject.get(JsonKeys.DESCRIPTION);
        final String description = jsonDescription.getAsString();

        final JsonArray jsonAuthors = jsonObject.get(JsonKeys.AUTHORS).getAsJsonArray();
        final String[] authors = new String[jsonAuthors.size()];
        for (int i = 0; i < authors.length ; i++) {
            final JsonElement jsonAuthor = jsonAuthors.get(i);
            authors[i] = jsonAuthor.getAsString();
        }

        final JsonArray jsonPublishers = jsonObject.get(JsonKeys.PUBLISHERS).getAsJsonArray();
        final String[] publishers = new String[jsonPublishers.size()];
        for (int i = 0; i < publishers.length ; i++) {
            final JsonElement jsonPublisher = jsonPublishers.get(i);
            publishers[i] = jsonPublisher.getAsString();
        }



        final BookResponse bookResponse = new BookResponse();
        bookResponse.setBib_key(bibKey);
        bookResponse.setAuthors(authors);
        bookResponse.setTitle(title);
        bookResponse.setDescription(description);
        bookResponse.setPublishers(publishers);
        bookResponse.setThumbnail_url(thumbnail);

        return bookResponse;


    }

}

