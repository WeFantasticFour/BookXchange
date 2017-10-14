package com.fantastic.bookxchange.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;
import org.parceler.Parcel;

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
    @SerializedName("publishers")
    public List<String> publishers;
    @SerializedName("bib_key")
    public String isbn;
    @SerializedName("thumbnail_url")
    public String urlPicture;
    @SerializedName("description")
    public String shortDescription;


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        if (authors != null) {
            return TextUtils.join(", ", authors);
        }
        return "No author";
    }



    public String getPublisher() {
        if(publishers != null) {
            return TextUtils.join(", ", publishers);
        }
        return "No Publishers";
    }

    public String getIsbn() {
        return isbn;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public static Book fromJSON(JSONObject jsonObject){
        //TODO Complete the method to get info from JSON
        return new Book();
    }

    public static void toJSON(Book book){
        //TODO Complete the method to send the info tho Firebase
    }

}
