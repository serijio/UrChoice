package com.example.urchoice2.Classes;

public class Category {
    public int id_cat;
    public String name_cat;
    public String img_cat;

    public Category(int id_cat, String name_cat, String img_cat) {
        this.id_cat = id_cat;
        this.name_cat = name_cat;
        this.img_cat = img_cat;
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
}
