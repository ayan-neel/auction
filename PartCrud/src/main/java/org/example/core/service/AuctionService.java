package org.example.core.service;

import org.example.api.model.PlayerRequest;
import org.example.api.model.TeamRequest;
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

    public Player savePlayer(PlayerRequest playerRequest){
        Player player = new Player();
        player.setPlayerName(playerRequest.getPlayerName());
        player.setNationality(playerRequest.getNationality());
        player.setPrimaryRole(playerRequest.getPrimaryRole());
        player.setFlag(true);
        player.setBasePrice(playerRequest.getBasePrice());
        return playerDao.savePlayer(player);
    }
    public Player editPlayer(PlayerRequest playerRequest){
        Player player = new Player();
        player.setId(playerRequest.getId());
        player.setPlayerName(playerRequest.getPlayerName());
        player.setNationality(playerRequest.getNationality());
        player.setPrimaryRole(playerRequest.getPrimaryRole());
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
    public Team saveTeam(TeamRequest teamRequest){
        Team team = new Team();
        team.setTeamName(teamRequest.getTeamName());
        team.setBudget(teamRequest.getBudget());
        return teamDao.saveTeam(team);
    }

    public Team updateTeam(Team team){
        Team existingTeam = findTeamById(team.getId());
        existingTeam.setTeamName(team.getTeamName());
        existingTeam.setBudget(team.getBudget());
        return teamDao.saveTeam(existingTeam);
    }
    public Team addPlayerToTeam(int playerId,int teamId,int sellPrice){

        Team team = addPlayerToTeamHelper(playerId,teamId,sellPrice);
        return teamDao.saveTeam(team);

    }
    public Team addPlayerToTeam(int playerId,int teamId){
        Team team= addPlayerToTeamHelper(playerId,teamId,0);
        return teamDao.saveTeam(team);

    }
    private Team addPlayerToTeamHelper(int playerId, int teamId,int sellPrice){
        Player player = getPlayerById(playerId);
        player.setSoldPrice(sellPrice);
        Team team = findTeamById(teamId);
        team.setBudget(team.getBudget()-player.getSoldPrice());
        player.setFlag(false);
        team.addPlayer(player);
        playerDao.savePlayer(player);
        return team;

    }
}