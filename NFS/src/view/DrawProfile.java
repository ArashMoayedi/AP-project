package view;

import com.sun.media.jfxmedia.events.PlayerStateEvent;
import com.sun.org.apache.xml.internal.security.encryption.CipherData;
import com.sun.scenario.effect.impl.sw.java.JSWBoxBlurPeer;
import controller.GameController;
import model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by sina on 2/3/2017.
 */
public class DrawProfile extends JComponent implements ActionListener {
    private City city;
    JFrame firstFrame = new JFrame();
    JPanel firstPanel = new JPanel();
    JButton signUp = new JButton("signUp");
    JButton logIn = new JButton("logIn");


    public void init(City city){
        this.city = city;


    }

    public void firstState() throws FileNotFoundException {
        firstPanel.add(signUp);
        firstPanel.add(logIn);
        firstFrame.getContentPane().add(firstPanel);
        firstFrame.setLocationRelativeTo(null);
        firstFrame.pack();
        firstFrame.setVisible(true);
        firstFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            startNew();
        } catch (IOException e) {
            e.printStackTrace();
        }

        signUp.addActionListener(this);
        logIn.addActionListener(this);


    }


    public void startNew() throws IOException {
        for (int i = 0; i < Files.readAllLines(Paths.get("players_profile.txt")).size(); i++) {
            if (i%10 == 0) {
                String name = Files.readAllLines(Paths.get("players_profile.txt")).get(i);

                Player player = new Player(name);
                city.addPlayer(player);

                for (int j = i + 1; j <= i + 6; j++) {
                    if (Files.readAllLines(Paths.get("players_profile.txt")).get(j).length() > 0) {
                        if (j == i + 1) {
                            String[] machines = Files.readAllLines(Paths.get("players_profile.txt")).get(j).split(" ");
                            for (String machine : machines) {
                                for (Machine m : City.machines) {
                                    if (m.getName().equals(machine)) {
                                        player.addMachine(m);
                                    }
                                }
                            }
                        } else if (j == i + 2) {
                            String nameCar = Files.readAllLines(Paths.get("players_profile.txt")).get(j);
                            for (Machine m : City.machines) {
                                if (m.getName().equals(nameCar)) {
                                    player.setCurrentMachine(m);
                                }
                            }
                        } else if (j == i + 3) {
                            String mony = Files.readAllLines(Paths.get("players_profile.txt")).get(j);
                            player.setMoney(Integer.parseInt(mony) - player.getMoney());
                        } else if (j == i + 4) {
                            String popularity = Files.readAllLines(Paths.get("players_profile.txt")).get(j);
                            player.setPopularity(Integer.parseInt(popularity) - player.getPopularity());
                        } else if (j == i + 5) {
                            String[] races = Files.readAllLines(Paths.get("players_profile.txt")).get(j).split(" ");
                            for (String race : races) {
                                for (Race r : City.races) {
                                    if (r.getName().equals(race)) {
                                        player.addRace(r);
                                    }
                                }
                            }
                        } else if (j == i + 6) {
                            String[] records = Files.readAllLines(Paths.get("players_profile.txt")).get(j).split(" ");
                            for (String record : records) {
                                for (Race r : player.getRaces()) {
                                    player.addFullRecordOfRace(r, Integer.parseInt(record));
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        firstFrame.dispose();
        String inputName = JOptionPane.showInputDialog("Enter your name:");
        if(e.getSource().equals(logIn)){
            try {
                handleFile(inputName);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else if(e.getSource().equals(signUp)) {

            try {
                signUp(inputName);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    public void signUp(String name) throws IOException {
        Player player = new Player(name);
        City.players.add(player);
        city.setCurrentPlayer(player);

        append(name);
        for (int k = 0; k < 9; k++){
            if (k == 4 || k == 5){
                append("50");
            }
            else {append("");}
        }
        profileMode();

    }

    public void append(String string) throws IOException {
        FileWriter fw = new FileWriter("players_profile.txt",true);
        fw.write(string + "\n");
        fw.close();
    }



    public void handleFile(String input) throws IOException {
        boolean flag = false;
        for(int i = 0; i < Files.readAllLines(Paths.get("players_profile.txt")).size(); i++) {
            if (i%10 == 0) {
                String name = Files.readAllLines(Paths.get("players_profile.txt")).get(i);
                if (input.equals(name)){
                    for (Player player:City.players){
                        if (player.getName().equals(name)) {
                            city.setCurrentPlayer(player);
                            flag = true;
                        }

                    }

                    profileMode();

                    break;
                }
            }

        }
        if (!flag){

            JOptionPane.showMessageDialog(null, "This player not found");
        }
    }

    public void writeFile(String string, int line) throws IOException {
        Scanner reader = new Scanner(new File("players_profile.txt"));
        PrintWriter writer = new PrintWriter("second.txt");
        for (int i = 0; i < line;i++){
            writer.println(reader.nextLine());
        }
        reader.nextLine();
        writer.println(string);
        while (reader.hasNextLine()){
            writer.println(reader.nextLine());
        }

        reader.close();
        writer.close();
        Scanner reader1 = new Scanner(new File("second.txt"));
        PrintWriter writer1 = new PrintWriter("players_profile.txt");
        while (reader1.hasNextLine()){
            writer1.println(reader1.nextLine());
        }
        reader1.close();
        writer1.close();
    }

    public void startGame() throws IOException {


        JFrame gameFrame = new JFrame();
        gameFrame.setSize(614, 700);
        gameFrame.setLocationRelativeTo(null);



        DrawMap drawMap = new DrawMap();
        gameFrame.getContentPane().add(drawMap);

        GameController gameController = new GameController();
        gameController.init(city.getCurrentPlayer().getCurrentMachine(), drawMap, this);
        drawMap.init(gameController, city , this);

        gameController.start();
        gameController.startMultiKey();

        gameController.time();


        gameFrame.setVisible(true);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void buyMachineMode(){
        JFrame buyMachineFrame = new JFrame();
        JPanel buyMachinePanel = new JPanel();
        JButton machine1 = new JButton("buy car1");
        JButton machine2 = new JButton("buy car2");
        JButton machine3 = new JButton("buy car3");
        JButton back = new JButton("back");

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyMachineFrame.dispose();
                profileMode();
            }
        });

        machine1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Machine machine : City.machines) {
                    if (machine.getName().equals("car1")){
                        if (city.getCurrentPlayer().buy(machine)){
                            JOptionPane.showMessageDialog(null, "done");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "your mony not enough");
                        }
                    }
                }
        }

        });
        machine2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Machine machine : City.machines) {
                    if (machine.getName().equals("car2")){
                        if (city.getCurrentPlayer().buy(machine)){
                            JOptionPane.showMessageDialog(null, "done");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "your mony not enough");
                        }
                    }
                }
            }

        });

        machine3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Machine machine : City.machines) {
                    if (machine.getName().equals("car3")){
                        if (city.getCurrentPlayer().buy(machine)){
                            JOptionPane.showMessageDialog(null, "done");
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "your mony not enough");
                        }
                    }
                }
            }

        });

        if (city.getCurrentPlayer().getMachines().size() == 1){
            city.getCurrentPlayer().setCurrentMachine(city.getCurrentPlayer().getMachines().get(0));
        }
        buyMachinePanel.add(machine1);
        buyMachinePanel.add(machine2);
        buyMachinePanel.add(machine3);
        buyMachinePanel.add(back);
        buyMachineFrame.getContentPane().add(buyMachinePanel);
        buyMachineFrame.setVisible(true);
        buyMachineFrame.setLocationRelativeTo(null);
        buyMachineFrame.pack();
        buyMachineFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void profileMode(){
        JFrame profileFrame = new JFrame();
        JPanel profilePanel = new JPanel();
        JButton startButton = new JButton("start");
        JButton buyMachinButton = new JButton("buyCar");
        JButton editMachine = new JButton("editCar");
        JButton setCurrentMachine = new JButton("setCurrentCar");
        JButton training = new JButton("trainingRace");
        JButton competetive = new JButton("competetiveRace");


        training.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Race race = city.races.get(0);
                city.getCurrentPlayer().addRace(race);
                double x1 = race.getWay().getNodes()[0].getX_center();
                double x2 = race.getWay().getNodes()[1].getX_center();
                double y1 = race.getWay().getNodes()[0].getY_center();
                double y2 = race.getWay().getNodes()[1].getY_center();

                city.getCurrentPlayer().getCurrentMachine().setX_center(x1);
                city.getCurrentPlayer().getCurrentMachine().setY_center(y1);

                if (x1 - x2 == 0){
                    if (y1 > y2){
                        city.getCurrentPlayer().getCurrentMachine().setTeta(-90);
                    }
                    else {
                        city.getCurrentPlayer().getCurrentMachine().setTeta(90);

                    }
                }
                else {
                    if (x1 > x2){
                        city.getCurrentPlayer().getCurrentMachine().setTeta(180);
                    }
                    else {
                        city.getCurrentPlayer().getCurrentMachine().setTeta(0);

                    }
                }
                profileFrame.dispose();


                try {
                    startGame();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


//                race.start(city.getCurrentPlayer());
            }
        });

        competetive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Race race =  city.races.get(1);
                city.getCurrentPlayer().addRace(race);
                double x1 = race.getWay().getNodes()[0].getX_center();
                double x2 = race.getWay().getNodes()[1].getX_center();
                double y1 = race.getWay().getNodes()[0].getY_center();
                double y2 = race.getWay().getNodes()[1].getY_center();

                city.getCurrentPlayer().getCurrentMachine().setX_center(x1);
                city.getCurrentPlayer().getCurrentMachine().setY_center(y1);

                if (x1 - x2 == 0){

                    city.getCurrentPlayer().getCurrentMachine().setX_center(x1);
                    city.getCurrentPlayer().getCurrentMachine().setY_center(y1 - 3 );
                    // TODO
                    if (y1 > y2){
                        city.getCurrentPlayer().getCurrentMachine().setTeta(-90);
                    }
                    else {

                        city.getCurrentPlayer().getCurrentMachine().setTeta(90);
                    }
                }
                else {
                    city.getCurrentPlayer().getCurrentMachine().setX_center(x1 - 3);
                    city.getCurrentPlayer().getCurrentMachine().setY_center(y1);
                    if (x1 > x2){
                        city.getCurrentPlayer().getCurrentMachine().setTeta(180);
                    }
                    else {
                        city.getCurrentPlayer().getCurrentMachine().setTeta(0);

                    }
                }

                profileFrame.dispose();
                try {
                    startGame();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

//                race.start(city.getCurrentPlayer());
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileFrame.dispose();
                try {
                    startGame();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        buyMachinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileFrame.dispose();
                buyMachineMode();
            }
        });

        editMachine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileFrame.dispose();
                editMachine();
            }
        });

        setCurrentMachine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileFrame.dispose();
                setCurrentMachineMode();
            }
        });



        String temp = "";
        ArrayList<Machine> machines = city.getCurrentPlayer().getMachines();
        if (machines.size() > 0) {
            for (int w = 0; w < machines.size(); w++) {
                if (w < machines.size() - 1) {
                    temp += machines.get(w).getName() + ", ";
                } else {
                    temp += machines.get(w).getName();
                }
            }
        }
        JLabel machinLable = new JLabel("Cars : " + temp);


        temp = "";
        if (city.getCurrentPlayer().getCurrentMachine() != null) {
            temp += city.getCurrentPlayer().getCurrentMachine().getName();
        }
        JLabel currentMachinLable = new JLabel("CurrentCar : " + temp);

        temp = "";
        temp += Integer.toString(city.getCurrentPlayer().getMoney());
        JLabel moneyLable = new JLabel("Money : " + temp);

        temp = "";
        temp += Integer.toString(city.getCurrentPlayer().getPopularity());
        JLabel popularityLable = new JLabel("Popularity : " + temp);

        temp = "";
        ArrayList<Race> races = city.getCurrentPlayer().getRaces();
        for (int w = 0; w < races.size(); w++){
            if (w < races.size() - 1){
                temp += races.get(w).getName() + ">>>" + city.getCurrentPlayer().getFullRecordOfRace(races.get(w)) + ", ";
            }
            else {
                temp += races.get(w).getName() + ">>>" + city.getCurrentPlayer().getFullRecordOfRace(races.get(w));
            }
        }
        JLabel racesLable = new JLabel("Races : " + temp);



        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));

        profilePanel.add(machinLable);
        profilePanel.add(currentMachinLable);
        profilePanel.add(moneyLable);
        profilePanel.add(popularityLable);
        profilePanel.add(racesLable);
        profilePanel.add(startButton);
        profilePanel.add(buyMachinButton);
        profilePanel.add(editMachine);
        profilePanel.add(setCurrentMachine);
        profilePanel.add(training);
        profilePanel.add(competetive);

        profileFrame.getContentPane().add(profilePanel);


        profileFrame.pack();
        profileFrame.setLocationRelativeTo(null);
        profileFrame.setVisible(true);
        profileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



    public void update() throws IOException {
        for (int i = 0; i < Files.readAllLines(Paths.get("players_profile.txt")).size(); i++){
            if (i % 10 == 0){
                updateProfile(i, Files.readAllLines(Paths.get("players_profile.txt")).get(i));
            }
        }
    }

    public void updateProfile(int i , String name){
        for (Player player : City.players){
                        if (name.equals(player.getName())){
                            for (int j = i + 1; j <= i + 6; j++){
                                String toPass = "";
                                if (j == i + 1){
                                    ArrayList<Machine> machines = player.getMachines();
                                    for (int k = 0; k < machines.size(); k++){
                                        if (k < machines.size() - 1){
                                            toPass += machines.get(k).getName() + " ";
                                        }
                                        else {
                                            toPass += machines.get(k).getName();
                                        }
                                    }
                                }
                                else if(j == i + 2){
                                    toPass = player.getCurrentMachine().getName();
                                }
                                else if(j == i + 3){
                                    toPass = Integer.toString(player.getMoney());
                                    System.out.println(player.getMoney());
                                    System.out.println("money");
                                }
                                else if(j == i + 4){
                                    toPass = Integer.toString(player.getPopularity());
                                }
                                else if(j == i + 5){
                                    ArrayList<Race> races = player.getRaces();
                                    for (int k = 0; k < races.size(); k++){
                                        if (k < races.size() - 1){
                                            toPass += races.get(k).getName() + " ";
                                        }
                                        else {
                                            toPass += races.get(k).getName();
                                        }
                                    }
                                }
                                else if (j == i + 6){
                                    ArrayList<Race> races = player.getRaces();
                                    for (int k = 0; k < races.size(); k++){
                                        if (k < races.size() - 1){
                                            toPass += Integer.toString(player.getFullRecordOfRace(races.get(k))) + " ";
                                        }
                                        else {
                                            toPass += Integer.toString(player.getFullRecordOfRace(races.get(k)));
                                        }
                                    }
                                }
                                try {
                                    writeFile(toPass, j);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
    }


    public void editMachine(){
        JFrame editMachineFrame = new JFrame();
        JPanel editMachinePanel = new JPanel();
        JButton buyComponent = new JButton("buyComponent");
        JButton back = new JButton("back");

        String temp = "";
        if (city.getCurrentPlayer().getCurrentMachine() != null) {
            temp = city.getCurrentPlayer().getCurrentMachine().getEngine().getName();
        }
        JLabel engineLable = new JLabel("engine : " + temp);

        if (city.getCurrentPlayer().getCurrentMachine() != null) {
            temp = city.getCurrentPlayer().getCurrentMachine().getWheel().getName();
        }
        JLabel wheelLable = new JLabel("wheel : " + temp);

        if (city.getCurrentPlayer().getCurrentMachine() != null) {
            temp = city.getCurrentPlayer().getCurrentMachine().getBody().getName();
        }
        JLabel bodyLable = new JLabel("body : " + temp);


        buyComponent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMachineFrame.dispose();
                buyComponentForMachine();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editMachineFrame.dispose();
                profileMode();
            }
        });

        editMachinePanel.add(engineLable);
        editMachinePanel.add(wheelLable);
        editMachinePanel.add(bodyLable);
        editMachinePanel.add(buyComponent);
        editMachinePanel.add(back);
        editMachinePanel.setLayout(new BoxLayout(editMachinePanel, BoxLayout.Y_AXIS));

        editMachineFrame.getContentPane().add(editMachinePanel);
        editMachineFrame.setLocationRelativeTo(null);
        editMachineFrame.setVisible(true);
        editMachineFrame.pack();
        editMachineFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    public void buyComponentForMachine(){
        JFrame buyComponentFram = new JFrame();
        JPanel buyComponentPnae = new JPanel();


        JButton buyEngine1 = new JButton("buyEngine1");
        JButton buyEngine2 = new JButton("buyEngine2");
        JButton buyEngine3 = new JButton("buyEngine3");
        JButton buyWheel1 = new JButton("buyWheel1");
        JButton buyWheel2 = new JButton("buyWheel2");
        JButton buyWheel3 = new JButton("buyWheel3");
        JButton buyBody1 = new JButton("buyBody1");
        JButton buyBody2 = new JButton("buyBody2");
        JButton buyBody3 = new JButton("buyBody3");
        JButton back = new JButton("back");

        buyEngine1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Engine engine : City.engines){
                    if (engine.getName().equals("engine1")){
                        if (city.getCurrentPlayer().buyEngineForMachin( city.getCurrentPlayer().getCurrentMachine(), engine)){
                            JOptionPane.showMessageDialog(null ,"done");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "your money not enough");
                        }
                    }
                }
            }
        });

        buyEngine2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Engine engine : City.engines){
                    if (engine.getName().equals("engine2")){
                        if (city.getCurrentPlayer().buyEngineForMachin( city.getCurrentPlayer().getCurrentMachine(), engine)){
                            JOptionPane.showMessageDialog(null ,"done");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "your money not enough");
                        }
                    }
                }
            }
        });

        buyEngine3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Engine engine : City.engines){
                    if (engine.getName().equals("engine3")){
                        if (city.getCurrentPlayer().buyEngineForMachin( city.getCurrentPlayer().getCurrentMachine(), engine)){
                            JOptionPane.showMessageDialog(null ,"done");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "your money not enough");
                        }
                    }
                }
            }
        });

        buyWheel1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Wheel wheel : City.wheels){
                    if (wheel.getName().equals("wheel1")){
                        if (city.getCurrentPlayer().buyWeelForMachine(city.getCurrentPlayer().getCurrentMachine(), wheel)){
                            JOptionPane.showMessageDialog(null ,"done");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "your money not enough");
                        }
                    }
                }
            }
        });

        buyWheel2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Wheel wheel : City.wheels){
                    if (wheel.getName().equals("wheel2")){
                        if (city.getCurrentPlayer().buyWeelForMachine(city.getCurrentPlayer().getCurrentMachine(), wheel)){
                            JOptionPane.showMessageDialog(null ,"done");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "your money not enough");
                        }
                    }
                }
            }
        });

        buyWheel3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Wheel wheel : City.wheels){
                    if (wheel.getName().equals("wheel3")){
                        if (city.getCurrentPlayer().buyWeelForMachine(city.getCurrentPlayer().getCurrentMachine(), wheel)){
                            JOptionPane.showMessageDialog(null ,"done");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "your money not enough");
                        }
                    }
                }
            }
        });

        buyBody1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Body body : City.bodies){
                        if (body.getName().equals("body1")){
                            if (city.getCurrentPlayer().buyBodyForMachine(city.getCurrentPlayer().getCurrentMachine(), body)){
                                JOptionPane.showMessageDialog(null ,"done");
                            }
                            else{
                                JOptionPane.showMessageDialog(null, "your money not enough");
                            }
                        }
                    }
                }

        });

        buyBody2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Body body : City.bodies){
                    if (body.getName().equals("body2")){
                        if (city.getCurrentPlayer().buyBodyForMachine(city.getCurrentPlayer().getCurrentMachine(), body)){
                            JOptionPane.showMessageDialog(null ,"done");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "your money not enough");
                        }
                    }
                }
            }

        });

        buyBody3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Body body : City.bodies){
                    if (body.getName().equals("body3")){
                        if (city.getCurrentPlayer().buyBodyForMachine(city.getCurrentPlayer().getCurrentMachine(), body)){
                            JOptionPane.showMessageDialog(null ,"done");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "your money not enough");
                        }
                    }
                }
            }

        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyComponentFram.dispose();
                editMachine();
            }
        });


        buyComponentPnae.add(buyEngine1);
        buyComponentPnae.add(buyEngine2);
        buyComponentPnae.add(buyEngine3);
        buyComponentPnae.add(buyWheel1);
        buyComponentPnae.add(buyWheel2);
        buyComponentPnae.add(buyWheel3);
        buyComponentPnae.add(buyBody1);
        buyComponentPnae.add(buyBody2);
        buyComponentPnae.add(buyBody3);
        buyComponentPnae.add(back);

        buyComponentFram.add(buyComponentPnae);


        buyComponentFram.setLocationRelativeTo(null);
        buyComponentFram.setVisible(true);
        buyComponentFram.pack();
        buyComponentFram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void setCurrentMachineMode(){
        JFrame currentFrame = new JFrame();
        JPanel currentPanel = new JPanel();
        JButton nextMachin = new JButton("nextCar");
        JButton lastMachin = new JButton("lastCar");
        JButton back = new JButton("back");

        if (city.getCurrentPlayer().getCurrentMachine() == null){
            city.getCurrentPlayer().setCurrentMachine(city.getCurrentPlayer().getMachines().get(0));
        }

        nextMachin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int k = 0; k < city.getCurrentPlayer().getMachines().size(); k++){
                    if (city.getCurrentPlayer().getMachines().get(k).equals(city.getCurrentPlayer().getCurrentMachine())) {
                        if (k < city.getCurrentPlayer().getMachines().size() - 1) {
                            city.getCurrentPlayer().setCurrentMachine(city.getCurrentPlayer().getMachines().get(k + 1));
                            JOptionPane.showMessageDialog(null, "done");
                        } else {
                            JOptionPane.showMessageDialog(null, "invalid");
                        }
                        break;
                    }

                }

            }
        });

        lastMachin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int k = 0; k < city.getCurrentPlayer().getMachines().size(); k++){
                    if (city.getCurrentPlayer().getMachines().get(k).equals(city.getCurrentPlayer().getCurrentMachine())) {
                        if (k > 0) {
                            city.getCurrentPlayer().setCurrentMachine(city.getCurrentPlayer().getMachines().get(k - 1));
                            JOptionPane.showMessageDialog(null, "done");
                        } else {
                            JOptionPane.showMessageDialog(null, "invalid");
                        }
                        break;
                    }

                }

            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame.dispose();
                try {
                    update();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                profileMode();
            }
        });

        currentPanel.add(nextMachin);
        currentPanel.add(lastMachin);
        currentPanel.add(back);
        currentFrame.getContentPane().add(currentPanel);
        currentFrame.setLocationRelativeTo(null);
        currentFrame.setVisible(true);
        currentFrame.pack();
        currentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
