package com.example.urchoice2.Classes;

import java.util.List;

public class RoomData {
    private List<User> users;
    private List<RoomGame> roomGames;

    public RoomData(List<User> users, List<RoomGame> roomGames) {
        this.users = users;
        this.roomGames = roomGames;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<RoomGame> getRoomGames() {
        return roomGames;
    }
}
