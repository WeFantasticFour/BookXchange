package com.fantastic.bookxchange.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.fantastic.bookxchange.api.JsonKeys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gretel on 10/18/17.
 */

public class BookTest implements Parcelable {

    private String openLibraryId;
    private String author;
    private String title;
    private String publisher;
    private String isbn;



    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getOpenLibraryId() {
        return openLibraryId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }


    public static BookTest fromJson(JSONObject jsonObject) {
        BookTest bookTest = new BookTest();
        try {

            if (jsonObject.has(JsonKeys.COVER_EDITION_KEY)) {
                bookTest.openLibraryId = jsonObject.getString(JsonKeys.COVER_EDITION_KEY);
            } else if(jsonObject.has(JsonKeys.EDITION_KEY)) {
                final JSONArray ids = jsonObject.getJSONArray(JsonKeys.EDITION_KEY);
                bookTest.openLibraryId = ids.getString(0);
            }
            bookTest.title = jsonObject.has(JsonKeys.BOOK_TITLE) ? jsonObject.getString(JsonKeys.BOOK_TITLE) : "";
            bookTest.author = getAuthor(jsonObject);
            bookTest.publisher = getPublisher(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return bookTest;
    }



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


    public static ArrayList<BookTest> fromJson(JSONArray jsonArray) {
        ArrayList<BookTest> books = new ArrayList<BookTest>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            BookTest bookTest = BookTest.fromJson(bookJson);
            if (bookTest != null) {
                books.add(bookTest);
            }
        }
        return books;
    }

    public BookTest() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.openLibraryId);
        dest.writeString(this.author);
        dest.writeString(this.title);
        dest.writeString(this.publisher);
    }

    private BookTest(Parcel in) {
        this.openLibraryId = in.readString();
        this.author = in.readString();
        this.title = in.readString();
        this.publisher = in.readString();
    }

    public static final Creator<BookTest> CREATOR = new Creator<BookTest>() {
        public BookTest createFromParcel(Parcel source) {
            return new BookTest(source);
        }

        public BookTest[] newArray(int size) {
            return new BookTest[size];
        }
    };


}
