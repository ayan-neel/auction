package org.example.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
@NamedQuery(name = "Player.getAll", query = "SELECT e from Player e")
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    public Player() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Column
    private String playerName;

    @Column
    private String nationality;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }


    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    public int getSoldPrice() {
        return soldPrice;
    }

    public void setSoldPrice(int soldPrice) {
        this.soldPrice = soldPrice;
    }

    @Column
    private String primaryRole;

    @Column
    private String flag;

    @Column
    private int basePrice;

    @Column
    private int soldPrice;

    @ManyToOne(cascade= {CascadeType.DETACH,
            CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    @JsonIgnore
    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }


    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", nationality='" + nationality + '\'' +
                ", primaryRole='" + primaryRole + '\'' +
                ", price=" + basePrice +
                '}';
    }
}