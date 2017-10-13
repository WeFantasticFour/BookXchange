package com.fantastic.bookxchange.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m3libea on 10/11/17.
 */

@Parcel
public class User {
    public String name;
    public String username;
    public String emailAddress;
    public LatLng location;
    public String urlProfileImage;
    public List<Book> books;

    public User() {
        this.books = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public LatLng getLocation() {
        return location;
    }

    public List<Book> getBooks() {
        return books;
    }

    public String getUrlProfileImage() {
        return urlProfileImage;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setUrlProfileImage(String urlProfileImage) {
        this.urlProfileImage = urlProfileImage;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public static User fromJSON(JSONObject jsonObject){
        //TODO Complete the method to get info from JSON
        return new User();
    }

    public static void toJSON(User user){
        //TODO Complete the method to send the info tho Firebase
    }
}
