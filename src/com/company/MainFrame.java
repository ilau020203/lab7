package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MainFrame {
    //frame
    private static JFrame frame;
    public static JFrame getFrame() {
        return frame;
    }

    //menu frame content
    private static JTextField raceAmount;
    private static JPanel buttonsPanel;
    private static JButton[] raceStart;
    private static JPanel contentsPanel;

    //race frame content
    private static ArrayList<RaceButton> racesThreads;
    private static ArrayList<JButton> racesButtons;
    private static JButton RunButton = new JButton();
    private static JButton ResetButton = new JButton();
    private static JPanel panel;
    public static JPanel getPanel() {
        return panel;
    }
    public static JButton getRunButton() {
        return RunButton;
    }

    //stats
    private static int raceWidth = 100;
    private static int raceHeight = 30;
    private static Color[] colors = new Color[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.MAGENTA};
    private static String[] colorsName = new String[]{"red","blue","green","yellow","magenta"};
    public static final int frameWidth = 1000;
    public static final int indent = 150;
    private static int racesAmount;


    //  initialization
    private static void initMainFrame(){
        /*Thread mainThread = new Thread();
        mainThread.start();*/
        WinFrame.newRace();
        frame = new JFrame("Гонка");

        raceAmount = new JTextField("Сколько кнопок в гонке?");
        raceAmount.setFont(new Font("Arial", Font.PLAIN, 20));
        raceAmount.setEditable(false);

        buttonsPanel = new JPanel(new GridLayout(2,2));
        raceStart = new JButton[4];
        for (int i = 0; i < raceStart.length; i++){
            raceStart[i] = new JButton(i+2+"");
            buttonsPanel.add(raceStart[i]);
        }

        racesThreads = new ArrayList<>();
        racesButtons = new ArrayList<>();
        RunButton = new JButton("Run");
        ResetButton = new JButton("Reset");
        for(int i = 0; i < 5; i++){
            JButton tempRace = new JButton(colorsName[i]);
            tempRace.setBounds(10,(raceHeight+5)*(i+1),raceWidth,raceHeight);
            tempRace.setBackground(colors[i]);
            racesButtons.add(tempRace);
        }

        contentsPanel = new JPanel(new GridLayout(2,1));
        contentsPanel.add(raceAmount);
        contentsPanel.add(buttonsPanel);

        frame.setContentPane(contentsPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocation(200,200);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    }

    //second frame that call when in init frame click raceStart-button
    public static void initRaceFrame(int raceAmount){
        racesAmount = raceAmount;
        frame.setSize(frameWidth,raceAmount*raceHeight + 5*(raceAmount-1) + 3*(raceHeight+5));

        panel = new JPanel();
        panel.setLayout(null);

        for(int i = 0; i < raceAmount; i++){
            panel.add(racesButtons.get(i));
        }

        RunButton.setBounds(870,frame.getHeight()/4,100,frame.getHeight()/5);
        RunButton.setVisible(true);
        panel.add(RunButton);
        ResetButton.setBounds(870,frame.getHeight()/2,100,frame.getHeight()/5);
        ResetButton.setVisible(true);
        panel.add(ResetButton);

        panel.add(new Finish());

        frame.setContentPane(panel);
        frame.setLocation(200,200);
    }

    //threads initialization
    private static void initThreads(){
        try{
            for(int i = 0; i < racesAmount; i++){
                RaceButton tmp = new RaceButton(racesButtons.get(i));
                tmp.setFinish(frameWidth - indent);
                tmp.setRedrawer(new Redrawer());
                racesThreads.add(tmp);
                racesThreads.get(i).start();
            }
        }
        catch (Exception e){        }
    }

    /**
     * listeners for buttons
     */
    public static void runAndListen(){

        for (JButton button : raceStart){
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    initRaceFrame(Integer.parseInt(button.getText()));
                }
            });
        }

        RunButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RaceButton.invertWait();
                if(RaceButton.isWait()){
                    RunButton.setText("Continue");
                }
                else{
                    RunButton.setText("Stop");
                }
                RaceButton.dropHardReset();
                initThreads();
            }
        });

        ResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //new WinFrame(races.get(1));
                RaceButton.setHardReset();
                for(RaceButton race : racesThreads){
                    race.getButton().getBounds().x = frameWidth;
                }
                frame.setVisible(false);
                WinFrame.resetWinPlace();
                launch();
            }
        });

        for(int i = 0; i < racesButtons.size(); i++){
            int finalI = i;
            racesButtons.get(i).addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    racesThreads.get(finalI).invertRun();
                    racesThreads.get(finalI).incSpeed();
                }
            });
        }
    }

    /**
     * start of all program
     */
    public static void launch(){
        MainFrame.initMainFrame();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame.runAndListen();
            }
        });
    }
}

//draw finish-line
class Finish extends JComponent{

    public Finish(){
        super();
        setBounds(MainFrame.frameWidth-MainFrame.indent,0,MainFrame.frameWidth-MainFrame.indent, MainFrame.getFrame().getHeight());
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.black);
        g2.drawLine(0, 0, 0, MainFrame.getFrame().getHeight());
    }
}

/**
 * class with method that will do in RaceButton (delegate-pattern)
 */
class Redrawer implements BackRedrawer{
    @Override
    public void redrawBack(Color color) {
        MainFrame.getPanel().setBackground(color);
        MainFrame.getRunButton().setText("Continue");
    }
}