package com.fantastic.bookxchange.models;

import android.text.TextUtils;

import com.fantastic.bookxchange.rest.JsonKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by m3libea on 10/11/17.
 */

@Parcel
public class Book {
    public String isbn;
    public String author;
    public String title;
    public String publisher;
    public String cover;
    public String shortDescription;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    // Get book cover from covers API
    public String getCoverUrl() {

        return "http://covers.openlibrary.org/b/olid/" + isbn + "-L.jpg?default=false";

    }

    public void setCoverUrl(String cover){
        this.cover = cover;
    }

    // Returns a Book given the expected JSON
    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            // Deserialize json into object fields
            // Check if a cover edition is available
            if (jsonObject.has(JsonKeys.COVER_EDITION_KEY)) {
                book.isbn = jsonObject.getString(JsonKeys.COVER_EDITION_KEY);
            } else if(jsonObject.has(JsonKeys.EDITION_KEY)) {
                final JSONArray ids = jsonObject.getJSONArray(JsonKeys.EDITION_KEY);
                book.isbn = ids.getString(0);
            }
            book.title = jsonObject.has(JsonKeys.BOOK_TITLE) ? jsonObject.getString(JsonKeys.BOOK_TITLE) : "";
            book.author = getAuthor(jsonObject);
            book.publisher = getPublisher(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return book;
    }

    // Return comma separated author list when there is more than one author
    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray(JsonKeys.BOOK_AUTHOR);
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }    private static String getPublisher(final JSONObject jsonObject) {
        try {
            final JSONArray pubs = jsonObject.getJSONArray(JsonKeys.BOOK_PUBLISHER);
            int numPubs = pubs.length();
            final String[] pubStrings = new String[numPubs];
            for (int i = 0; i < numPubs; ++i) {
                pubStrings[i] = pubs.getString(i);
            }
            return TextUtils.join(", ", pubStrings);
        } catch (JSONException e) {
            return "";
        }
    }


    public static ArrayList<Book> fromJson(JSONArray jsonArray) {
        ArrayList<Book> books = new ArrayList<Book>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Book book = Book.fromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }

    public Book() {
    }


    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
