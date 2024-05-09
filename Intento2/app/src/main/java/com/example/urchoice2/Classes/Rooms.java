package com.example.urchoice2.Classes;

public class Rooms {

    private int idRoom;

    private String passRoom;

    private String statusRoom;

    private int idCat;

    private String nameRoom;

    private int userCount;

    private String imgCat;

    public Rooms(int idRoom, String passRoom, String statusRoom, int idCat, String nameRoom, int userCount, String imgCat) {
        this.idRoom = idRoom;
        this.passRoom = passRoom;
        this.statusRoom = statusRoom;
        this.idCat = idCat;
        this.nameRoom = nameRoom;
        this.userCount = userCount;
        this.imgCat = imgCat;
    }


    public int getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(int idRoom) {
        this.idRoom = idRoom;
    }

    public String getPassRoom() {
        return passRoom;
    }

    public void setPassRoom(String passRoom) {
        this.passRoom = passRoom;
    }

    public String getStatusRoom() {
        return statusRoom;
    }

    public void setStatusRoom(String statusRoom) {
        this.statusRoom = statusRoom;
    }

    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public String getNameRoom() {
        return nameRoom;
    }

    public void setNameRoom(String nameRoom) {
        this.nameRoom = nameRoom;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public String getImgCat() {
        return imgCat;
    }

    public void setImgCat(String imgCat) {
        this.imgCat = imgCat;
    }
}
