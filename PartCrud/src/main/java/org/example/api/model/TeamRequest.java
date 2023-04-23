package org.example.api.model;

import org.example.db.entity.Player;

import java.util.List;

public class TeamRequest {

    private int id;
    private String teamName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    private int budget;

}
