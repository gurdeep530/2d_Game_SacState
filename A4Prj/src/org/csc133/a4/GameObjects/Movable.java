package org.csc133.a4.GameObjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public class Movable extends GameObject{

    int NEW_POS_X = 0;
    int NEW_POS_Y = 0;
    private int HEADING = 0;
    private int SPEED = 0;

    public int[] Move() {

        int[] newPos = new int[2];
        double angleSin = AngleSin();
        double angleCos = AngleCos();

        if (SPEED > 0) {
            NEW_POS_Y = (int) (angleSin * (SPEED * 3));
            NEW_POS_X = (int) (angleCos * (SPEED * 3));
        }
        else
        {
            NEW_POS_X = 0;
            NEW_POS_Y = 0;
        }
        newPos[0] = NEW_POS_X;
        newPos[1] = NEW_POS_Y;
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

    public int getSpeed()
    {
        return SPEED;
    }

    public int getHeading()
    {
        return HEADING;
    }

    public void setSpeed(int speed){
        SPEED = speed;
    }
    @Override
    public void localDraw(Graphics g, Point containerOrigin, Point originScreen) {

    }
}
