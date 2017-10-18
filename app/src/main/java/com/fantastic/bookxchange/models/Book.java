package com.fantastic.bookxchange.models;

import android.text.TextUtils;

import com.fantastic.bookxchange.api.JsonKeys;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m3libea on 10/11/17.
 */

@Parcel
public class Book {


    @SerializedName("title")
    public String title;
    @SerializedName("authors")
    public List<String> authors;
    public String author;
    public String publisher;
    @SerializedName("publishers")
    public List<String> publishers;
    @SerializedName("description")
    public String description;
    @SerializedName("bib_key")
    public String isbn;
    @SerializedName("thumbnail_url")
    public String urlPicture;

    public Book() {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.authors = authors;
        this.publishers = publishers;
        this.description = description;
        this.isbn = isbn;
        this.urlPicture = urlPicture;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthors() {
        if (authors != null) {
            return TextUtils.join(", ", authors);
        }
        return "No author";
    }

    public String getPublishers() {
        if(publishers != null){
            return TextUtils.join(" ", publishers);
        }
        return "No publisher";
    }


    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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



    public static void fromJSON(JSONObject jsonObject){
        //TODO Complete the method to get info from JSON

    }

    public static void toJSON(Book book){
        //TODO Complete the method to send the info tho Firebase
    }

}
