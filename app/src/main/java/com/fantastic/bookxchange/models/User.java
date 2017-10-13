package com.fantastic.bookxchange.models;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;
import org.parceler.Parcel;

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

    public static User fromJSON(JSONObject jsonObject){
        //TODO Complete the method to get info from JSON
        return new User();
    }

    public static void toJSON(User user){
        //TODO Complete the method to send the info tho Firebase
    }
}
