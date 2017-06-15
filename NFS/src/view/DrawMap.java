package view;

import controller.GameController;
import model.City;
import model.Machine;
import model.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sina on 1/31/2017.
 */
public class DrawMap extends JComponent {
    private BufferedImage img1;
    private BufferedImage img2;
    private BufferedImage img3;
    private BufferedImage img4;
    private BufferedImage img5;
    private BufferedImage img6;
    BufferedImage road;
    BufferedImage left;
    BufferedImage bottom;
    BufferedImage grass;
    BufferedImage wall;
    BufferedImage water;
    BufferedImage car;
    private int WIDTH;
    private int HEIGHT;
    private int map[][];
    private City city;
    private DrawProfile drawProfile;



    private GameController gameController;

    public void init(GameController controller, City city , DrawProfile drawProfile) throws IOException {

        this.drawProfile = drawProfile;
        this.city = city;
        File file1 = new File("road_left.jpg");
        File file2 = new File("wall.jpg");
        File file3 = new File("grass.jpg");
        File file4 = new File("bottom.jpg");
        File file5 = new File("water.jpg");
        File file6 = new File("car.png");
        img1 = ImageIO.read(file1);
        img2 = ImageIO.read(file2);
        img3 = ImageIO.read(file3);
        img4 = ImageIO.read(file4);
        img5 = ImageIO.read(file5);
        img6 = ImageIO.read(file6);
        road = img1.getSubimage(6 * 20, 6 * 20 , 20, 20);
        left = img1.getSubimage(1 * 20, 0 * 20, 20, 20);
        wall = img2.getSubimage(12 * 20, 0 * 20, 20, 20);
        grass = img3.getSubimage(0 * 20, 0 * 20, 20, 20);
        bottom = img4.getSubimage(0 * 20, 0 * 20, 20, 20);
        water = img5.getSubimage(16 * 20, 1 * 20, 20, 20);

        BufferedReader tileMap = new BufferedReader(new FileReader(new File("map.txt")));
        this.WIDTH = Integer.parseInt(tileMap.readLine());
        this.HEIGHT = Integer.parseInt(tileMap.readLine());

        map = new int[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++){
            String[] line  = tileMap.readLine().split(",");
            for (int j = 0; j < HEIGHT; j++){
                map[j][i] = Integer.parseInt(line[j]);
            }
        }
        ///////////////////////////////////////////////////////////////////////
        this.gameController = controller;
        addKeyListener(gameController);
        addMouseListener(gameController);
        setFocusable(true);
        requestFocus();
        setLayout(null);
        //setPreferredSize(new Dimension(WIDTH, HEIGHT));
        requestFocus();
        addNotify();
    }
    @Override
    public void paintComponent(Graphics g){
        for (int i = 0; i < WIDTH; i++){
            for (int j = 0; j < HEIGHT; j++){
                if (map[i][j] == 2785){
                    g.drawImage(road, 20 * i, 20 * j, null);
                }
                else if (map[i][j] == 2732){
                    g.drawImage(left, 20 * i, 20 * j, null);
                }
                else if (map[i][j] == 2512){
                    g.drawImage(wall, 20 * i, 20 * j, null);
                }
                else if (map[i][j] == 1){
                    g.drawImage(grass, 20 * i, 20 * j, null);
                }
                else if (map[i][j] == 2787){
                    g.drawImage(bottom, 20 * i, 20 * j, null);
                }
                else if (map[i][j] == 2652){
                    g.drawImage(water, 20 * i, 20 * j, null);
                }

            }
        }


        ///////////////////////////////////////////////////////////
        for (Player player : City.players) {
                Machine machine = player.getCurrentMachine();
                g.setColor(Color.BLACK);
                g.fillOval((int) machine.getX_center() - (machine.getLength() / 2), (int) machine.getY_center() - (machine.getWidth() / 2),
                        machine.getLength(), machine.getWidth());
                g.setColor(Color.YELLOW);

                float r = (float) (machine.getWidth() / 2);
                g.fillOval((int) machine.getX_center() + (int) (r * Math.cos(machine.getTeta() * Math.PI / 180)), (int) machine.getY_center() + (int) (r * Math.sin(machine.getTeta() * Math.PI / 180)),
                        2, 2);
//            double rotationRequired = Math.toRadians (machine.getTeta());
//            double locationX = img6.getWidth() / 2;
//            double locationY = img6.getHeight() / 2;
//            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
//            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
//
//// Drawing the rotated image at the required drawing locations
//            g.drawImage(op.filter(img6,null), (int)machine.getX_center(), (int)machine.getY_center(), null);


        }
        g.setColor(Color.WHITE);
        g.drawLine(80, 420, 480, 420);

        // SAVE
        g.setColor(Color.lightGray);
        g.fillRect(0 , 600, 100, 64);
        g.setColor(Color.RED);
        g.setFont(new Font("Calibri", Font.PLAIN, 24));
        g.drawString("SAVE" , 25, 634);

        // SPEED
        g.setColor(Color.BLACK);
        g.fillRect(100 , 600, 100, 64);
        g.setColor(Color.RED);
        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        String speed = Integer.toString((int) city.getCurrentPlayer().getCurrentMachine().getSpeed());
        g.drawString("SPEED: " + speed , 125, 634);

        // money
        g.setColor(Color.BLACK);
        g.fillRect(200 , 600, 100, 64);
        g.setColor(Color.RED);
        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        String money = Integer.toString(city.getCurrentPlayer().getMoney());
        g.drawString("MONEY: " + money , 225, 634);

        //popularity
        g.setColor(Color.BLACK);
        g.fillRect(300 , 600, 100, 64);
        g.setColor(Color.RED);
        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        String popularity = Integer.toString((int) city.getCurrentPlayer().getPopularity());
        g.drawString("POPULARITY: " + popularity , 320, 634);

        // body power
        g.setColor(Color.BLACK);
        g.fillRect(400 , 600, 100, 64);
        g.setColor(Color.RED);
        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        String bodyPower = Integer.toString((int) city.getCurrentPlayer().getCurrentMachine().getBody().getBody_power());
        g.drawString("POWER: " + bodyPower , 420, 634);

        //TIMER

        g.setColor(Color.BLACK);
        g.fillRect(500 , 600, 100, 64);
        g.setColor(Color.RED);
        g.setFont(new Font("Calibri", Font.PLAIN, 12));
        String second = null;
        String minute = null;
        if (GameController.TIMER_MINUTE < 10){
            minute = "0" + Integer.toString(GameController.TIMER_MINUTE);
            if (GameController.TIMER_SECOND < 10){
                second = "0"+ Integer.toString(GameController.TIMER_SECOND);
            }
            else{
                second = Integer.toString(GameController.TIMER_SECOND);
            }
        }
        else {
            minute = Integer.toString(GameController.TIMER_MINUTE);
            if (GameController.TIMER_SECOND < 10){
                second = "0"+ Integer.toString(GameController.TIMER_SECOND);
            }
            else{
                second = Integer.toString(GameController.TIMER_SECOND);
            }
        }

        String timer = minute + ":" + second;
        g.drawString("TIME: " + timer , 520, 634);
    }


//            System.out.println("Done");
}
