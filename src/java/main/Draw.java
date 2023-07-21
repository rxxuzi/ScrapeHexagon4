package main;

import java.awt.*;

public class Draw {
    public void hexagon(Graphics g , double dx , double dy, int w , int h){
        int corner = 6;
        double dt = CoreX.dt;

        g.setColor(Color.WHITE);
        int[] x = new int[corner];
        int[] y = new int[corner];

        for(int i = 0; i < corner; i++){
            double sita = 2 * Math.PI;
            x[i] = (int) (100 * Math.cos(i * sita / corner + dt) + dx + w/2);
            y[i] = (int) (100 * Math.sin(i * sita / corner + dt) + dy + h/2);
        }
        g.drawPolygon(x, y, corner);
    }
}
