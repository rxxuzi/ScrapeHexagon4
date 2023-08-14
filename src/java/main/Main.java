package main;

import fast.Log;
import global.GlobalProperties;

import javax.swing.*;

public class Main {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 750;
    public static final long st = System.currentTimeMillis();

    public static void main(String[] args) {
        Core core = new Core();
        Log.length = Log.getLogNum();
        GlobalProperties gp = new GlobalProperties();
        gp.makeDir();

        JFrame jf = new JFrame();
        ImageIcon icon = new ImageIcon("./data/images/hexagon.png");

        jf.setTitle("Scrape Hexagon");
        jf.setSize(WIDTH, HEIGHT);
        jf.setLocation(0, 0);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setVisible(true);
        //protected frame size
        jf.setResizable(false);
        //set icon from ./data/icon.png
        jf.setIconImage(icon.getImage());

        jf.add(core);

    }
}