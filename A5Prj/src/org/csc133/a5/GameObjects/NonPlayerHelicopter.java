package org.csc133.a5.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;


public class NonPlayerHelicopter extends Helicopter{
    private double t = 0;
    private final GameObjectCollection goc;
    private final Fire f;
    private float x;
    private float y;
    private boolean setUpDone;


    private class NPHStrategy
    {

    }

    public NonPlayerHelicopter(Dimension worldSize,
                               River river, HeliPad heliPad, Fire fire,
                               GameObjectCollection goc) {
        super(  ColorUtil.CYAN,
                ColorUtil.CYAN,
                worldSize,
                river, heliPad);
        f = fire;
        this.goc = goc;
        setUpDone = false;
        setSpeed(10);
    }

    public void setHeliLocation(FlightPath fp) {

        if(!setUpDone) {
            myTranslation.setTranslation((int) fp.getStartPoint().getX(),
                    (int) fp.getStartPoint().getY() - 25);
            x = myTranslation.getTranslateX();
            y = myTranslation.getTranslateY() + 25;
            setUpDone = true;
        }
    }

    public void updateHeli(FlightPath fp)
    {
        getNPHInReadyState();
        NPHFlight(fp);
        consumeFuel(getFuel());
    }

    private void NPHFlight(FlightPath fp)
    {
        mathForFlight(fp);
        canNPHDrink(fp);
        canNPHFight(fp);
        if(t<=1)
            t = t + 0.01;

    }

    private void mathForFlight(FlightPath fp)
    {
        myTranslation.setTranslation((float) Math.ceil(x),(float) Math.ceil(y));
        if(fp.setPath(myTranslation) && t >= 1){
            t = 0;
        }

        Point2D c = new Point2D(myTranslation.getTranslateX(),
                myTranslation.getTranslateY());
        x = getX() + (float)fp.getPathTranslation(t,c).getX();
        y = getY() + (float)fp.getPathTranslation(t,c).getY();
        myTranslation.setTranslation(x,y);
    }
    private void getNPHInReadyState()
    {
        startOrStopEngine();
        accelerate();
        setSpeed(1);
    }
    private void canNPHDrink(FlightPath fp)
    {
        if(PointMatch(myTranslation.getTranslateX(),
                myTranslation.getTranslateY(),
                (float)fp.getRiverEndPoint().getX(),
                (float) fp.getRiverEndPoint().getY()))
        {
            setSpeed(1);
            while(getWater() != 1000)
                drink();
        }
    }

    private void canNPHFight(FlightPath fp)
    {
        if(PointMatch(myTranslation.getTranslateX(),
                myTranslation.getTranslateY(),
                (float)fp.getFireEndPoint().getX(),
                (float) fp.getFireEndPoint().getY()))
        {
            fight(f,getWater(), goc.getFires());
        }
    }

}