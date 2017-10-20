package com.fantastic.bookxchange.models;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by m3libea on 10/19/17.
 */

@Parcel
public class Review {

    public String id;
    public User author;
    public float stars;
    public String review;
    public Date date;

    public Review() {
        this.date = Calendar.getInstance().getTime();
    }

    public Review(String id, User author, int stars, String review) {
        this.id = id;
        this.author = author;
        this.stars = stars;
        this.review = review;
        this.date = Calendar.getInstance().getTime();
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

    public float getStars() {
        return stars;
    }

    public Date getDate() {
        return date;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getFormattedDate() {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String formattedDate = "";
        formattedDate = DateFormat.getDateTimeInstance().format(date);


        return formattedDate;
    }
}
