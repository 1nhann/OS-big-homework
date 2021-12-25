package com.example.czxtks.Models.DirectoryManagement.vo;

public class File {
    private String content;
    private String date;
    private String user;
    private int filesize;

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "{" +
                "\"content\":\"" + content.substring(2)  + "\"" +
                ", \"date\":\"" + date  + "\"" +
                ", \"user\":\"" + user + "\"" +
                ", \"filesize\":" + filesize +
                "}";
    }
}
