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
    public String id;
    public String name;
    public String username;
    public String emailAddress;
    public String zip;
    public LatLng location;
    public String urlProfileImage;
    public List<Book> shareBooks;
    public List<Book> exchangeBooks;
    public List<Book> wishListBooks;
    public List<Review> reviews;
    public float rating;
    public int starsCount;
    public User() {
        this.shareBooks = new ArrayList<>();
        this.exchangeBooks = new ArrayList<>();
        this.wishListBooks = new ArrayList<>();
        this.reviews = new ArrayList<>();

        this.starsCount = 0;
        this.rating = 0;
    }

    public static User fromJSON(JSONObject jsonObject) {
        //TODO Complete the method to get info from JSON
        return new User();
    }

    public static void toJSON(User user) {
        //TODO Complete the method to send the info tho Firebase
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getUrlProfileImage() {
        return urlProfileImage;
    }

    public void setUrlProfileImage(String urlProfileImage) {
        this.urlProfileImage = urlProfileImage;
    }

    public List<Book> getShareBooks() {
        return shareBooks;
    }

    public void setShareBooks(List<Book> shareBooks) {
        this.shareBooks = shareBooks;
    }

    public List<Book> getExchangeBooks() {
        return exchangeBooks;
    }

    public void setExchangeBooks(List<Book> exchangeBooks) {
        this.exchangeBooks = exchangeBooks;
    }

    public List<Book> getWishListBooks() {
        return wishListBooks;
    }

    public void setWishListBooks(List<Book> wishListBooks) {
        this.wishListBooks = wishListBooks;
    }

    public float getRating() {
        return rating;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review r) {
        reviews.add(r);
        //Update rating
        starsCount += r.getStars();
        rating = starsCount / reviews.size();
    }
}
