package model;

/**
 * Created by mohammadreza on 12/5/2016.
 */
public class Wheel {

    private String name;
    private double verticalFriction;
    private double powerSteering;
    private double friction;
    private int price;

    public Wheel (String name, double verticalFriction, double powerSteering, double friction, int price){
        this.name = name;
        this.verticalFriction = verticalFriction;
        this.powerSteering = powerSteering;
        this.friction = friction;
        this.price = price;

    }

    public double getVerticalFriction(){
        return this.verticalFriction;
    }

    public double getPowerSteering() {
        return powerSteering;
    }


    public double getFriction() {
        return friction;
    }


    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
