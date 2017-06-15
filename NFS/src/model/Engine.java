package model;

/**
 * Created by mohammadreza on 12/5/2016.
 */
public class Engine {
    private String name;
    private double acceleration;
    private double maxSpeed;
    private int price;

    public Engine (String name, double acceleration, double maxSpeed, int price){
        this.name = name;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.price = price;
    }


    public double getAcceleration() {
        return acceleration;
    }

    public double getMaxSpeed() {
        return maxSpeed;
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
