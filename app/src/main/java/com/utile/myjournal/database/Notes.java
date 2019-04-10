package com.utile.myjournal.database;

import java.io.Serializable;

/**
 * Created by adity on 23/07/2017.
 */

public class Notes implements Serializable {
    private String title, timestamp, color, highlights;
    private int id;
    private String imp;

    public Notes(int id, String title, String timestamp, String color, String highlights, String imp) {
        this.title = title;
        this.timestamp = timestamp;
        this.color = color;
        this.highlights = highlights;
        this.id = id;
        this.imp = imp;
    }

    public Notes(String title, String timestamp, String color, String highlights, String imp) {
        this.title = title;
        this.timestamp = timestamp;
        this.color = color;
        this.highlights = highlights;
        this.imp = imp;
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getColor() {
        return color;
    }

    public String getHighlights() {
        return highlights;
    }

    public int getId() {
        return id;
    }

    public String getFavourite() {
        return imp;
    }
}
