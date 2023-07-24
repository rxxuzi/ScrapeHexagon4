package main;


import java.awt.*;

public class Draw {
    int w ;
    int h ;
    private final int corner = 6;

    Draw(int w, int h) {
        this.w = w; this.h = h;
    }

    public void hexagon(Graphics g, double dx, double dy, int type) {
        switch (type % 3) {
            case 0 -> g.setColor(new Color(255, 255, 100));
            case 1 -> g.setColor(new Color(100, 255, 255));
            case 2 -> g.setColor(new Color(255, 100, 255));
            default -> g.setColor(new Color(255, 255, 255));
        }

        int[] x = new int[corner];
        int[] y = new int[corner];

        for(int i = 0; i < corner; i++){

            double sita = 2 * Math.PI;
            x[i] = (int) (100 * Math.cos(i * sita / corner + Core.dt) + dx + w/2);
            y[i] = (int) (100 * Math.sin(i * sita / corner + Core.dt) + dy + h/2);
        }
        g.drawPolygon(x, y, corner);
    }

    public void title(Graphics g, int nx ,int ny){
        int size = 10;
        g.setFont(new Font("Courier New", Font.PLAIN, size));
        for (int i = 0; i < titleLine.length; i++) {
            g.drawString(titleLine[i], nx, ny + i * size);
        }


    }

    public void progressBar(Graphics g , int x , int y, int dx , int dy){
        g.setColor(new Color(255, 255, 255));

//        g.fillRect(x, y , (int)(dx*(Driver.progress/100)), dy);
//        g.drawString(Driver.progress + "%" , x , y - dy);
    }

    private final String[] titleLine ={
            "   _____                                               \n",
            "  / ___/______________ _____  ___                      \n",
            "  \\__ \\/ ___/ ___/ __ `/ __ \\/ _ \\                 \n",
            " ___/ / /__/ /  / /_/ / /_/ /  __/                     \n",
            "/____/\\___/_/   \\__,_/ .___/\\___/           __ __   \n",
            "   / / / /__  _  ___/_/ _____ _____  ____  / // /   __ \n",
            "  / /_/ / _ \\| |/_/ __ `/ __ `/ __ \\/ __ \\/ // /___/ /_\n",
            " / __  /  __/>  </ /_/ / /_/ / /_/ / / / /__  __/_  __/\n",
            "/_/ /_/\\___/_/|_|\\__,_/\\__, /\\____/_/ /_/  /_/   /_/   \n",
            "                      /____/                           \n"
    };

    public void setW_H(int w, int h) {
        this.w = w; this.h = h;
    }

}
