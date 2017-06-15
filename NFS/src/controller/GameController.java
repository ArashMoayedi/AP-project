package controller;

import model.City;
import model.Machine;
import model.Player;
import sun.awt.RequestFocusController;
import view.DrawMap;
import view.DrawProfile;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sina on 2/1/2017.
 */
public class GameController implements KeyListener , MouseListener{
    public static final int FPS = 30;

    private Set<Integer> pressed = new HashSet<>();

    Thread gameloop;
    Thread thread;
    Thread multiKey;
    Thread time;

    public static int TIMER_SECOND = 0;
    public static int TIMER_MINUTE = 0;

    boolean running;

    private Machine machine;
    private DrawMap drawMap;
    private DrawProfile drawProfile;

    public void init(Machine machine, DrawMap drawMap, DrawProfile drawProfile){
        this.drawMap = drawMap;
        this.machine = machine;
        this.drawProfile = drawProfile;
        running = true;
    }

    public void time(){
        time = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){

                    if(TIMER_SECOND < 59){

                        TIMER_SECOND += 1;
                    }
                    else{
                        TIMER_MINUTE += 1;
                        TIMER_SECOND = 0;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // Do nothing
                    }
                }


            }



        });

        time.start();
    }

    public void start() {
        gameloop = new Thread(new Runnable() {

            @Override
            public void run() {
                while (running){
                    gameUpdate();
                    gameRender();

                }
                try {
                    Thread.sleep(1000 / FPS);
                } catch (InterruptedException e) {
                    // Do nothing
                }

            }



        });

        gameloop.start();
    }

    private void gameUpdate() {
        try {
//            for (Player player: City.players){
                machine.setLimitPoints();
                machine.update();
//                drawProfile.update();
//            }
        } catch (Exception e) {
        }
    }

    private void gameRender() {
        drawMap.repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }



    @Override
    public void keyPressed(KeyEvent e) {
        pressed.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed.remove(e.getKeyCode());
        start1();
    }



    public void start1() {
        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (machine.getSpeed()> 0.9 && !pressed.contains(KeyEvent.VK_UP) && !pressed.contains(KeyEvent.VK_DOWN)){
                    machine.defaultMode();
                    try {
                        Thread.sleep(1000 / FPS);
                    } catch (InterruptedException e) {
                        // Do nothing
                    }
                }



            }
        });

        thread.start();
    }

    public void startMultiKey() {
        multiKey = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    if (pressed.size() >= 1){
                        for (int i : pressed){
                            if (i == KeyEvent.VK_LEFT){machine.rotateCounterClockWise();}
                            if (i == KeyEvent.VK_RIGHT){machine.rotateClockWise();}
                            if (i == KeyEvent.VK_UP){machine.run();}
                            if (i == KeyEvent.VK_DOWN){machine.backRun();}
                            if (i == KeyEvent.VK_SPACE){machine.brakeMode();}
                            if (i == KeyEvent.VK_ESCAPE){
                                if (running){running = false;}
                                else {running = true;}
                            }
                        }
                    }
                    try {
                        Thread.sleep(500 / FPS);
                    } catch (InterruptedException e) {
                        // Do nothing
                    }
                }



            }
        });

        multiKey.start();
    }



    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();



        if (e.getButton() == 1){
            if (x > 0 && x < 100 && y > 600 && y < 664){
                try {

                    drawProfile.update();
                    System.out.println("done");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
