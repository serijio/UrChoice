package com.example.urchoice2.Classes;


public class UserVote {
    private int id_user;
    private String nick_user;
    private String vote_game;
    private int admin;

    public UserVote(int id_user, String nick_user, String vote_game,int admin) {
        this.id_user = id_user;
        this.nick_user = nick_user;
        this.vote_game = vote_game;
        this.admin = admin;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getNick_user() {
        return nick_user;
    }

    public void setNick_user(String nick_user) {
        this.nick_user = nick_user;
    }

    public String getVote_game() {
        return vote_game;
    }

    public void setVote_game(String vote_game) {
        this.vote_game = vote_game;
    }
}