package org.csc133.a4.GameObjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;

public class Movable extends GameObject{

    private static int HEADING = 0;
    private static int SPEED = 0;
    static int NEW_POS_X = 0;
    static int NEW_POS_Y = 0;


    public int[] Move() {

        double angleSin = AngleSin();
        double angleCos = AngleCos();

        if (SPEED > 0) {
            NEW_POS_Y += (int) (angleSin * SPEED) ;
            NEW_POS_X += (int) (angleCos * SPEED);
        }
        int[] newPos = new int[2];
        newPos[0] += NEW_POS_X;
        newPos[1] += NEW_POS_Y;
        return newPos;
    }

    public double AngleSin()
    {
        double angle = Math.toRadians(HEADING);
        return Math.sin(angle);
    }

    public double AngleCos()
    {
        double angle = Math.toRadians(HEADING);
        return Math.cos(angle);
    }
    
    public void ChangeDirection(int change)
    {
        HEADING += change;
    }
    
    public void ChangeSpeed(int change)
    {
        SPEED += (change);
    }

    public int Speed()
    {
        return SPEED;
    }

    public int Heading()
    {
        return HEADING;
    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin, Point originScreen) {

    }
}
