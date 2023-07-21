package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Listener implements MouseMotionListener, MouseListener {

    public int x;
    public int y = 500;
    public int fx ;
    public int fy ;

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(e.getX() > 650 && e.getX() < Main.WIDTH - 20 && e.getY() > 20 && e.getY() < 120) {
                Core.canMove = true;
                fx = e.getX();
                fy = e.getY();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Core.canMove = false;
        fx = 0;
        fy = 0;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
