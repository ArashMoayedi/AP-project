package model;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by mohammadreza on 12/5/2016.
 */
public class City {
    static public ArrayList <Node> nodes = new ArrayList<>();
    static public ArrayList <Street> streets = new ArrayList<>();
    static public ArrayList <Player> players = new ArrayList<>();
    static public ArrayList <Race> races = new ArrayList<>();
    static public ArrayList <Machine> machines = new ArrayList<>();
    static public ArrayList <Engine> engines = new ArrayList<>();
    static public ArrayList <Wheel> wheels = new ArrayList<>();
    static public ArrayList <Body> bodies = new ArrayList<>();
    private boolean pastFlag;
    private boolean nowFlag;
    private Player currentPlayer;

    public City(){

        this.pastFlag = false;
        this.nowFlag = false;
        this.currentPlayer = null;

    }

    public  boolean isMachineAccident(Machine machine1, Machine machine2){
        int [][] machine1LimitPoints = machine1.getLimitPoints();
        int [][] machine2LimitPoints = machine2.getLimitPoints();

        for ( int [] a: machine1LimitPoints) {
            if (machine2.isPointInMachine(a[0], a[1])){
                return true;
            }
        }
        for ( int [] b: machine2LimitPoints) {
            if (machine1.isPointInMachine(b[0], b[1])){
                return true;
            }
        }
        return false;
    }


    public void addNodes(Node node){
        this.nodes.add(node);

    }


    public void addStreets(Street street){
        this.streets.add(street);
    }


    public Node[] getNodes(){
        Node[] toReturn = new Node[this.nodes.size()];
        toReturn = nodes.toArray(toReturn);
        return toReturn;
    }



    public Node getTheNodeOfStreet(String name){
        Node toReturn = null;
        for (Node node : this.nodes){
            if (node.getName().equalsIgnoreCase(name)){
                toReturn = node;
                break;
            }
        }
        return toReturn;
    }

    public void wallAccident(){
            Machine machine = this.currentPlayer.getCurrentMachine();
            if (machine.isWallAccident()){
                try
                {
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(new File("crash_x.wav")));
                    clip.start();
                }
                catch (Exception exc)
                {
                    exc.printStackTrace(System.out);
                }
                int temp = (int)(3 * (0.5 * machine.getWeight() * Math.pow(machine.getSpeed(), 2)) / 4);
                machine.getBody().setBody_power(machine.getBody().getBody_power() - temp);
                if(machine.getBody().getBody_power() < 0 ){
                    machine.getBody().setBody_power(0);
                }
                    if (machine.getMachineStreet() != null) {
                        if (machine.getMachineStreet().getDirection().equals("vertical")) {
                            machine.setTeta(180 - machine.getTeta());
                            if (machine.getX_center() > machine.getMachineStreet().getX_center()) {
                                machine.pureSetX_center(-1);
                            } else {
                                machine.pureSetX_center(1);
                            }
                        } else {
                            machine.setTeta(-machine.getTeta());
                            if (machine.getY_center() > machine.getMachineStreet().getY_center()) {
                                machine.pureSetY_center(-1);
                            } else {
                                machine.pureSetY_center(1);
                            }
                        }
                    }
                    else {
                        if (machine.getNodeFlag().equals("e") || machine.getNodeFlag().equals("w")) {
                            machine.setTeta(180 - machine.getTeta());
                            if (machine.getNodeFlag().equals("e")) {
                                machine.pureSetX_center(-1);
                            } else {
                                machine.pureSetX_center(1);
                            }
                        } else {
                            machine.setTeta(-machine.getTeta());
                            if (machine.getNodeFlag().equals("n")) {
                                machine.pureSetY_center(1);
                            } else {
                                machine.pureSetY_center(-1);
                            }
                        }

                    }


                    machine.setSpeed(machine.getPureSpeed() / 2 );

            }
    }

    public void machineAccident(){
        for (int i = 0; i < this.machines.size(); i++){
            for (int j = i; j < this.machines.size(); j++) {
                if (!machines.get(i).equals(machines.get(j))){
                    if (this.isMachineAccident(machines.get(i), machines.get(j))){
                        this.changeOfMachineAccident(machines.get(i), machines.get(j));
                    }
                }
            }
        }
    }

    public void changeOfMachineAccident(Machine machine1, Machine machine2){

        double t1 = machine1.getV_x();
        double t2 = machine2.getV_x();
        double t3 = machine1.getV_y();
        double t4 = machine2.getV_y();

        double t1Prime = ( ((machine1.getWeight() - machine2.getWeight()/2) * t1) + ((machine2.getWeight() + machine1.getWeight()/2) * t2) )
                / (machine1.getWeight() + machine2.getWeight());

        double t2Prime = ( ((machine2.getWeight() - machine1.getWeight()/2) * t2) + ((machine1.getWeight() + machine2.getWeight()/2) * t1) )
                / (machine1.getWeight() + machine2.getWeight());

        double t3Prime = ( ((machine1.getWeight() - machine2.getWeight()/2) * t3) + ((machine2.getWeight() + machine1.getWeight()/2) * t4) )
                / (machine1.getWeight() + machine2.getWeight());

        double t4Prime = ( ((machine2.getWeight() - machine1.getWeight()/2) * t4) + ((machine1.getWeight() + machine2.getWeight()/2) * t3) )
                / (machine1.getWeight() + machine2.getWeight());

        double Q_x = (3/8) * Math.pow( machine1.getV_x() - machine2.getV_x(), 2) *(machine1.getWeight() * machine2.getWeight() /(machine1.getWeight() + machine2.getWeight()));
        double Q_y = (3/8) * Math.pow( machine1.getV_y() - machine2.getV_y(), 2) *(machine1.getWeight() * machine2.getWeight() /(machine1.getWeight() + machine2.getWeight()));
        double Q = Q_x + Q_y;
        double Q_machine1 = machine2.getWeight()/(machine1.getWeight() + machine2.getWeight()) * Q;
        double Q_machine2 = machine1.getWeight()/(machine1.getWeight() + machine2.getWeight()) * Q;

        machine1.getBody().setBody_power(machine1.getBody().getBody_power() - (int)Q_machine1);
        machine2.getBody().setBody_power(machine2.getBody().getBody_power() - (int)Q_machine2);

        double directMachine1Speed = (t1Prime * Math.cos(machine1.getTeta() * Math.PI / 180)) + (t3Prime * Math.cos(machine1.getTeta() * Math.PI / 180));
        double directMachine2Speed = (t2Prime * Math.cos(machine1.getTeta() * Math.PI / 180)) + (t4Prime * Math.cos(machine1.getTeta() * Math.PI / 180));
        double verticalMachine1Speed = (t1Prime * Math.sin(machine1.getTeta() * Math.PI / 180)) + (t3Prime * Math.sin(machine1.getTeta() * Math.PI / 180));
        double verticalMachine2Speed = (t2Prime * Math.sin(machine1.getTeta() * Math.PI / 180)) + (t4Prime * Math.sin(machine1.getTeta() * Math.PI / 180));

        machine1.setSpeed(directMachine1Speed);
        machine2.setSpeed(directMachine2Speed);


        machine1.setVerticalSpeed(verticalMachine1Speed);
        machine2.setVerticalSpeed(verticalMachine2Speed);

    }


    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }
}

