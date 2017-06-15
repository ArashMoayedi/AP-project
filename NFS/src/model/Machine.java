package model;

import model.Body;
import model.City;
import model.Engine;

/**
 * Created by mohammadreza on 12/5/2016.
 */

public class Machine {
    final static double Time = 0.3;

    private City city;
    private String name;
    private Body body;
    private Engine engine;
    private Wheel wheel;
    private float x_center, y_center;
    private double teta;
    private int lenght, wide;
    private double weight;
    private double brake_acceleration;
    private double current_acceleration;
    private int price;
    private double speed;
    private double pureSpeed;
    private float v_x, v_y;
    private double verticalSpeed;
    private double pureVerticalSpeed;
    private double verticalV_x, verticalV_y;
    private int[][] limitPoints = new int[4][2];
    private String nodeFlag;

    public Machine (String name, Body body, Engine engine, Wheel wheel, int x_center, int y_center,
                    int lenght, int wide, double weight, double brake_acceleration, int price){
        this.name = name;
        this.body = body;
        this.engine = engine;
        this.wheel = wheel;
        this.x_center = x_center;
        this.y_center = y_center;
        this.lenght = lenght;
        this.wide = wide;
        this.weight = weight;
        this.brake_acceleration = brake_acceleration;
        this.price = price;
        this.speed = 0;
        this.pureSpeed = 0;
        this.v_x = 0;
        this.v_y = 0;
        this.verticalSpeed = 0;
        this.pureVerticalSpeed = 0;
        this.verticalV_x = 0;
        this.verticalV_y = 0;
        this.teta = 0;
    }

    public void init(City city){
        this.city = city;
    }

    public int getLength(){return this.lenght;}
    public int getWidth(){return this.wide;}


    public void setLimitPoints(){
        rotatePoint();
    }

    public double getV_x(){

        return this.v_x;
    }

    public double getV_y(){

        return this.v_y;
    }

    public int[][] getLimitPoints(){

        return this.limitPoints;
    }

    public double getWeight(){

        return this.weight;
    }


    public Body getBody(){

        return this.body;
    }

    public void setSpeed(double speed){
        this.pureSpeed = speed;
        this.speed = Math.abs(speed);
    }
    public double getPureSpeed(){
        return this.pureSpeed;
    }
    public double getSpeed(){

        return this.speed;
    }

    public void setTeta(double teta){

        this.teta = teta;
        System.out.println(teta);
    }

    public double getTeta(){

        return this.teta;
    }


    public void rotatePoint(){
        double temp1 = Math.PI /180;
        double temp = Math.pow(lenght, 2) + Math.pow(wide, 2);
        double r = Math.sqrt(temp)/2;
        double alpha = Math.atan(wide/lenght);

        limitPoints[0][0] = (int)(x_center - Math.cos(teta * temp1 + alpha) * r);
        limitPoints[0][1] = (int)(y_center - Math.sin(teta * temp1 + alpha) * r);

        limitPoints[1][0] = (int)(x_center + Math.cos(-teta * temp1 + alpha) * r);
        limitPoints[1][1] = (int)(y_center - Math.sin(-teta * temp1 + alpha) * r);

        limitPoints[2][0] = (int)(x_center - Math.sin(teta * temp1 + alpha) * r);
        limitPoints[2][1] = (int)(y_center + Math.cos(teta * temp1 + alpha) * r);

        limitPoints[3][0] = (int)(x_center + Math.sin(-teta * temp1 + alpha) * r);
        limitPoints[3][1] = (int)(y_center + Math.cos(-teta * temp1 + alpha) * r);

    }

    public void updateVerticalSpeed(double a){

        this.pureVerticalSpeed += a * Time;
        this.verticalSpeed = Math.abs(this.pureVerticalSpeed);
    }


    public void updateSpeed(double a){

        this.pureSpeed += a * Time;
//        System.out.println(a +  "= a");
        this.speed = Math.abs(this.pureSpeed);
        if (this.speed > this.engine.getMaxSpeed()){
            if (pureSpeed > 0){
                this.pureSpeed = this.engine.getMaxSpeed();
            }
            else{
                this.pureSpeed = - this.engine.getMaxSpeed();
            }
            this.speed = this.engine.getMaxSpeed();
        }
        this.setV_xy();
    }

    public void setVerticalV_xy(){

        this.verticalV_x = this.pureVerticalSpeed * Math.sin(Math.PI * teta /180);
        this.verticalV_y = this.pureVerticalSpeed * Math.cos(Math.PI * teta /180);

    }


    public void setV_xy(){

        this.v_x = (float) (this.pureSpeed * Math.cos(Math.PI * teta / 180));
        this.v_y = (float) (this.pureSpeed * Math.sin(Math.PI * teta / 180));
    }

    public void run(){
        this.current_acceleration = this.engine.getAcceleration();
        this.setX_center(current_acceleration);
        this.setY_center(current_acceleration);
        this.setLimitPoints();
//        this.allVerticalVariable();
        this.updateSpeed(current_acceleration);
    }

