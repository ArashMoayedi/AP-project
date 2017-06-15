package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mohammadreza on 12/5/2016.
 */
public class Race {
    protected String name;
    protected ArrayList <Player> players;
    protected int numberOfPlayers = 1;
    protected Way way;
    protected HashMap<Player, Integer> player_counterTime;
    protected int theNumberOfLoop;
    private int minPopularity;
    private int[] awardPopularity;
    private int[] awardMoney;
    protected HashMap<Player, Integer> bestPlayers_record = new HashMap<>();
    protected ArrayList <Player> bestPlayers = new ArrayList<>();

    public Race(String name1, Way way1, int theNumberOfLoop1){
        this.name = name1;
        this.way = way1;
        this.theNumberOfLoop = theNumberOfLoop1;
        this.numberOfPlayers = 1;
        this.players = new ArrayList<>();
        this.player_counterTime = new HashMap<>();

    }

    public Race(String name1, Way way1, int theNumberOfPlayers1, int theNumberOfLoop1, int minPopularity, int[] awardPopularity, int[] awards){
        this.name = name1;
        this.way = way1;
        this.theNumberOfLoop = theNumberOfLoop1;
        this.numberOfPlayers = theNumberOfPlayers1;
        this.players = new ArrayList<>();
        this.player_counterTime = new HashMap<>();

        this.awardPopularity = new int[3];
        this.awardMoney = new int[3];
        this.minPopularity = minPopularity;
        this.awardPopularity = awardPopularity;
        this.awardMoney = awards;

    }

    public void start(Player player){

        if(numberOfPlayers == 1){
            System.out.println("start func");
            addPlayerInTrainingRace(player);

        }
        else if(numberOfPlayers > 1){
            addPlayerInCompetetiveRace(player);

        }


    }




    public void startRace(){

        if (this.numberOfPlayers == this.players.size()){

            this.duringRace();
        }
    }

    public void firstSetPlayerHashmap(){
        for (Player player : this.players){
            int [] temp = new int[numberOfPlayers];
            player.firstSetRecordOfEachLoopOfRace(this, temp);
        }
    }

    public void duringRace(){
        for (int j = 0; j < this.theNumberOfLoop; j++){
            for (Player player : this.players){
                int time = 0;

                for (Node node : this.way.getNodes()){
                    while (!player.getCurrentMachine().getNode().equals(node)){
                        time++;
                        this.player_counterTime.put(player, time);
                        System.out.println("duringRace WHILE");
                        if (player.getCurrentMachine().getBody().getBody_power() <= 1){
                            removePlayer(player);
                        }
                    }
                }
                player.setRecordOfEachLoopOfRace(this, j, (int)(this.player_counterTime.get(player) * Machine.Time));
            }
        }
        this.setFullRecordAndBestPlayers();

    }

    public void addPlayer(Player player){
        if (player.getCurrentMachine().getBody().getBody_power() >= player.getCurrentMachine().getBody().getAllBody_Power()*3/10){
            this.players.add(player);
            System.out.println("addPlayer func");
        }

        this.startRace();
    }

    public void removePlayer(Player player){

        this.players.remove(player);
    }

    public void setFullRecordAndBestPlayers(){
        for (Player player : this.players){

            int temp =0;
            for (int record : player.getRecordOfEachLopp(this)) {
                temp += record;
            }

            this.bestPlayers_record.put(player, temp);

            if (player.getFullRecordOfRace(this) > temp){
                player.setFullRecordOfRace(this, temp);
            }

        }

        this.awardRace();

    }


    public String getName(){
        return this.name;
    }

    public Way getWay(){
        return this.way;
    }






//class TrainingRace extends Race{
//    public TrainingRace(String name1, Way way1, int theNumberOfLoop1) {
//        name = name1;
//        way = way1;
//        theNumberOfLoop = theNumberOfLoop1;
//        numberOfPlayers = 1;
//    }

    public void addPlayerInTrainingRace(Player player){
        addPlayer(player);
    }

//}




//class CompetetiveRace extends Race{
//    private int minPopularity;
//    private int[] awardPopularity;
//    private int[] awardMoney;

//    public CompetetiveRace(String name1, Way way1, int theNumberOfPlayers1, int theNumberOfLoop1, int minPopularity, int[] awardPopularity, int[] awards) {
//        name = name1;
//        way = way1;
//        theNumberOfLoop = theNumberOfLoop1;
//        numberOfPlayers = theNumberOfPlayers1;
//
//        this.awardPopularity = new int[3];
//        this.awardMoney = new int[3];
//        this.minPopularity = minPopularity;
//        this.awardPopularity = awardPopularity;
//        this.awardMoney = awards;
//    }

    public void addPlayerInCompetetiveRace(Player player){
        if (this.minPopularity <= player.getPopularity()){
            addPlayer(player);
        }
    }

    public void awardRace(){

        ArrayList <String> playernameSpaceRecord = new ArrayList<>();
        ArrayList <Integer> recordOfPlayer = new ArrayList<>();


        for (Player player : players) {

            playernameSpaceRecord.add(player.getName() + " " + Integer.toString(player.getFullRecordOfRace(this)));
            recordOfPlayer.add(player.getFullRecordOfRace(this));
        }

        recordOfPlayer.sort(null);

        for (int i = 0; i < recordOfPlayer.size() ; i++) {
            for (int j = 0; j < playernameSpaceRecord.size() ; j++) {
                if (playernameSpaceRecord.get(j).contains(Integer.toString(recordOfPlayer.get(i)))){
                    for (Player p: players) {
                        if (p.getName().equals(playernameSpaceRecord.get(j).substring(0, playernameSpaceRecord.get(j).indexOf(" ")-1))){
                            bestPlayers.add(p);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 2; i++) {

            bestPlayers.get(i).setMoney(awardMoney[i]);
            bestPlayers.get(i).setPopularity(awardPopularity[i]);
        }
    }
}