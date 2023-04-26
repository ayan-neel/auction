package org.example.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.hibernate.UnitOfWork;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.checkerframework.checker.units.qual.Time;
import org.example.api.Representation;
import org.example.api.model.*;
import org.example.core.service.AuctionService;
import org.example.db.entity.Player;
import org.example.db.entity.Team;

import java.util.*;
import java.util.stream.Collectors;

@Path("/auction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuctionResource {

    private final AuctionService auctionService;

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
        return new Representation<>(code,auctionService.getPlayerById(id));

    }
    @Path("/getPlayers")
    @GET
    @Timed
    @UnitOfWork
    public Representation<List<Player>> getPlayers(@PathParam("id") int id){
        System.out.println("invoked");
        int code = 200;
        return new Representation<>(code,auctionService.getPlayers());

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

        int code = 201;
        return new Representation<>(code, auctionService.savePlayer(playerRequest));

    }
    @Path("/players/edit")
    @PUT
    @Time
    @UnitOfWork
    public Representation<Player> editPlayer(@Valid PlayerRequest playerRequest) {

        int code = 204;
        return new Representation<>(code, auctionService.editPlayer(playerRequest));

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

        int code=201;
        return new Representation<>(code,auctionService.saveTeam(teamRequest));

    }

    //for testing purpose
    @Path("/auction/buy")
    @PUT
    @Timed
    @UnitOfWork
    public Representation<Team> addPlayerToTeam(BidRequest bidRequest){

        int playerId = bidRequest.getPlayerId();
        int teamId = bidRequest.getTeamId();
        int code = 200;
        return new Representation<>(code,auctionService.addPlayerToTeam(playerId,teamId));

    }

    @Path("/start")
    @GET
    @Timed
    @UnitOfWork
    public Representation<List<Team>> startAuction(){

        List<Team> teams;
        teams = auctionService.getTeams();
        List<Player> players = auctionService.getPlayers().stream()
                .filter(p -> p.isFlag()).collect(Collectors.toList());


        startAuction(players, teams);

        System.out.println("#####End of Auction###########");
        int code =200;
        return new Representation<>(code,auctionService.getTeams());


    }
    private void startAuction(List<Player> players, List<Team> teams) {

        for (Player player : players) {

            Map<String, Integer> bidMap = teams.stream().filter(t->t.getBudget()-player.getBasePrice()>0)
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
        Stack<String>bidStack = new Stack<>();
        Scanner sc = new Scanner(System.in);
        while (bidders.size() >= 1) {
            if (bidders.size() == 1 && bidMap.get(bidders.stream().findFirst().get()) > 0) //bid win
            {
                bidWin(bidders, teams, bidMap, player);
                break;
            }
            for (String team : bidders) {
                System.out.println("Team: " + team);
                System.out.println("Bid?:");
                String choice = sc.nextLine();
                if (Objects.equals(sc, "y")||Objects.equals(sc,"Y")) {
                    basePrice += 500;
                    bidMap.put(team, basePrice);
                    bidStack.push(team);
                    System.out.println(
                            "#######Team: " + team + " has bid" + "\n New bid price :" + basePrice);
                } else {
                    bidMap.put(team, -1);
                }

            }

            bidders = bidMap.entrySet().stream().filter(e -> e.getValue() >= 0).map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            int finalBasePrice = basePrice;

            List<String>canBidFurtherTeams = teams.stream().filter(t->t.getBudget()- finalBasePrice >=0).map(t->t.getTeamName()).collect(Collectors.toList());

            bidders = bidders.stream().filter(canBidFurtherTeams::contains).collect(Collectors.toList());


            if (bidders.isEmpty()) { //no bids
                if(bidStack.isEmpty()) {
                    System.out.println("Player: " + player.getPlayerName() + " not sold");
                }
                else{
                    String bidWinner = bidStack.pop();
                    bidders.add(bidWinner);
                    bidMap.put(bidWinner,basePrice);
                    bidWin(bidders, teams, bidMap, player);

                }
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
        System.out.println("Player : "+player.getPlayerName()+" sold to "+team.getTeamName()+" for: "+soldPrice+ "\tremaining budget: "+team.getBudget());

    }



}
