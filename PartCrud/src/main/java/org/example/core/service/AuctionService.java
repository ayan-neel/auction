package org.example.core.service;

import org.example.db.dao.PlayerDao;
import org.example.db.dao.TeamDao;
import org.example.db.entity.Player;
import org.example.db.entity.Team;

import java.util.List;

public class AuctionService {
    PlayerDao playerDao;
    TeamDao teamDao;

    public AuctionService(PlayerDao playerDao, TeamDao teamDao){
        this.playerDao = playerDao;
        this.teamDao = teamDao;
    }
    public Player getPlayerById(int id){
        return playerDao.getPlayer(id);
    }
    public  List<Player> getPlayers(){
        return playerDao.listPlayers();
    }

    public Player savePlayer(Player player){
        return playerDao.savePlayer(player);
    }
    public Player editPlayer(Player player){
        Player existingPlayer = getPlayerById(player.getId());
        existingPlayer.setPlayerName(player.getPlayerName());
        existingPlayer.setPrimaryRole(player.getPrimaryRole());
        existingPlayer.setNationality(player.getNationality());
        return playerDao.savePlayer(existingPlayer);
    }
    public int deletePlayer(int id){
        playerDao.deletePlayerById(id);
        return 1;
    }
    public List<Team> getTeams(){
        return teamDao.listTeams();
    }
    public Team findTeamById(int id){
        return teamDao.getTeamById(id);
    }
    public Team saveTeam(Team team){
        return teamDao.saveTeam(team);
    }

    public Team updateTeam(Team team){
        Team existingTeam = findTeamById(team.getId());
        existingTeam.setTeamName(team.getTeamName());
        existingTeam.setBudget(team.getBudget());
        return teamDao.saveTeam(existingTeam);
    }
    public Team addPlayerToTeam(int playerId,int teamId){
        Player player = getPlayerById(playerId);
        Team team = findTeamById(teamId);
        team.addPlayer(player);
        return teamDao.saveTeam(team);

    }
}
