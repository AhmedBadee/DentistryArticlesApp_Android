package com.spells.dentistryarticles;

import android.graphics.Bitmap;

public class Article {

    private String title;
    private Bitmap image;
    private String brief;

    public Article() {

    }

    public Article(String title, Bitmap image, String brief) {
        this.title = title;
        this.image = image;
        this.brief = brief;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getBrief() {
        return brief;
    }
}
