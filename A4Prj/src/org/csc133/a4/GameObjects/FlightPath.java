package org.csc133.a4.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;

import java.util.ArrayList;

public class FlightPath extends GameObject{


    //region BezierCurve Class
    static class BezierCurve{

        private void drawBezierCurve(Graphics g,
                                    ArrayList<Point2D> controlPoints)
        {
            g.setColor(ColorUtil.WHITE);
            for(Point2D p: controlPoints)
                g.fillArc((int)p.getX()-15,(int)p.getY()-15,
                        30,30,0,360);
            double t = 0;
            float smallFloatIncrement = .01f;
            Point2D currentPoint = controlPoints.get(0);
            while(t<1){
                Point2D nextPoint = new Point2D(0,0);
                int d = controlPoints.size()-1;

                for(int i = 0; i<controlPoints.size(); i++)
                {
                    double b = BernsteinD(d,i,t);
                    double mx = b * controlPoints.get(i).getX();
                    double my = b * controlPoints.get(i).getY();

                    nextPoint.setX(nextPoint.getX() + mx);
                    nextPoint.setY(nextPoint.getY() + my);
                }

                g.drawLine( (int) currentPoint.getX(),
                            (int) currentPoint.getY(),
                            (int) nextPoint.getX(),
                            (int) nextPoint.getY());

                currentPoint = nextPoint;
                t = t + smallFloatIncrement;
            }

        }

        private double BernsteinD(int d, int i, double t)
        {
            return choose(d,i)*Math.pow(t,i)*Math.pow(1-t,d-i);
        }

        private double choose(int n, int k)
        {
            if(k<=0 || k>=n)
            {
                return 1;
            }
            return choose(n-1,k-1) + choose(n-1,k);
        }
    }
    //endregion

    //region FlightPath Class

    private final BezierCurve bc;
    private final ArrayList<Point2D> controlPointsToGetWater;
    private final ArrayList<Point2D> controlPointsToSelectedFire;
    private final ArrayList<Point2D> controlPointsToReturnToRiver;

    public FlightPath(HeliPad hp, River r, Fire selectedFire)
    {
        this.bc = new BezierCurve();
        controlPointsToGetWater = pathToFiverFromHelipad(hp,r);
        controlPointsToSelectedFire = pathToSelectedFire(hp,r,selectedFire);
        controlPointsToReturnToRiver = pathToReturnToRiver(r,selectedFire);
    }
    @Override
    public void localDraw(Graphics g, Point containerOrigin, Point originScreen)
    {
        containerTranslate(g);
        bc.drawBezierCurve(g, controlPointsToGetWater);
        bc.drawBezierCurve(g, controlPointsToSelectedFire);
        bc.drawBezierCurve(g ,controlPointsToReturnToRiver);
        g.resetAffine();
    }

    private ArrayList<Point2D> pathToReturnToRiver(River r, Fire selectedFire)
    {
        ArrayList<Point2D> controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(selectedFire.getX(),selectedFire.getY()));
        controlPoints.add(new Point2D(r.getX()+r.getDimensionsW()/2f,
                            r.getY() - r.getDimensionsH()/2f));
        controlPoints.add(new Point2D(r.getX(),r.getY()));
        return controlPoints;
    }

    private ArrayList<Point2D> pathToSelectedFire(HeliPad hp,River r,
                                                  Fire selectedFire)
    {
        ArrayList<Point2D> controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(r.getX(),r.getY()));
        controlPoints.add(new Point2D(15,hp.getY()));
        controlPoints.add(new Point2D(selectedFire.getX(),
                                        selectedFire.getY()));

        return controlPoints;
    }
    private ArrayList<Point2D> pathToFiverFromHelipad(HeliPad hp, River r)
    {
        ArrayList<Point2D> controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(hp.getX(), hp.getY()));
        controlPoints.add(new Point2D(r.getX(),
                            r.getY()+r.getDimensionsH()*2));
        controlPoints.add(new Point2D(15,
                            r.getY() - r.getDimensionsH()/2f));
        controlPoints.add(new Point2D(r.getX(),r.getY()));
        return controlPoints;
    }

    //endregion
}
