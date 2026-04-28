package com.example.sportsnewsapp.model;

import java.io.Serializable;

public class NewsItem implements Serializable {

    private int    id;
    private String title;
    private String description;
    private String category;
    private int    imageResId;

    public NewsItem(int id, String title, String description,
                    String category, int imageResId) {
        this.id          = id;
        this.title       = title;
        this.description = description;
        this.category    = category;
        this.imageResId  = imageResId;
    }

    public int    getId()          { return id; }
    public String getTitle()       { return title; }
    public String getDescription() { return description; }
    public String getCategory()    { return category; }
    public int    getImageResId()  { return imageResId; }
}