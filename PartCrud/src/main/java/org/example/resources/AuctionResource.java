package org.example.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.checkerframework.checker.units.qual.Time;
import org.example.api.Representation;
import org.example.api.model.BidRequest;
import org.example.api.model.PlayerRequest;
import org.example.api.model.TeamRequest;
import org.example.core.service.AuctionService;
import org.example.db.entity.Player;
import org.example.db.entity.Team;

import java.util.List;

@Path("/auction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuctionResource {

    private AuctionService auctionService;

    public AuctionResource(AuctionService auctionService){
        this.auctionService = auctionService;
    }

    @Path("/getPlayers/{id}")
    @GET
    @Timed
    @UnitOfWork
    public Representation<Player> getPlayerById(@PathParam("id") int id){
        System.out.println("invoked");
        int code = 200;
        Player player = auctionService.getPlayerById(id);
        return new Representation<>(code,player);

    }
    @Path("/getPlayers")
    @GET
    @Timed
    @UnitOfWork
    public Representation<List<Player>> getPlayers(@PathParam("id") int id){
        System.out.println("invoked");
        int code = 200;
        List<Player> players = auctionService.getPlayers();
        return new Representation<>(code,players);

    }

    @Path("/players/save")
    @POST
    @Time
    @UnitOfWork
    public Representation<Player> savePlayer(@Valid PlayerRequest playerRequest) {
        Player player = new Player();
        player.setPlayerName(playerRequest.getPlayerName());
        player.setNationality(playerRequest.getNationality());
        player.setPrimaryRole(playerRequest.getPrimaryRole());
        int code = 201;
        return new Representation<>(code, auctionService.savePlayer(player));

    }
    @Path("/players/edit")
    @PUT
    @Time
    @UnitOfWork
    public Representation<Player> editPlayer(@Valid PlayerRequest playerRequest) {
        Player player = new Player();
        player.setId(playerRequest.getId());
        player.setPlayerName(playerRequest.getPlayerName());
        player.setNationality(playerRequest.getNationality());
        player.setPrimaryRole(playerRequest.getPrimaryRole());
        int code = 204;
        return new Representation<>(code, auctionService.editPlayer(player));

    }
    @Path("/players/delete/{id}")
    @DELETE
    @Time
    @UnitOfWork
    public Representation<Integer> deletePlayer(@PathParam("id") int id ) {
        int code = 200;
        return new Representation<>(code, auctionService.deletePlayer(id));

    }

    @Path("/getTeams")
    @GET
    @Timed
    @UnitOfWork
    public Representation<List<Team>> getTeams(){
        int code = 200;
        return new Representation<>(code,auctionService.getTeams());
    }

    @Path("/getTeam/{id}")
    @GET
    @Timed
    @UnitOfWork
    public Representation<Team> getTeam(@PathParam("id") int id){
        int code = 200;
        return new Representation<>(code, auctionService.findTeamById(id));
    }

    @Path("/teams/save")
    @POST
    @Timed
    @UnitOfWork
    public Representation<Team> saveTeam(TeamRequest teamRequest){
        Team team = new Team();
        team.setTeamName(teamRequest.getTeamName());
        team.setBudget(teamRequest.getBudget());
        int code=201;
        return new Representation<>(code,auctionService.saveTeam(team));

    }

    //for testing purpose
    @Path("/buy")
    @PUT
    @Timed
    @UnitOfWork
    public Representation<Team> addPlayerToTeam(BidRequest bidRequest){

        int playerId = bidRequest.getPlayerId();
        int teamId = bidRequest.getTeamId();
        int code = 200;
        return new Representation<>(code,auctionService.addPlayerToTeam(playerId,teamId));

    }

}
