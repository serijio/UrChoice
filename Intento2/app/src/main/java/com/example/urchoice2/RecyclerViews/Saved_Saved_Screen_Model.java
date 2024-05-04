package com.example.urchoice2.RecyclerViews;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Saved_Saved_Screen_Model {
    public String savedSavedScreenName;
    public Bitmap savedSavedScreenImg;
    public Drawable savedSavedScreenFavs;
    public Drawable savedSavedScreenSave;

    public Saved_Saved_Screen_Model(String savedSavedScreenName, Bitmap savedSavedScreenImg, Drawable savedSavedScreenFavs, Drawable savedSavedScreenSave) {
        this.savedSavedScreenName = savedSavedScreenName;
        this.savedSavedScreenImg = savedSavedScreenImg;
        this.savedSavedScreenFavs = savedSavedScreenFavs;
        this.savedSavedScreenSave = savedSavedScreenSave;
    }

    public String getSavedSavedScreenName() {
        return savedSavedScreenName;
    }

    public Bitmap getSavedSavedScreenImg() {
        return savedSavedScreenImg;
    }

    public Drawable getSavedSavedScreenFavs() {
        return savedSavedScreenFavs;
    }

    public Drawable getSavedSavedScreenSave() {
        return savedSavedScreenSave;
    }
}
