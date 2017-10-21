package com.fantastic.bookxchange.models;

import com.fantastic.bookxchange.utils.Utils;

import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static HashMap<User, List<Message>> getMap(List<Message> messages){
        HashMap<User, List<Message>> mess = new HashMap<>();

        for(Message m:messages) {
            User user = m.getSenderUser();
            if (mess.containsKey(user)){
                mess.get(user).add(m);
            }else{
                List<Message> mList = new ArrayList<>();
                mList.add(m);
                mess.put(user, mList);
            }
        }
        return mess;
    }

    public static List<Message> getLastUnique(HashMap<User, List<Message>> messages){

        ArrayList<Message> mList = new ArrayList<>();

        for(User u:messages.keySet()){
            mList.add(messages.get(u).get(0));
        }

        return mList;
    }
}
