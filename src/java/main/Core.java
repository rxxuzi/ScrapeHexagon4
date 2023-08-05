package main;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import crawler.OpenSRC;
import data.Json;
import data.Predict;
import fast.*;
import data.*;
import data.ReadFromJson;
import global.GlobalProperties;
import jdk.jfr.BooleanFlag;

public class Core extends JPanel {
    Font font;
    //検索ワードを設定
    public static String word;
    public static int imgCount = 0;

    final int corner = 6;
    static boolean isRunning = true;
    public static double dt = 0.01;
    private long time = 0;
    public static boolean canMove = false;
    private final int typeOfJson = 4;
    @BooleanFlag
    boolean isFormed1 = false, isFormed2= false;

    JButton[] btn = new JButton[10];
    JCheckBox[] cb = new JCheckBox[3];
    JTextField[] tf = new JTextField[2];
    JLabel[] lb = new JLabel[10];
    JLabel mainLabel;
    Listener ml = new Listener();
    Random random = new Random();
    Draw draw ;

    Core(){


        draw = new Draw(getWidth(),getHeight());
        for(int i = 0 ; i < lb.length ; i ++ ){
            lb[i] = new JLabel();
        }

        int x = 10; int y = 10;
        int w = 200;
        int h = 30;
        this.setBackground(Color.BLACK);
        this.setLayout(null);
        this.addMouseListener(ml);
        this.addMouseMotionListener(ml);

        mainLabel = new JLabel();
        mainLabel.setBounds(Main.WIDTH/2 - 150,10, 300 ,200);

        // GET BUTTON
        JButton mainBtn = new JButton("GET");
        mainBtn.setBounds(Main.WIDTH/2 - 75,Main.HEIGHT - 150, 160, 40);
        mainBtn.addActionListener(e -> {
            if(isFormed1 && isFormed2){
                mainLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
                System.out.println(word);
                GlobalProperties.Compare(imgCount);
                OpenSRC open = new OpenSRC();
                open.setTag(word);
                open.run();
            }else if(!isFormed1 && isFormed2){
                mainLabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
                mainLabel.setForeground(Color.red);
                mainLabel.setText("Please input a word.");
            } else if(isFormed1){
                mainLabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
                mainLabel.setForeground(Color.red);
                mainLabel.setText("Please input a number.");
            }else {
                mainLabel.setFont(new Font("SansSerif", Font.PLAIN, 17));
                mainLabel.setForeground(Color.red);
                mainLabel.setText("Please input a word and a number.");
            }
        });

        mainBtn.setFont(new Font("DIALOG",Font.BOLD , 30));
        mainBtn.setForeground(Color.magenta);
        mainBtn.setOpaque(false);
        mainBtn.setBackground(Color.BLACK);
        mainBtn.setBorderPainted(false);
        mainBtn.setFocusable(false);
        mainBtn.setFocusPainted(false);

        tf[0] = new JTextField();
        tf[0].setBounds(x,y, w ,h);
        tf[0].setBackground(new Color(10,10,10,100));
        tf[0].setForeground(Color.GREEN);
        tf[0].setOpaque(false);
        lb[0].setText("Get Picture About : ");
        lb[0].setForeground(Color.GREEN);
        lb[0].setBounds(x,y + h, 400 ,h);
        btn[0] = new JButton();
        btn[0].setBounds(x + w , y, h, h);
        btn[0].setBackground(Color.BLACK);
        btn[0].addActionListener(e -> {
            word = tf[0].getText();
            System.out.println(word);
            lb[0].setText("Get Picture About : " + word);
            if(!Objects.equals(word, "")){
                isFormed1 = true;
            }
            mainLabel.setText("");
        });

        y = y + h * 2;
        w = 100;
        tf[1] = new JTextField();
        tf[1].setBounds(x,y, w ,h);
        tf[1].setBackground(new Color(10,10,10,100));
        tf[1].setForeground(Color.GREEN);
        tf[1].setOpaque(false);
        lb[1].setText("Get Picture Count : ");
        lb[1].setForeground(Color.GREEN);
        lb[1].setBounds(x,y + h, 300 ,h);
        ((AbstractDocument) tf[1].getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                fb.insertString(offset, string.replaceAll("[^0-9]", ""), attr);
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text,
                                AttributeSet attrs) throws BadLocationException {
                fb.replace(offset, length, text.replaceAll("[^0-9]", ""), attrs);
            }
        });
        btn[1] = new JButton();
        btn[1].setBounds(x + w , y, h, h);
        btn[1].setBackground(Color.BLACK);
        btn[1].addActionListener(e -> {
            try {
                if(!Objects.equals(tf[1].getText(), "")){
                    isFormed2 = true;
                }
                imgCount = Integer.parseInt(tf[1].getText());
                System.out.println(imgCount);
                //set maxImage
                lb[1].setText("Get Picture Count : " + imgCount);
                mainLabel.setText("");
            }catch (NumberFormatException exception){
                tf[1].setText(random.nextInt(10) + 5 + "");
                lb[1].setText("Get Picture Count : Random");
            }
        });

        y += h*2;
        lb[3].setText("Explicit content");
        lb[3].setBounds(x,y, 100 ,h);
        lb[3].setForeground(Color.GREEN);
        lb[4].setBounds(100 + h, y, 100 ,h);
        lb[4].setForeground(Color.yellow);

        cb[0] = new JCheckBox();
        cb[0].setBounds(100 , y ,h,h);
        cb[0].setOpaque(false);
        //check box is
        cb[0].addActionListener(e -> {
            if(cb[0].isSelected()){
                lb[4].setText("Blocked");
            }else {
                lb[4].setText("");
            }
        });

        //video
        y += 20;
        lb[5].setText("Save Video");
        cb[1] = new JCheckBox();

        lb[5].setBounds(x,y, 100 ,h);
        lb[5].setForeground(Color.GREEN);

        lb[6].setBounds(100 + h, y, 200 ,h);
        lb[6].setForeground(Color.yellow);

        cb[1].setBounds(100 , y ,h,h);
        cb[1].setOpaque(false);
        cb[1].addActionListener(e -> {
            if(cb[1].isSelected()){

                lb[6].setText("Save the video");
            }else {
                lb[6].setText("Not Save the video");
            }
        });

        y += 20;

        //save High Quality
        cb[2] = new JCheckBox();
        lb[8].setText("High Quality");
        lb[9] = new JLabel();
        lb[8].setBounds(x,y, 100 ,h);
        lb[9].setBounds(100 + h, y, 200 ,h);
        lb[8].setForeground(Color.GREEN);
        lb[9].setForeground(Color.yellow);
        cb[2].setBounds(100 , y ,h,h);
        cb[2].setOpaque(false);
        cb[2].addActionListener(e -> {
            if(cb[2].isSelected()){
                lb[9].setText("Save hq");
            }else {
                lb[9].setText("");
            }
        });



        font = new Font("Sanserif" , Font.BOLD , 10);
        y += h;

        /*
          Delete Button
          Pushed -> Delete all files generated in ./resources/Log and ./resources/Pic
         */
        btn[2] = new JButton();
        btn[2].setFont(font);
        btn[2].setBounds(x,y,w,h);
        btn[2].setText("Delete Pic");
        btn[2].setForeground(Color.magenta);
        btn[2].setOpaque(false);
        btn[2].setFocusPainted(false);
        btn[2].setBackground(Color.BLACK);
        btn[2].addActionListener(e -> {
            Del.allPicDelete();
            Del.allLogDelete();
            Log.length = 0;

            mainLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
            mainLabel.setForeground(Color.yellow);
            mainLabel.setText("All Deleted.");
        });

        y += h;
        btn[3] = new JButton("Random Post");
        btn[3].setFont(font);
        btn[3].setBounds(x,y,w,h);
        btn[3].setText("Random Post");
        btn[3].setForeground(Color.magenta);
        btn[3].setOpaque(false);
        btn[3].setFocusPainted(false);
        btn[3].setBackground(Color.BLACK);
        btn[3].addActionListener(e -> {
            int r = random.nextInt(typeOfJson);
            tf[0].setText(Json.getRandomTag());
        });

        y += h;
        lb[2].setBounds(x+w,y,900,h);
        lb[2].setForeground(Color.yellow);
        lb[2].setFont(font);
        btn[4] = new JButton("Search");
        btn[4].setFont(font);
        btn[4].setBounds(x,y,w,h);
        btn[4].setForeground(Color.magenta);
        btn[4].setOpaque(false);
        btn[4].setFocusPainted(false);
        btn[4].setBackground(Color.BLACK);
        btn[4].addActionListener(e -> {
            String sw = tf[0].getText();
            WordMatcher wm = new WordMatcher();
            wm.text = sw;

            Json json = new Json();
            if(!sw.equals("")){
                if(Json.isExist(Tag.reverse(sw))){
                    json.search(Tag.reverse(sw));
                    lb[2].setText("Found , id : " + json.id + " , (" + json.category + ")");
                }else{
                    lb[2].setText("Predict : " + wm.AutoComplete());
                }
            }else{
                lb[2].setText("Please input a word.");
            }
        });

        y+=h;
        lb[7].setBounds(x+w,y,900,h);
        lb[7].setForeground(Color.yellow);
        lb[7].setFont(font);
        btn[5] = new JButton("Predict words");
        btn[5].setFont(font);
        btn[5].setBounds(x,y,w,h);
        btn[5].setForeground(Color.magenta);
        btn[5].setOpaque(false);
        btn[5].setFocusPainted(false);
        btn[5].setBackground(Color.BLACK);
        btn[5].addActionListener(e -> {
            //open directory ./resources/pic
            Predict predict = new Predict(tf[0].getText());
            lb[7].setText(predict.text);
        });


        y += h;
        btn[6] = new JButton("Check Pic Dir");
        btn[6].setFont(font);
        btn[6].setBounds(x,y,w,h);
        btn[6].setForeground(Color.magenta);
        btn[6].setOpaque(false);
        btn[6].setFocusPainted(false);
        btn[6].setBackground(Color.BLACK);
        btn[6].addActionListener(e -> {
            //open directory ./resources/pic
            try {
                Desktop.getDesktop().open(new File("./output/pics"));
            } catch (IOException ex) {
                Log.error(ex.getMessage());
            }
        });

        this.add(tf[0]);
        this.add(lb[0]);
        this.add(btn[0]);
        this.add(tf[1]);
        this.add(lb[1]);
        this.add(btn[1]);
        this.add(lb[3]);
        this.add(btn[2]);
        this.add(btn[3]);
        this.add(lb[2]);
        this.add(btn[4]);
        this.add(lb[5]);
        this.add(lb[6]);
        this.add(lb[4]);
        this.add(btn[5]);
        this.add(btn[6]);
        this.add(lb[7]);
        this.add(lb[8]);
        this.add(lb[9]);
        for(JCheckBox c : cb){
            this.add(c);
        }
        this.add(mainBtn);
        this.add(mainLabel);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    String name = "word";

    private void draw(Graphics g){
        g.setColor(Color.white);

        draw.setW_H(getWidth(),getHeight());

        draw.progressBar(g, 0,getHeight() - 20 , getWidth() , 20);


        int rad = 100;
        for (int i = 0 ; i < 6 ; i ++){
            double x = rad * Math.cos(i * Math.PI / 3 );
            double y = rad * Math.sin(i * Math.PI / 3 );
            draw.hexagon(g, x, y , i);
        }
        draw.hexagon(g,0,0,-1);

        g.setColor(Color.red);


        g.setColor(Color.GREEN);

        int x = 650;
        int y = 20;
        int dx ;
        int dy ;
        if(canMove){
            dx = ml.fx - ml.x;
            dy = ml.fy - ml.y;
            x -= dx;
            y  -= dy;

            if(time % 24 < 8){
                g.setColor(new Color(100, 255, 255));
            } else if (time % 24 < 16){
                g.setColor(new Color(255, 255, 100));
            }else{
                g.setColor(new Color(255, 100, 255));
            }
            draw.title(g,x ,y);
            g.drawString("debug" , 5,5);
        }else{
            g.setColor(Color.GREEN);
            draw.title(g,x ,y);
            rainbowColor(g);
            g.drawString("elapsed time" + (System.currentTimeMillis()/100 - Main.st/100), 5,5);
        }
        sleep();
    }

    private void rainbowColor(Graphics g){
        switch ((int) (time%12)) {
            case 0 -> g.setColor(new Color(255, 0, 0));
            case 1 -> g.setColor(new Color(255, 100, 0));
            case 2 -> g.setColor(new Color(255, 255, 0));
            case 3 -> g.setColor(new Color(100, 255, 0));
            case 4 -> g.setColor(new Color(0, 255, 0));
            case 5 -> g.setColor(new Color(0, 255, 100));
            case 6 -> g.setColor(new Color(0, 255, 255));
            case 7 -> g.setColor(new Color(0, 100, 255));
            case 8 -> g.setColor(new Color(0, 0, 255));
            case 9 -> g.setColor(new Color(100, 0, 255));
            case 10 -> g.setColor(new Color(255, 0, 255));
            case 11 -> g.setColor(new Color(255, 0, 100));
            default -> g.setColor(new Color(100, 100, 100));
        }
    }

    private void sleep(){
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if(isRunning) {
            dt += 0.04;
            time ++;
        }
        repaint();
    }
}
