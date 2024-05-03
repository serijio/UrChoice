package com.example.urchoice2.Classes;


public class UserVote {
    private int id_user;
    private String nick_user;
    private String vote_game;


    public UserVote(int id_user, String nick_user, String vote_game) {
        this.id_user = id_user;
        this.nick_user = nick_user;
        this.vote_game = vote_game;
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