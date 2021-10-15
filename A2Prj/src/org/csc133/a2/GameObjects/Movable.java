package org.csc133.a2.GameObjects;

public class Movable extends GameObject{

    private static int HEADING = -90;
    private static int SPEED = 0;
    int NEW_POS_X = 0;
    int NEW_POS_Y = 0;
    int TOP_POS_X = 0;
    int TOP_POS_Y = 0;

    public int[] Move() {

        double angleSin = AngleSin();
        double angleCos = AngleCos();

        if (SPEED > 0) {
            NEW_POS_Y += (int) (angleSin * (SPEED));
            NEW_POS_X += (int) (angleCos * (SPEED));

            TOP_POS_X += (int) (angleSin * 5);
            TOP_POS_Y += (int) (angleCos * 5);
        }
        int[] newPos = new int[4];
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

    public int Speed()
    {
        return SPEED;
    }

    public int Heading()
    {
        return HEADING;
    }
}
