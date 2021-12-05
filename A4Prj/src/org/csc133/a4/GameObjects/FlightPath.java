package org.csc133.a4.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;

import java.util.ArrayList;

public class FlightPath extends GameObject{


    //region BezierCurve Class
    static class BezierCurve{
        ArrayList<Point2D> controlPoints;
        public BezierCurve(ArrayList<Point2D> controlPoints)
        {
            this.controlPoints = controlPoints;
        }

        public Point2D getStartControlPoint()
        {
            return controlPoints.get(0);
        }

        private void drawBezierCurve(Graphics g)
        {
            g.setColor(ColorUtil.WHITE);
            for(Point2D p: controlPoints)
                g.fillArc((int)p.getX()-15,(int)p.getY()-15,
                        30,30,0,360);
            double t = 0;
            float smallFloatIncrement = .01f;
            Point2D currentPoint = controlPoints.get(0);

            while(t<1){
                Point2D nextPoint = evaluateCurve(t);

                g.drawLine( (int) currentPoint.getX(),
                            (int) currentPoint.getY(),
                            (int) nextPoint.getX(),
                            (int) nextPoint.getY());

                currentPoint = nextPoint;
                t = t + smallFloatIncrement;
            }

        }

        private Point2D evaluateCurve(double t){
            Point2D p = new Point2D(0,0);
            int d = controlPoints.size() - 1;
            for(int i = 0; i<controlPoints.size(); i++)
            {
                p.setX(p.getX() + BernsteinD(d,i,t)
                        * controlPoints.get(i).getX());
                p.setY(p.getY() + BernsteinD(d,i,t)
                        * controlPoints.get(i).getY());
            }
            return p;
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

    private final BezierCurve pathToSelectedFire;
    private final BezierCurve pathToWater;
    private final BezierCurve pathToReturnToRiver;

    public FlightPath(HeliPad hp, River r, Fire selectedFire)
    {
        this.pathToWater = new BezierCurve(pathToRiverFromHelipad(hp,r));
        this.pathToSelectedFire = new BezierCurve(pathToSelectedFire(hp,r,
                selectedFire));
        this.pathToReturnToRiver = new BezierCurve(pathToReturnToRiver(r,
                selectedFire));
    }
    @Override
    public void localDraw(Graphics g, Point containerOrigin, Point originScreen)
    {
        containerTranslate(g);
        pathToWater.drawBezierCurve(g);
        pathToSelectedFire.drawBezierCurve(g);
        pathToReturnToRiver.drawBezierCurve(g);
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
    private ArrayList<Point2D> pathToRiverFromHelipad(HeliPad hp, River r)
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
