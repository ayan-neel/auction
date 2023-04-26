package org.example.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@NamedQuery(name = "Player.getAll", query = "SELECT e from Player e")
@Data
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



    @Column
    private String playerName;

    @Column
    private String nationality;

    @Column
    private String primaryRole;

    @Column
    private boolean flag;

    @Column
    private int basePrice;

    @Column
    private int soldPrice;

    @ManyToOne(cascade= {CascadeType.DETACH,
            CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    @JsonIgnore
    private Team team;

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