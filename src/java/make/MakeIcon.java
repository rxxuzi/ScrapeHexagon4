package make;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

public class MakeIcon {
    private static final int width = 1024; //width of image
    private static final int height = 1024; //height of image
    public static void main(String[] args) {

        //save draw hexagon image
        File file = new File("./data/images/hexagon.png");
        var img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        var g = img.createGraphics();

        g.setColor(Color.CYAN);
        int corners = 6;
        int radius = 500;
        drawHexagon(g, radius);
        g.setColor(Color.BLACK);

        drawHexagon(g, radius-50);
        g.dispose();
        try {
            ImageIO.write(img, "png", file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Save icon image successfully");
    }

    private static void drawHexagon(Graphics g, int radius) {
        var corners = 6;
        int[] xPoints = new int[corners];
        int[] yPoints = new int[corners];
        for (int i = 0; i < corners; i++) {
            xPoints[i] = (int) (width/2 + radius * Math.cos(2 * Math.PI * i / corners));
            yPoints[i] = (int) (height/2 + radius * Math.sin(2 * Math.PI * i / corners));
        }
        g.fillPolygon(xPoints, yPoints, corners);
    }

}
