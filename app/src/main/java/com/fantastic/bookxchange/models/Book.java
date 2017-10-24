package com.fantastic.bookxchange.models;

import android.text.TextUtils;

import com.fantastic.bookxchange.rest.JsonKeys;
import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by m3libea on 10/11/17.
 */

@Parcel
@IgnoreExtraProperties
public class Book {
    public String isbn;
    public String author;
    public String title;
    public String publisher;
    public String cover;
    public String shortDescription;
    public long createdAt = new Date().getTime();
    public boolean active = true;
    public CATEGORY category;
    public String userId;
    private String urlPicture;


    public Book() {
    }

    // Returns a Book given the expected JSON
    public static Book fromJson(JSONObject jsonObject) {
        Book book = new Book();
        try {
            // Deserialize json into object fields
            // Check if a cover edition is available
            if (jsonObject.has(JsonKeys.COVER_EDITION_KEY)) {
                book.isbn = jsonObject.getString(JsonKeys.COVER_EDITION_KEY);
            } else if (jsonObject.has(JsonKeys.EDITION_KEY)) {
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
    }

    private static String getPublisher(final JSONObject jsonObject) {
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public void setCategory(CATEGORY category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // Get book cover from covers API
    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/isbn/" + isbn + ".jpg";
    }

    public void setCoverUrl(String cover) {
        this.cover = cover;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }


    public enum CATEGORY {SHARE, WISH, EXCHANGE}

    public static class Builder {
        Book book;

        private Builder(Book book) {
            this.book = book;
        }

        public static Builder get() {
            return new Book.Builder(new Book());
        }

        public Builder title(String val) {
            book.setTitle(val);
            return this;
        }

        public Builder author(String val) {
            book.setAuthor(val);
            return this;
        }

        public Builder isbn(String val) {
            book.setIsbn(val);
            return this;
        }

        public Builder description(String val) {
            book.setShortDescription(val);
            return this;
        }

        public Builder publisher(String val) {
            book.setPublisher(val);
            return this;
        }

        public Builder urlPicture(String val) {
            book.setUrlPicture(val);
            return this;
        }

        public Builder user(String val) {
            book.setUserId(val);
            return this;
        }

        public Builder active(boolean val) {
            book.setActive(val);
            return this;
        }

        public Builder category(CATEGORY val) {
            book.setCategory(val);
            return this;
        }

        public Book build() {
            return book;
        }
    }
}
