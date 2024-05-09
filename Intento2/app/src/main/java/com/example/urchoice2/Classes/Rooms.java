package com.example.urchoice2.Classes;

public class Rooms {

    private int id_room;

    private String pass_room;

    private String status_room;

    private int id_cat;

    private String name_room;

    private int userCount;

    private String img_cat;


    public Rooms(int id_room, String pass_room, String status_room, int id_cat, String name_room, int userCount, String img_cat) {
        this.id_room = id_room;
        this.pass_room = pass_room;
        this.status_room = status_room;
        this.id_cat = id_cat;
        this.name_room = name_room;
        this.userCount = userCount;
        this.img_cat = img_cat;
    }

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public String getPass_room() {
        return pass_room;
    }

    public void setPass_room(String pass_room) {
        this.pass_room = pass_room;
    }

    public String getStatus_room() {
        return status_room;
    }

    public void setStatus_room(String status_room) {
        this.status_room = status_room;
    }

    public int getId_cat() {
        return id_cat;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }

    public String getName_room() {
        return name_room;
    }

    public void setName_room(String name_room) {
        this.name_room = name_room;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getImg_cat() {
        return img_cat;
    }

    public void setImg_cat(String img_cat) {
        this.img_cat = img_cat;
    }
}
