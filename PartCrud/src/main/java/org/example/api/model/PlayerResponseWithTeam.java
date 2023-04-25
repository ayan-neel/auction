package org.example.api.model;

public class PlayerResponseWithTeam {



    private String playerName;
    private String nationality;

    public PlayerResponseWithTeam() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String primaryRole;

    private String teamName;

    private int id;

    public PlayerResponseWithTeam(String playerName, String nationality, String primaryRole, String teamName, int id) {
        this.playerName = playerName;
        this.nationality = nationality;
        this.primaryRole = primaryRole;
        this.teamName = teamName;
        this.id = id;
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

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

}
