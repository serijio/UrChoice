package com.example.urchoice2.Classes;

public class RoomGame {

    Integer id_game_room;

    Integer id_room;

    Integer id_game;

    Integer id_user;

    String vote;


    public RoomGame(Integer id_game_room, Integer id_room, Integer id_game, Integer id_user, String vote) {
        this.id_game_room = id_game_room;
        this.id_room = id_room;
        this.id_game = id_game;
        this.id_user = id_user;
        this.vote = vote;
    }


    public Integer getId_game_room() {
        return id_game_room;
    }

    public void setId_game_room(Integer id_game_room) {
        this.id_game_room = id_game_room;
    }

    public Integer getId_room() {
        return id_room;
    }

    public void setId_room(Integer id_room) {
        this.id_room = id_room;
    }

    public Integer getId_game() {
        return id_game;
    }

    public void setId_game(Integer id_game) {
        this.id_game = id_game;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
