package org.example.db.dao;

import io.dropwizard.hibernate.AbstractDAO;
import org.example.db.entity.Team;
import org.hibernate.SessionFactory;

import java.util.List;

public class TeamDao extends AbstractDAO<Team> {

    public TeamDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Team getTeamById(int id){
        return get(id);
    }
    public List<Team> listTeams(){
        return list(namedTypedQuery("Team.findAll"));
    }

    public Team saveTeam(Team team){
        return persist(team);
    }
    public void deleteTeam(int id){
        Team team = getTeamById(id);
        currentSession().remove(team);
    }

}
