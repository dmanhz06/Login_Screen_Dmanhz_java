package com.example.myapplication;

public class Post {
    private int id;
    private String userName;
    private String date;
    private String content;
    private String avatarUrl;

    public Post(int id, String userName, String date, String content, String avatarUrl) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.content = content;
        this.avatarUrl = avatarUrl;
    }

    // Constructor without ID (for creating new posts before inserting into DB)
    public Post(String userName, String date, String content, String avatarUrl) {
        this.userName = userName;
        this.date = date;
        this.content = content;
        this.avatarUrl = avatarUrl;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUserName() { return userName; }
    public String getDate() { return date; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getAvatarUrl() { return avatarUrl; }
}