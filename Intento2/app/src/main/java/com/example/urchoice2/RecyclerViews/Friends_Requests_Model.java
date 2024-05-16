package com.example.urchoice2.RecyclerViews;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Friends_Requests_Model {
    public String friendsRequestsName;
    public String friendsRequestsEmail;
    public Bitmap friendsRequestsImg;
    public Drawable friendsRequestsAccept;
    public Drawable friendsRequestsDecline;

    public Friends_Requests_Model(String friendsRequestsName, String friendsRequestsEmail, Bitmap friendsRequestsImg, Drawable friendsRequestsAccept, Drawable friendsRequestsDecline) {
        this.friendsRequestsName = friendsRequestsName;
        this.friendsRequestsEmail = friendsRequestsEmail;
        this.friendsRequestsImg = friendsRequestsImg;
        this.friendsRequestsAccept = friendsRequestsAccept;
        this.friendsRequestsDecline = friendsRequestsDecline;
    }

    public String getFriendsScreenName() {
        return friendsRequestsName;
    }

    public String getFriendsRequestsEmail() {
        return friendsRequestsEmail;
    }

    public Bitmap getFriendsRequestsImg() {
        return friendsRequestsImg;
    }

    public Drawable getFriendsRequestsAccept() {
        return friendsRequestsAccept;
    }

    public Drawable getFriendsRequestsDecline() {
        return friendsRequestsDecline;
    }
}
