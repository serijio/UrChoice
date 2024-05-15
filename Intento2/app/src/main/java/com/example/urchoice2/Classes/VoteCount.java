package com.example.urchoice2.Classes;

public class VoteCount {
    private String voteGame;
    private int voteCount;

    public VoteCount(String voteGame, int voteCount) {
        this.voteGame = voteGame;
        this.voteCount = voteCount;
    }

    public String getVoteGame() {
        return voteGame;
    }

    public void setVoteGame(String voteGame) {
        this.voteGame = voteGame;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
