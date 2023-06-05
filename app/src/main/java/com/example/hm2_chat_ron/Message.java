package com.example.hm2_chat_ron;

public class Message {
    public String userId;
    public String userName;
    public String userPhoto;
    public String message;

    public Message(String userId, String userName, String userPhoto, String message) {
        this.userId = userId;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.message = message;
    }
}