    public void backRun(){
        this.current_acceleration = (-1) * this.engine.getAcceleration();
        this.setY_center(current_acceleration);
        this.setX_center(current_acceleration);
        this.setLimitPoints();
//        this.allVerticalVariable();
        this.updateSpeed(current_acceleration);
        System.out.println(pureSpeed);
    }


    public void defaultMode(){
        if (this.pureSpeed < 0) {
            this.current_acceleration = this.wheel.getFriction() * 1;
            this.updateSpeed(current_acceleration);
        }
        else if (this.pureSpeed > 0){
            this.current_acceleration = -this.wheel.getFriction() * 1;
            this.updateSpeed(current_acceleration);;
        }


        if (this.speed > 0){
            this.setX_YBaseOnPureSpeed();
        }
//        this.allVerticalVariable();
    }

    public void brakeMode(){
        if (this.pureSpeed > 0.5){
            this.current_acceleration = (-1) * this.brake_acceleration;
            this.updateSpeed(current_acceleration);
        }
        else if(this.pureSpeed <- 0.5){
            this.current_acceleration = this.brake_acceleration;
            this.updateSpeed(current_acceleration);
        }


        if (this.speed > 0.1){
            this.setX_YBaseOnPureSpeed();
        }
//        this.allVerticalVariable();
    }

    public void allVerticalVariable(){
        double temp = this.wheel.getVerticalFriction() * 10;
        current_acceleration = temp;
        if (this.pureVerticalSpeed > 0){
            this.updateVerticalSpeed((-1) * current_acceleration);
            this.x_center += (temp * Math.sin(Math.PI * this.teta /180) * Time * Time) / 2 + verticalV_x * Time;
            this.y_center += (temp * Math.cos(Math.PI * this.teta /180) * Time * Time) / 2 + verticalV_y * Time;
        }
        else if(this.pureVerticalSpeed < 0){
            this.updateVerticalSpeed(current_acceleration);
            this.x_center += ((-1) * current_acceleration * Math.sin(Math.PI * this.teta /180) * Time * Time) / 2 + verticalV_x * Time;
            this.y_center += ((-1) * current_acceleration * Math.cos(Math.PI * this.teta /180) * Time * Time) / 2 + verticalV_y * Time;
        }
    }

    public void setX_YBaseOnPureSpeed(){

        if (this.pureSpeed > 0){
            this.current_acceleration = (-1) * this.wheel.getFriction() * 10;
            this.setY_center(current_acceleration);
            this.setX_center(current_acceleration);
        }
        else {
            this.current_acceleration = this.wheel.getFriction() * 10;
            this.setY_center(current_acceleration);
            this.setX_center(current_acceleration);
        }
    }

    public void rotateClockWise(){
        if (this.speed >= 0.9) {

            this.teta += this.wheel.getPowerSteering() * Time / (this.weight / 10) / this.speed;
          //  setX_center(current_acceleration);
          //  setY_center(current_acceleration);
//            System.out.println(teta + " = teta1");
        }

    }

    public void rotateCounterClockWise(){
        if (this.speed >= 0.9) {

            this.teta -= this.wheel.getPowerSteering() * Time / (this.weight / 10) / this.speed;
           // setX_center(current_acceleration);
            //setY_center(current_acceleration);
//            System.out.println(teta + " = teta2");
        }
    }


    public float getX_center() {

        return x_center;
    }


    public void setX_center(double a) {
//        System.out.println(teta + " = teta1");
//        System.out.println(v_x + "= v_x");
        this.x_center += ((a * Math.cos(Math.PI * teta / 180) * Time * Time) / 2 + this.v_x * Time);
    }


    public float getY_center() {

        return y_center;
    }


    public void setY_center(double a) {

        this.y_center += ((a * Math.sin(Math.PI * teta / 180) * Time * Time) / 2 + this.v_y * Time);
    }

    public void setBodyPower(int energy ){

        int temp = this.body.getBody_power() - energy;
        this.body.setBody_power(temp);
    }

    public int costOfMachineRepair(){
        int temp = (this.body.getAllBody_Power() - this.body.getBody_power()) * this.body.getCostOfBodyRepair();
        return temp;
    }

    public boolean isCarRaceable(){
        if (this.body.getBody_power() > 0.3 * this.body.getAllBody_Power()){

            return true;
        }
        return false;
    }


    public int getPrice() {
        return price;
    }


    public void setBody(Body body){

        this.body = body;
    }


    public void setEngine(Engine engine){

        this.engine = engine;
    }


    public void setWheel(Wheel wheel){

        this.wheel = wheel;
    }

    public Street getMachineStreet(){
        return this.getStreetPoint(this.x_center , this.y_center);
    }

