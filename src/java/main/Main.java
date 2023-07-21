package main;

import driver.Driver;
import fast.Del;
import fast.Log;

import javax.swing.*;

public class Main {
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 750;
    public static final long st = System.currentTimeMillis();

    public static void main(String[] args) {
        Core core = new Core();
        Log.l = Log.getLogNum();
        Driver.setTotalImage(Del.numberOfPic());

        JFrame jf = new JFrame();
        ImageIcon icon = new ImageIcon("./data/images/hexagon.png");

        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            System.out.println("FUCK MAC OS");
        } else {
            System.out.println("Hello World!");
        }


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