package org.example.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.example.db.entity.Player;
import org.example.db.entity.Team;
import org.hibernate.SessionFactory;

import java.util.List;

public class PlayerDao extends AbstractDAO<Player> {

    public PlayerDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
    public Player getPlayer(int id){
        return get(id);
    }
    public List<Player> listPlayers(){
        return list(namedTypedQuery("Player.getAll"));
    }

    public Player savePlayer(Player player){
        return persist(player);
    }
    public void deletePlayerById(int id){
        Player player = getPlayer(id);
        currentSession().remove(player);

    }


}