    public Street getStreetPoint(float x, float y){
        for (Street s: City.streets) {
            if (s.getDirection().equals("vertical")) {

                if (x < s.getMaxPointer() && x > s.getMinPointer()){
                    if (y > s.getY_center() - s.getLenght() / 2 && y < s.getY_center() + s.getLenght() / 2){

                        return s;
                    }
                }
            }
            else{
                if (y < s.getMaxPointer() && y > s.getMinPointer()){
                    if (x > s.getX_center() - s.getLenght() / 2 && x < s.getX_center() + s.getLenght() / 2){
                        return s;
                    }
                }
            }
        }
        return null;
    }


    public Node getNode(){
        for (Node n: City.nodes) {
            if (this.x_center < n.getX_center() + n.getLength() / 2 && this.x_center > n.getX_center() - n.getLength() / 2){
                if (this.y_center < n.getY_center() + n.getWide() / 2 && this.y_center > n.getY_center() - n.getWide() / 2){
                    return n;
                }
            }
        }
        return null;
    }

    public  boolean isPointInMachine(int x , int y){

        double temp = this.teta * Math.PI / 180;
        int xprime = (int)(Math.cos(temp) * (x - this.x_center) - Math.sin(temp) * (y - this.y_center));
        int yprime = (int)(Math.sin(temp) * (x - this.x_center) + Math.cos(temp) * (y - this.y_center));
        if(xprime < this.lenght / 2 && xprime > -(this.lenght / 2)){
            if (yprime < this.wide / 2 && yprime > -(this.wide / 2 )){
                return true;
            }
        }
        return false;
    }

    public boolean isWallAccident(){

        Street machineStreet = this.getMachineStreet();
        if (machineStreet != null){
            for (int i = 0; i < 4; i++) {

                if (machineStreet.getDirection().equals("vertical")){
                    if(limitPoints[i][0] >= machineStreet.getMaxPointer() || limitPoints[i][0] <= machineStreet.getMinPointer()){
                        return true;
                    }
                }
                else{
                    if(limitPoints[i][1] >= machineStreet.getMaxPointer() || limitPoints[i][1] <= machineStreet.getMinPointer()){
                        return true;
                    }
                }
            }
        }

        else {
            Node machineNode = this.getNode();
            int[] point1 = {machineNode.getX_center() + machineNode.getLength() / 2, machineNode.getY_center() + machineNode.getWide() / 2};
            int[] point2 = {machineNode.getX_center() + machineNode.getLength() / 2, machineNode.getY_center() - machineNode.getWide() / 2};
            int[] point3 = {machineNode.getX_center() - machineNode.getLength() / 2, machineNode.getY_center() + machineNode.getWide() / 2};
            int[] point4 = {machineNode.getX_center() - machineNode.getLength() / 2, machineNode.getY_center() - machineNode.getWide() / 2};
            if (this.isPointInMachine(point1[0], point1[1])){
                return true;
            }
            else if (this.isPointInMachine(point2[0], point2[1])){
                return true;
            }
            else if (this.isPointInMachine(point3[0], point3[1])){
                return true;
            }
            else if (this.isPointInMachine(point4[0], point4[1])){
                return true;
            }

            if (machineNode.getNorthStreets().size() == 0){
                for (int i = 0; i < 4; i++){
                    if (this.limitPoints[i][1] < machineNode.getY_center() - machineNode.getWide() / 2){
                        this.nodeFlag = "n";
                        return true;
                    }
                }
            }
            if (machineNode.getSouthStreets().size() == 0){
                for (int i = 0; i < 4; i++){
                    if (this.limitPoints[i][1] > machineNode.getY_center() + machineNode.getWide() / 2){
                        this.nodeFlag = "s";
                        return true;
                    }
                }
            }
            if(machineNode.getEastStreets().size() == 0){
                for (int i = 0; i < 4; i++){
                    if (this.limitPoints[i][0] > machineNode.getX_center() + machineNode.getLength() / 2){
                        this.nodeFlag = "e";
                        return true;
                    }
                }
            }
            if(machineNode.getWestStreets().size() == 0){
                for (int i = 0; i < 4; i++){
                    if (this.limitPoints[i][0] < machineNode.getX_center() - machineNode.getLength() / 2){
                        this.nodeFlag = "w";
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public double getVerticalSpeed() {

        return verticalSpeed;
    }

    public void setVerticalSpeed(double verticalSpeed) {
        this.pureVerticalSpeed = verticalSpeed;
        this.verticalSpeed = Math.abs(this.pureVerticalSpeed);
    }

    public void update() {
        city.wallAccident();
        city.machineAccident();
    }

    public void pureSetX_center(float a){
        this.x_center += a;
    }
    public void pureSetY_center(float a){
        this.y_center += a;
    }

    public String getNodeFlag() {
        return nodeFlag;
    }

    public String getName() {
        return name;
    }

    public Engine getEngine(){return this.engine;}
    public Wheel getWheel(){return this.wheel;}

}
