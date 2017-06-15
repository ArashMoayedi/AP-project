package model;

/**
 * Created by mohammadreza on 12/5/2016.
 */
public class Street {

    private final int lengthOfLine = 20; // 3 meter = 60 pixel

    private String type;
    private String direction;
    private Node firstNode, secondNode;
    private int numberOfLine;
    private int maxPointer, minPointer;
    private int x_center , y_center;
    private int lenght;

    public Street (String type, Node firstNode, Node secondNode, int numberOfLine, String direction, int x_center, int y_center){
        this.type = type;
        this.firstNode = firstNode;
        this.secondNode = secondNode;
        this.numberOfLine = numberOfLine;
        this.x_center = x_center;
        this.y_center = y_center;
        this.direction = direction;

        if (this.direction.equals("vertical") ){
            this.maxPointer = this.x_center + (this.numberOfLine * lengthOfLine) / 2;
            this.minPointer = this.x_center - (this.numberOfLine * lengthOfLine) / 2;

        }
        else {
            this.maxPointer = this.y_center + (this.numberOfLine * lengthOfLine) / 2;
            this.minPointer = this.y_center - (this.numberOfLine * lengthOfLine) / 2;
        }


    }

    public int getwide(){
        return lengthOfLine * this.numberOfLine;
    }

    public void setStreetsOfFirstNodeAndSecondNode(){

        if (this.direction.equals("vertical")){
            if (this.firstNode.getY_center() < this.secondNode.getY_center()){
                this.firstNode.addSouthStreets(this);
                this.secondNode.addNorthStreets(this);
            }
            else {
                this.firstNode.addNorthStreets(this);
                this.secondNode.addSouthStreets(this);
            }
        }

        else {
            if (this.firstNode.getX_center() < this.secondNode.getX_center()){
                this.firstNode.addEastStreets(this);
                this.secondNode.addWestStreets(this);

            }
            else {
                this.firstNode.addWestStreets(this);
                this.secondNode.addEastStreets(this);

            }
        }

    }

    public void setLenght(){
        if (this.direction.equals("vertical")){
            if (this.firstNode.getY_center() < this.secondNode.getY_center()){
                int temp = (firstNode.getY_center() + (firstNode.getWide()/2)) - (secondNode.getY_center() - (secondNode.getWide()/2));
                this.lenght = Math.abs(temp);
            }
            else {
                int temp = (firstNode.getY_center() - (firstNode.getWide()/2)) - (secondNode.getY_center() + (secondNode.getWide()/2));
                this.lenght = Math.abs(temp);
            }
        }

        else {
            if (this.firstNode.getX_center() < this.secondNode.getX_center()){
                int temp = (firstNode.getX_center() + firstNode.getLength()/2) - (secondNode.getX_center() - secondNode.getLength()/2);
                this.lenght = Math.abs(temp);
            }
            else {
                int temp = (firstNode.getX_center() - firstNode.getLength()/2) - (secondNode.getX_center() + secondNode.getLength()/2);
                this.lenght = Math.abs(temp);
            }
        }

    }

    public String getDirection(){
        return this.direction;
    }

    public int getLenght() {
        return this.lenght;
    }

    public int getMaxPointer() {
        return this.maxPointer;
    }

    public int getMinPointer() {
        return this.minPointer;
    }

    public int getX_center(){
        return this.x_center;
    }

    public int getY_center(){
        return this.y_center;
    }

}
