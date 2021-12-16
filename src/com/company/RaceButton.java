package com.company;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * class for button component as race
 */
public class RaceButton extends Thread implements BackRedrawer{

    /**
     * button getter
     * @return button
     */
    public JButton getButton() { return button; }

    /**
     * set finish coord of oX
     * @param finish coordinate on oX
     */
    public void setFinish(int finish) { this.finish = finish; }

    /**
     * set redrawer for delegate-pattern in method run
     * @param redrawer
     */
    public void setRedrawer(BackRedrawer redrawer) { this.redrawer = redrawer; }

    /**
     * increment speed of race
     */
    public void incSpeed() { speed++; }

    /**
     * getter of wait-flag for all races
     * @return
     */
    public static boolean isWait() { return wait; }

    /**
     * invert wait-flag for all races
     */
    public static void invertWait(){ wait = !wait;}

    /**
     * invert run-flag for current race
     */
    public void invertRun(){ isRun = !isRun;}

    public static void setHardReset() {hardReset = true;}
    public static void dropHardReset() {hardReset = false;}

    public static boolean hardReset = false;
    private static boolean wait = true;
    private static double speedCoef = 0.1;
    private int finish;
    private BackRedrawer redrawer;

    private int speed;
    public boolean isRun;
    private JButton button;


    public RaceButton(JButton button){
        super();
        this.button = button;
        speed = (new Random()).nextInt(3)+3;
        isRun = true;
    }

    //move-step for race
    private void step() {
        speedCoef = (speedCoef > 1) ? 0.1 : speedCoef + 0.1;
        button.setBounds(button.getBounds().x + speed * (int) Math.round(speedCoef), button.getBounds().y, button.getBounds().width, button.getBounds().height);
    }

    /**
     * run of thread; call method redrawBack from BackRedrawer redrawer
     */
    @Override
    public void run() {
        try {
            while(button.getBounds().x + button.getWidth() < finish && !hardReset){
                if(!wait && isRun){
                    step();
                }
                sleep(50);
            }
            if(!hardReset) {
                button.setVisible(false);
                new WinFrame(this);
                redrawBack(button.getBackground());

            }wait = true;
        } catch (InterruptedException e) {
            return;
        }
    }

    /**
     * method of delegate-pattern that call in run
     * @param color
     */
    @Override
    public void redrawBack(Color color) {
        redrawer.redrawBack(color);
    }
}