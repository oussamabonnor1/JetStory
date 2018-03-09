package com.jetlightstudio.jetstory;


import java.io.Serializable;

/**
 * Created by oussama on 08/03/2018.
 */

public class Story  implements Serializable{
    private String title, author, date;
    private String content;
    private int id;

    public enum category {
        action, comedy, romance, moral, sad
    }

    private int time;

    public Story(String title, String author, String date, int id, int time) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.id = id;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }
}
