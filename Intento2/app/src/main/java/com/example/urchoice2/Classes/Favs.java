package com.example.urchoice2.Classes;

public class Favs {
    private int id_fav;
    private int id_user;
    private int id_cat;
    private String name_cat;
    private String img_cat;
    private boolean isSaved;

    public Favs(int id_fav, int id_user, int id_cat, String name_cat, String img_cat) {
        this.id_fav = id_fav;
        this.id_user = id_user;
        this.id_cat = id_cat;
        this.name_cat = name_cat;
        this.img_cat = img_cat;
        this.isSaved = true;
    }

    public int getId_fav() {
        return id_fav;
    }

    public void setId_fav(int id_fav) {
        this.id_fav = id_fav;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_cat() {
        return id_cat;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }

    public String getName_cat() {
        return name_cat;
    }

    public void setName_cat(String name_cat) {
        this.name_cat = name_cat;
    }

    public String getImg_cat() {
        return img_cat;
    }

    public void setImg_cat(String img_cat) {
        this.img_cat = img_cat;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}