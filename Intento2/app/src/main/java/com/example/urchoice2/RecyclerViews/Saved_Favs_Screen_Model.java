package com.example.urchoice2.RecyclerViews;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Saved_Favs_Screen_Model {
    public String savedFavsScreenName;
    public Bitmap savedFavsScreenImg;
    public Drawable savedFavsScreenFavs;
    public Drawable savedFavsScreenSave;

    public Saved_Favs_Screen_Model(String savedFavsScreenName, Bitmap savedFavsScreenImg, Drawable savedFavsScreenFavs, Drawable savedFavsScreenSave) {
        this.savedFavsScreenName = savedFavsScreenName;
        this.savedFavsScreenImg = savedFavsScreenImg;
        this.savedFavsScreenFavs = savedFavsScreenFavs;
        this.savedFavsScreenSave = savedFavsScreenSave;
    }

    public String getSavedFavsScreenName() {
        return savedFavsScreenName;
    }

    public Bitmap getSavedFavsScreenImg() {
        return savedFavsScreenImg;
    }

    public Drawable getSavedFavsScreenFavs() {
        return savedFavsScreenFavs;
    }

    public Drawable getSavedFavsScreenSave() {
        return savedFavsScreenSave;
    }
}
