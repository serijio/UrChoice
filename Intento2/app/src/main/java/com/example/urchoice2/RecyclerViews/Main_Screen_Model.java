package com.example.urchoice2.RecyclerViews;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Main_Screen_Model {
    public String mainScreenName;
    public Bitmap mainScreenImg;
    public Drawable mainScreenFavs;
    public Drawable mainScreenSave;

    public Main_Screen_Model(String mainScreenName, Bitmap mainScreenImg, Drawable mainScreenFavs, Drawable mainScreenSave) {
        this.mainScreenName = mainScreenName;
        this.mainScreenImg = mainScreenImg;
        this.mainScreenFavs = mainScreenFavs;
        this.mainScreenSave = mainScreenSave;
    }

    public String getMainScreenName() {
        return mainScreenName;
    }

    public Bitmap getMainScreenImg() {
        return mainScreenImg;
    }

    public Drawable getMainScreenFavs() {
        return mainScreenFavs;
    }

    public Drawable getMainScreenSave() {
        return mainScreenSave;
    }
}
