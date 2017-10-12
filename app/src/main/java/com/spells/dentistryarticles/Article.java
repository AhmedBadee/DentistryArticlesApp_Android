package com.spells.dentistryarticles;

import android.graphics.Bitmap;

class Article {

    private String title;
    private Bitmap image;
    private String brief;

    Article() {

    }

    public Article(String title, Bitmap image, String brief) {
        this.title = title;
        this.image = image;
        this.brief = brief;
    }

    void setTitle(String title) {
        this.title = title;
    }

    void setImage(Bitmap image) {
        this.image = image;
    }

    void setBrief(String brief) {
        this.brief = brief;
    }

    String getTitle() {
        return title;
    }

    Bitmap getImage() {
        return image;
    }

    String getBrief() {
        return brief;
    }
}
