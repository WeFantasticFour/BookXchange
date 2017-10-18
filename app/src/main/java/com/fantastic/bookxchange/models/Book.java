package com.fantastic.bookxchange.models;

import android.text.TextUtils;

import com.fantastic.bookxchange.api.Constant;
import com.fantastic.bookxchange.api.JsonKeys;
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

    @SerializedName("edition_key")
    private List<String> editionKeys;
    @SerializedName("title_suggest")
    public String title;
    @SerializedName("author_name")
    public String author;
    public String publisher;
    public List<String> authors;
    @SerializedName("publishers")
    public List<String> publishers;
    @SerializedName("isbn")
    public List<String> isbn;
    @SerializedName("cover_edition_key")
    public String urlPicture;

    //public String shortDescription;


    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        if(authors != null)
        {
            return TextUtils.join(", ", authors);
        }

        return "No author";
    }

    public String getPublishers() {
        if(publishers != null && publishers.size()>0){
            return publishers.get(0);
        }
        return "No Publisher";
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

    public String getIsbn() {
        if(isbn != null && isbn.size()>0){
            return isbn.get(0);
        }
        return "No ISBN";
    }


    public List<String> getEditionKeys() {
        return editionKeys;
    }

    public void setEditionKeys(List<String> editionKeys) {
        this.editionKeys = editionKeys;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public void setPublishers(List<String> publishers) {
        this.publishers = publishers;
    }

    public void setIsbn(List<String> isbn) {
        this.isbn = isbn;
    }

    public String getOLibraryId(){
        if(editionKeys != null && editionKeys.size() > 0){
            return editionKeys.get(0);
        }
        return urlPicture;
    }

    public String getUrlPicture() {
        return Constant.STATIC_BASE_URL + getOLibraryId() + "-L.jpg?default=false";
    }

//    public String getShortDescription() {
//        return shortDescription;
//    }

    public void setTitle(String title) {
        this.title = title;
    }



    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

//    public void setShortDescription(String shortDescription) {
//        this.shortDescription = shortDescription;
//    }

    public static Book fromJSON(JSONObject jsonObject){
        //TODO Complete the method to get info from JSON

        Book book = new Book();


        return new Book();
    }

    public static void toJSON(Book book){
        //TODO Complete the method to send the info tho Firebase
    }

}
