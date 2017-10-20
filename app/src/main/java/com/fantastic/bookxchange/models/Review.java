package com.fantastic.bookxchange.models;

import org.parceler.Parcel;

/**
 * Created by m3libea on 10/19/17.
 */

@Parcel
public class Review {

    private String id;
    private User author;
    private int stars;
    private String review;

    public Review() {
    }

    public Review(String id, User author, int stars, String review) {
        this.id = id;
        this.author = author;
        this.stars = stars;
        this.review = review;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
