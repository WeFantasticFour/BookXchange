package com.fantastic.bookxchange.models;

import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by m3libea on 10/11/17.
 */

@Parcel
public class Book {
    public String title;
    public String author;
    public String publisher;
    public String isbn;
    public String urlPicture;


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public static Book fromJSON(JSONObject jsonObject){
        //TODO Complete the method to get info from JSON
        return new Book();
    }

    public static void toJSON(Book book){
        //TODO Complete the method to send the info tho Firebase
    }

}
