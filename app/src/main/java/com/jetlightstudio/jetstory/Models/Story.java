package com.jetlightstudio.jetstory.Models;


import com.jetlightstudio.jetstory.R;

import java.io.Serializable;

/**
 * Created by oussama on 08/03/2018.
 */

public class Story implements Serializable {
    private String title, author, date;
    private String content;
    private int id;
    private int albumId;

    public enum Category {
        action, comedy, romance, moral, sad
    }

    private Category category;
    private int time;

    public Story(String title, String author, String date, String content, int id, int time, String category) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.content = content;
        this.id = id;
        this.category = categoryFromString(category);
        this.time = time;
        albumId = changeAlbumIcon(this.category);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
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

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    Category categoryFromString(String category) {
        if (category.matches("action"))
            return Story.Category.action;
        else if (category.matches("comedy"))
            return Story.Category.comedy;
        else if (category.matches("romance"))
            return Story.Category.romance;
        else if (category.matches("moral"))
            return Story.Category.moral;
        else if (category.matches("sad"))
            return Story.Category.sad;
        else return Category.action;
    }

    int changeAlbumIcon(Story.Category category) {
        if (category == Story.Category.action) {
            return R.drawable.book_red_action;

        } else if (category == Story.Category.comedy) {
            return R.drawable.book_red_comedy;

        } else if (category == Story.Category.romance) {
            return R.drawable.book_red_romance;

        } else if (category == Story.Category.sad) {
            return R.drawable.book_red_sad;

        } else if (category == Story.Category.moral) {
            return R.drawable.book_red_moral;
        } else return R.drawable.book_red;
    }

}
