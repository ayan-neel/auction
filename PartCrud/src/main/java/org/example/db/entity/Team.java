package org.example.db.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@NamedQuery(name="Team.findAll", query = "SELECT t from Team t")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="team_name")
    private String teamName;

    @Column(name="budget")
    private int budget;

    @OneToMany(cascade= {CascadeType.DETACH,
            CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH}, mappedBy ="team", fetch = FetchType.LAZY)
    private List<Player> squad = new ArrayList<>();

    public void addPlayer(Player player){

        player.setTeam(this);
        squad.add(player);
    }

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

    public List<Player> getSquad() {
        return squad;
    }

    public void setSquad(List<Player> squad) {
        this.squad = squad;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", teamName='" + teamName + '\'' +
                ", budget=" + budget +
                ", squad=" + squad +
                '}';
    }
}
