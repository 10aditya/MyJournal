package com.utile.myjournal.database;

import java.io.Serializable;

/**
 * Created by Aditya on 18/06/2016.
 */
public class Entries implements Serializable {

    //private variables
    private int _id;
    private String _title;
    private String _date, _mood, _highlights, no_of_images, location_c, location_n;
    private String image_uri, image2_uri, image3_uri, image4_uri, time, favourite, color;

    public Entries(int id, String title, String date, String mood, String color, String highlights,
                   String no_of_images, String image_uri, String image2_uri, String image3_uri,
                   String image4_uri, String location_n, String location_c, String favourite) {
        this._id = id;
        this._title = title;
        this._mood = mood;
        this._date = date;
        this.color = color;
        this._highlights = highlights;
        this.no_of_images = no_of_images;
        this.image_uri = image_uri;
        this.image2_uri = image2_uri;
        this.image3_uri = image3_uri;
        this.image4_uri = image4_uri;
        this.location_c = location_c;
        this.location_n = location_n;
        this.favourite = favourite;
    }

    // constructor
    public Entries(String title, String date, String mood, String color, String highlights,
                   String no_of_images, String image_uri, String image2_uri, String image3_uri,
                   String image4_uri, String location_n,
                   String location_c, String favourite) {
        this._title = title;
        this._mood = mood;
        this._date = date;
        this._highlights = highlights;
        this.color = color;
        this.no_of_images = no_of_images;
        this.image_uri = image_uri;
        this.image2_uri = image2_uri;
        this.image3_uri = image3_uri;
        this.image4_uri = image4_uri;
        this.location_n = location_n;
        this.location_c = location_c;
        this.favourite = favourite;
    }

    public String getImage2_uri() {
        return image2_uri;
    }

    public String getImage3_uri() {
        return image3_uri;
    }

    public String getLocation_n() {
        return location_n;
    }

    public String getLocation_c() {
        return location_c;
    }

    public String getNo_of_images() {
        return no_of_images;
    }

    public String getImage4_uri() {
        return image4_uri;
    }

    public String getFavourite() {
        return favourite;
    }

    public int getID() {
        return this._id;
    }

    public String getColor() {
        return color;
    }

    public String getTitle() {
        return this._title;
    }

    public String getMood() {
        return this._mood;
    }

    public String getDate() {
        return this._date;
    }

    public String getHighlights() {
        return this._highlights;
    }

    public String getImage() {
        return image_uri;
    }

    public void set_title(String ss){
        this._title=ss;
    }
}
