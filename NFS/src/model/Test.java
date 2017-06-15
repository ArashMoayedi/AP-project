package model;

import model.City;
import model.Node;
import model.Street;
import view.DrawMap;
import view.DrawProfile;
import model.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sina on 12/7/2016.
 */
public class Test {
    public static void main(String[] args) throws IOException {
        City city = new City();
        DrawProfile drawProfile = new DrawProfile();

        //aval bayad az file dade shode Node ha va Street ha ro besazim.
        //bad az sakhte shodane har street bayad methode setFirstNode... farakhani shavad.

        File mapFile = new File("map1.txt");
        Scanner input = new Scanner(mapFile);
        int theNumberOfNodes = input.nextInt();
        for (int i = 0; i < theNumberOfNodes; i++) {

            String name = input.next();
            int x_center = input.nextInt();
            int y_center = input.nextInt();
            String type = input.next();
            Node node = new Node(name, x_center, y_center, type);
            city.addNodes(node);


        }


        int theNumberOfStreets = input.nextInt();

        for (int i = 0; i < theNumberOfStreets; i++) {
            String type = input.next();
            String nameOfFirstNode = input.next();
            Node firstNode = city.getTheNodeOfStreet(nameOfFirstNode);
            String nameOfSecondNode = input.next();
            Node secondNode = city.getTheNodeOfStreet(nameOfSecondNode);
            int numberOfline = input.nextInt();
            String direction = input.next();
            int x_center = input.nextInt();
            int y_center = input.nextInt();
            Street street = new Street(type, firstNode, secondNode, numberOfline, direction, x_center, y_center);
            city.addStreets(street);
            street.setStreetsOfFirstNodeAndSecondNode();

        }
        input.close();
        for (Street streets : City.streets) {
            streets.setLenght();
        }

        ////////////////////////////////////////////////////////////
        //new kardane dade haye avvalie:
        Node [] nodes = {City.nodes.get(4), City.nodes.get(5), City.nodes.get(7), City.nodes.get(8), City.nodes.get(10), City.nodes.get(9)};
        Way way = new Way(nodes);
        Race training1 = new Race("training1", way, 2);
        int[] awardpopularity = {30, 20, 10};
        int[] awards = {30, 20, 10};
        Race competetive1 = new Race("competetive1", way, 2, 2, 20, awardpopularity, awards);
        Engine engine1 = new Engine("engine1", 0.2, 8.0, 10);
        Engine engine2 = new Engine("engine2", 1.0, 6.0, 20);
        Engine engine3 = new Engine("engine3", 1.2, 10.0, 30);
        Wheel wheel1 = new Wheel("wheel1", 2.0, 5.0 , 0.6, 1);
        Wheel wheel2 = new Wheel("wheel2", 2.4, 5.0 , 0.5, 2);
        Wheel wheel3 = new Wheel("wheel3", 2.8, 5.0 , 0.4, 3);
        Body body1 = new Body("body1", 1000, 3, 5);
        Body body2 = new Body("body2", 1500, 6, 10);
        Body body3 = new Body("body3", 2000, 9, 15);
        Machine machin1 = new Machine("car1", body1, engine1, wheel1, 320, 160, 10, 10, 1.5, 2, 40);
        Machine machin2 = new Machine("car2", body1, engine2, wheel1, 40, 160, 10, 10, 1.5, 3, 60);
        Machine machin3 = new Machine("car3", body2, engine1, wheel2, 40, 200, 10, 10, 1.5, 4, 80);
        machin1.init(city);
        machin2.init(city);
        machin3.init(city);
        City.machines.add(machin1);
        City.machines.add(machin2);
       // City.machines.add(machin3);
        City.engines.add(engine1);
        City.engines.add(engine2);
        City.engines.add(engine3);
        City.wheels.add(wheel1);
        City.wheels.add(wheel2);
        City.wheels.add(wheel3);
        City.bodies.add(body1);
        City.bodies.add(body2);
        City.bodies.add(body3);
        City.races.add(training1);
        City.races.add(competetive1);
        //////////////////////////////////////////////////////////////


        drawProfile.init(city);
        drawProfile.firstState();


    }
}