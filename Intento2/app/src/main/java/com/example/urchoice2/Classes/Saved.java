package com.example.urchoice2.Classes;

import com.example.urchoice2.API.ListItem;

public class Saved implements ListItem {
    private int id_save;
    private int id_user;
    private int id_cat;
    private String name_cat;
    private String img_cat;
    private boolean isSaved;

    public Saved(int id_save, int id_user, int id_cat, String name_cat, String img_cat) {
        this.id_save = id_save;
        this.id_user = id_user;
        this.id_cat = id_cat;
        this.name_cat = name_cat;
        this.img_cat = img_cat;
        this.isSaved = true;
    }

    public int getId_save() {
        return id_save;
    }

    public void setId_save(int id_save) {
        this.id_save = id_save;
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

    @Override
    public int getId() {
        return id_cat;
    }

    @Override
    public String getName() {
        return name_cat;
    }

    @Override
    public String getImg() {
        return img_cat;
    }
}