package model;

import model.Street;

import java.util.ArrayList;

/**
 * Created by mohammadreza on 12/5/2016.
 */
public class Node {
    private String name;
    private int x_center, y_center;
    private String type;
    private int area;
    private int length,wide;
    private ArrayList <Street> northStreets;
    private ArrayList <Street> eastStreets;
    private ArrayList <Street> westStreets;
    private ArrayList <Street> southStreets;

    public Node (String name, int x_center, int y_center, String type){

        this.name = name;
        this.x_center = x_center;
        this.y_center = y_center;
        this.type = type;

        northStreets = new ArrayList<>();
        eastStreets = new ArrayList<>();
        westStreets = new ArrayList<>();
        southStreets = new ArrayList<>();
    }

    public ArrayList<Street> getNorthStreets(){return this.northStreets;}
    public ArrayList<Street> getSouthStreets(){return this.southStreets;}
    public ArrayList<Street> getEastStreets(){return this.eastStreets;}
    public ArrayList<Street> getWestStreets(){return this.westStreets;}

    public int getX_center() {
        return this.x_center;
    }


    public int getY_center() {
        return this.y_center;
    }


    public void addNorthStreets(Street street){
        this.northStreets.add(street);

    }

    public void addSouthStreets(Street street){
        this.southStreets.add(street);

    }

    public void addEastStreets(Street street){
        this.eastStreets.add(street);

    }

    public void addWestStreets(Street street){
        this.westStreets.add(street);

    }

    public String getName(){
        return this.name;
    }


    public int getLength() {
        this.length = 0;
        if (this.northStreets.size() > 0){
            for (Street street: northStreets) {
                this.length += street.getwide();
            }
        }
        else {
            for (Street street: southStreets) {
                this.length += street.getwide();
            }
        }

        return length;
    }

    public int getWide() {
        this.wide = 0;
        if (this.eastStreets.size() > 0) {
            for (Street street : eastStreets) {
                this.wide += street.getwide();
            }
        }
        else {
            for (Street street : westStreets) {
                this.wide += street.getwide();
            }
        }

        return wide;
    }
}
