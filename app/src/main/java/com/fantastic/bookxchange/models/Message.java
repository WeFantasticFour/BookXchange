package com.fantastic.bookxchange.models;

import com.fantastic.bookxchange.utils.Utils;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by gretel on 10/14/17.
 */

@Parcel
public class Message {

    private String id;
    User recipientUser;
    User senderUser;
    String text;
    String relativeDate;

    public Message(){}

    public User getRecipientUser() {
        return recipientUser;
    }

    public void setRecipientUser(User recipientUser) {
        this.recipientUser = recipientUser;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRelativeDate() {
        return relativeDate;
    }

    public void setRelativeDate(String relativeDate) {
        this.relativeDate = relativeDate;
    }

    public static Message fromJSON(JSONObject jsonObject){
        //TODO Complete the method to get info from JSON
        return new Message();
    }

    public static void toJSON(Message message){
        //TODO Complete the method to send the info tho Firebase
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = Utils.getTimeString(dateMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }


}
