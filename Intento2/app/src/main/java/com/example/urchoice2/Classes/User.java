package com.example.urchoice2.Classes;

public class User {
    public int id_user;
    public String email_user;
    public String nick_user;
    public String pass_user;
    public String img_user;

    public User(int id_user, String email_user, String nick_user, String pass_user, String img_user) {
        this.id_user = id_user;
        this.email_user = email_user;
        this.nick_user = nick_user;
        this.pass_user = pass_user;
        this.img_user = img_user;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public String getNick_user() {
        return nick_user;
    }

    public void setNick_user(String nick_user) {
        this.nick_user = nick_user;
    }

    public String getPass_user() {
        return pass_user;
    }

    public void setPass_user(String pass_user) {
        this.pass_user = pass_user;
    }

    public String getImg_user() {
        return img_user;
    }

    public void setImg_user(String img_user) {
        this.img_user = img_user;
    }
}
