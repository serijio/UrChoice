package com.example.urchoice2.Classes;

public class Element {

    public int id_elem;

    public String img_elem;

    public String name_elem;

    public int victories;


    public Element(int id_element, String img_element, String name_elem, int victories) {
        this.id_elem = id_element;
        this.img_elem = img_element;
        this.name_elem = name_elem;
        this.victories = victories;
    }

    public int getId_element() {
        return id_elem;
    }

    public void setId_element(int id_element) {
        this.id_elem = id_element;
    }

    public String getImg_element() {
        return img_elem;
    }

    public void setImg_element(String img_element) {
        this.img_elem = img_element;
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
}
