package org.example.api.model;

public class PlayerRequest {

    private int id;



    private String playerName;
    private String nationality;

    private String primaryRole;

    public PlayerRequest() {
    }

    public PlayerRequest(String playerName, String nationality, String primaryRole) {
        this.playerName = playerName;
        this.nationality = nationality;
        this.primaryRole = primaryRole;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPrimaryRole() {
        return primaryRole;
    }

    public void setPrimaryRole(String primaryRole) {
        this.primaryRole = primaryRole;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
