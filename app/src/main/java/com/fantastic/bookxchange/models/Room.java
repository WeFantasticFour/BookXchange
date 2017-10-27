package com.fantastic.bookxchange.models;

/**
 * Created by dgohil on 10/26/17.
 */

public class Room {
    private String from;
    private String lastMessage;
    private Long time;

    public String getYou() {
        return you;
    }

    public void setYou(String you) {
        this.you = you;
    }

    private String you;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    private String roomId;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
