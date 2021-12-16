package com.company;
import javax.swing.*;
import java.awt.*;

/**
 * frame of winner
 */
public class WinFrame extends JFrame {
    private static int winPlace = 1;
    private static int raceNum = 0;
    public static void resetWinPlace(){ winPlace = 1;}
    public static void newRace(){ raceNum++;}

    public WinFrame(RaceButton winner){
        super(raceNum+"."+winPlace+"");
        setBackground(winner.getButton().getBackground());

        JTextField msg = new JTextField(winner.getButton().getText() + " занял " + winPlace + " место");
        msg.setEditable(false);

        winPlace++;

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(msg);
        panel.setBackground(winner.getButton().getBackground());
        setContentPane(panel);

        pack();
        setResizable(false);
        setLocation(150*winPlace, 150);
        setVisible(true);
    }
}