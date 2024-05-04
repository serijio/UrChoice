package com.example.urchoice2.RecyclerViews;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Friends_Screen_Model {
    public String friendsScreenName;
    public String friendsScreenEmail;
    public Bitmap friendsScreenImg;
    public Drawable friendsScreenDelete;

    public Friends_Screen_Model(String friendsScreenName, String friendsScreenEmail, Bitmap friendsScreenImg, Drawable friendsScreenDelete) {
        this.friendsScreenName = friendsScreenName;
        this.friendsScreenEmail = friendsScreenEmail;
        this.friendsScreenImg = friendsScreenImg;
        this.friendsScreenDelete = friendsScreenDelete;
    }

    public String getFriendsScreenName() {
        return friendsScreenName;
    }

    public String getFriendsScreenEmail() {
        return friendsScreenEmail;
    }

    public Bitmap getFriendsScreenImg() {
        return friendsScreenImg;
    }

    public Drawable getFriendsScreenDelete() {
        return friendsScreenDelete;
    }
}
