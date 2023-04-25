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
import org.example.api.model.PlayerResponseWithTeam;
import org.example.api.model.TeamRequest;
import org.example.core.service.AuctionService;
import org.example.db.entity.Player;
import org.example.db.entity.Team;

import java.util.*;
import java.util.stream.Collectors;

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
   /* @Path("/getPlayersWithTeam")
    @GET
    @Timed
    @UnitOfWork
    public Representation<List<PlayerResponseWithTeam>> getPlayersWithTeam(@PathParam("id") int id){
        int code = 200;
        return new Representation<>(code,auctionService);

    } */

    @Path("/players/save")
    @POST
    @Time
    @UnitOfWork
    public Representation<Player> savePlayer(@Valid PlayerRequest playerRequest) {
        Player player = new Player();
        player.setPlayerName(playerRequest.getPlayerName());
        player.setNationality(playerRequest.getNationality());
        player.setPrimaryRole(playerRequest.getPrimaryRole());
        player.setFlag("T");
        player.setBasePrice(playerRequest.getBasePrice());
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

    @Path("/startAuction")
    @GET
    @Timed
    @UnitOfWork
    public Representation<List<Team>> startAuction(){

        List<Team> teams = auctionService.getTeams();
        List<Player> players = auctionService.getPlayers().stream()
                .filter(p -> p.getFlag().equals("T")).collect(Collectors.toList());


        startAuction(players, teams);

        System.out.println("#####End of Auction###########");
        int code =200;
        return new Representation<>(code,auctionService.getTeams());


    }
    private void startAuction(List<Player> players, List<Team> teams) {

        for (Player player : players) {

            Map<String, Integer> bidMap = teams.stream()
                    .collect(Collectors.toMap(Team::getTeamName, data -> 0));

            System.out.println("Bidding for player : " + player);

            int basePrice = player.getBasePrice();
            System.out.println("Bid starting at :"+basePrice);
            List<String> bidders = new ArrayList<>(bidMap.keySet());
            playerBid(basePrice,bidders,teams,bidMap,player);
        }
    }
    @UnitOfWork
    private void playerBid(int basePrice,List<String>bidders,List<Team>teams,Map<String,Integer>bidMap,Player player) {
        Scanner sc = new Scanner(System.in);
        while (bidders.size() >= 1) {
            if (bidders.size() == 1 && bidMap.get(bidders.stream().findFirst().get()) > 0) //bid win
            {
                bidWin(bidders, teams, bidMap, player);
                break;
            }
            for (String team : bidders) {
                System.out.println("Team" + team);
                System.out.println("Bid?:");
                if (Objects.equals(sc.nextLine(), "Y")) {
                    basePrice += 500;
                    bidMap.put(team, basePrice);
                    System.out.println(
                            "#######Team: " + team + " has bid" + "\n New bid price :" + basePrice);
                } else {
                    bidMap.put(team, -1);
                }

            }

            bidders = bidMap.entrySet().stream().filter(e -> e.getValue() >= 0).map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            if (bidders.isEmpty()) { //no bids

                System.out.println("Player: " + player.getPlayerName() + " not sold");
                break;


            }

        }
    }
    @UnitOfWork
    public void bidWin(List<String> bidders, List<Team> teams, Map<String, Integer> bidMap, Player player) {

        String bidWinner = bidders.stream().findFirst().get();
        int soldPrice = bidMap.get(bidWinner);
        Team bidWinnerTeamObj = teams.stream()
                .filter(t -> t.getTeamName().equals(bidWinner)).findFirst().get();
        Team team =auctionService.addPlayerToTeam(player.getId(), bidWinnerTeamObj.getId(),
                soldPrice);
        System.out.println("Player: "+player.getPlayerName()+" sold to "+team.getTeamName()+" for "+soldPrice);

    }



}
