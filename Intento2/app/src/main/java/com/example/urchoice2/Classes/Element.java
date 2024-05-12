package com.example.urchoice2.Classes;

public class Element {

    public int id_elem;

    public String img_elem;

    public String name_elem;

    public int victories;

    public int id_cat;


    public Element(int id_elem, String img_elem, String name_elem, int victories, int id_cat) {
        this.id_elem = id_elem;
        this.img_elem = img_elem;
        this.name_elem = name_elem;
        this.victories = victories;
        this.id_cat = id_cat;
    }

    public int getId_elem() {
        return id_elem;
    }

    public void setId_elem(int id_elem) {
        this.id_elem = id_elem;
    }

    public String getImg_elem() {
        return img_elem;
    }

    public void setImg_elem(String img_elem) {
        this.img_elem = img_elem;
    }

    public String getName_elem() {
        return name_elem;
    }

    public void setName_elem(String name_elem) {
        this.name_elem = name_elem;
    }

    public int getVictories() {
        return victories;
    }

    public void setVictories(int victories) {
        this.victories = victories;
    }

    public int getId_cat() {
        return id_cat;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }
}