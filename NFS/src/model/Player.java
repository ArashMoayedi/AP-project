package model;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mohammadreza on 12/5/2016.
 */
public class Player {

    private String name;
    private ArrayList <Machine> machines;
    private  Machine currentMachine;
    private ArrayList <Race> races;
    private int popularity;
    private int money;
    private HashMap <Race, int[]> recordOfEachLoopOfRace;
    private HashMap <Race, Integer> fullRecordOfRace;


    public Player (String name){

        this.name = name;
        this.machines = new ArrayList<>();
        this.races = new ArrayList<>();
        this.popularity = 50;
        this.money = 50;
        this.currentMachine = null;
        this.recordOfEachLoopOfRace = new HashMap<>();
        this.fullRecordOfRace = new HashMap<>();
    }


    public ArrayList<Machine> getMachines(){
        return this.machines;
    }

    public boolean buy(Machine machine){
        if (this.money >= machine.getPrice()) {
            this.machines.add(machine);
            this.money -= machine.getPrice();
            return true;
        }
        else {
            return false;
        }
    }

    public void sell(Machine machine){
        this.machines.remove(machine);
        this.money += machine.getPrice();
    }

    public boolean buyEngineForMachin(Machine machine, Engine engine){
        if (this.getMoney() >= engine.getPrice()) {
            machine.setEngine(engine);
            this.money -= engine.getPrice();
            return true;
        }
        else {
            return false;
        }
    }

    public boolean buyWeelForMachine(Machine machine, Wheel wheel){
        if (this.getMoney() >= wheel.getPrice()) {
            machine.setWheel(wheel);
            this.money -= wheel.getPrice();
            return true;
        }
        else {
            return false;
        }
    }

    public boolean buyBodyForMachine(Machine machine, Body body){
        if (this.getMoney() >= body.getPrice()) {
            machine.setBody(body);
            this.money -= body.getPrice();
            return true;
        }
        else {
            return false;
        }
    }


    public void setCurrentMachine(Machine currentMachine) {
        if (machines.contains(currentMachine)){

            this.currentMachine = currentMachine;
        }
        else{
            System.out.println("This machine not exist.");
        }
    }


    public Machine getCurrentMachine(){

        return this.currentMachine;
    }

    public Street getCurrentStreet() {

        return currentMachine.getMachineStreet();

    }

    public Node getCureentNode() {

        return currentMachine.getNode();
    }

    public void firstSetRecordOfEachLoopOfRace(Race race, int[] temp) {

        this.recordOfEachLoopOfRace.put(race, temp);
    }

    public void setRecordOfEachLoopOfRace(Race race, int loopNumber, int time){

        this.recordOfEachLoopOfRace.get(race)[loopNumber] = time;
    }

    public int[] getRecordOfEachLopp(Race race){

        return this.recordOfEachLoopOfRace.get(race);
    }


    public int getFullRecordOfRace(Race race) {

        return this.fullRecordOfRace.get(race);
    }


    public void setFullRecordOfRace(Race race, int record) {

        this.fullRecordOfRace.put(race, record);
    }

    public int getPopularity() {

        return popularity;
    }

    public void setPopularity(int popularity, Race race) {
        if (!this.fullRecordOfRace.keySet().contains(race)){

            this.popularity += popularity;
        }
    }

    public int getMoney() {

        return money;
    }

    public void setMoney(int money) {

        this.money += money;
    }

    public void setPopularity(int popularity){
        this.popularity += popularity;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Race> getRaces() {
        return races;
    }

    public void addMachine(Machine machine){
        this.machines.add(machine);
    }

    public void addRace(Race race){
        this.races.add(race);
    }

    public void addFullRecordOfRace(Race race, Integer a){
        this.fullRecordOfRace.put(race, a);
    }


}
