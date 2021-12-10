package org.csc133.a5.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
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

        private Point2D getStartControlPoint()
        {
            return controlPoints.get(0);
        }

        private Point2D getEndingControlPoint()
        {
            return controlPoints.get(controlPoints.size()-1);
        }

        private Point2D getFlightTranslation(double t, Point2D c)
        {
            Point2D p = evaluateCurve(t);
            p.setX(p.getX()-c.getX());
            p.setY(p.getY()-c.getY());
            return p;
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

    private BezierCurve path;
    private BezierCurve pathToSelectedFire;
    private BezierCurve pathToWater;
    private BezierCurve pathToReturnToRiver;

    public FlightPath(HeliPad hp, River r, Fire selectedFire)
    {
        pathToWater = new BezierCurve(pathToRiverFromHelipad(hp,r));
        pathToSelectedFire = new BezierCurve(pathToSelectedFire(hp,r,
                selectedFire));
        pathToReturnToRiver = new BezierCurve(pathToReturnToRiver(r,
                selectedFire));
        path = pathToWater;
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

    public void updateFlightPath(HeliPad hp, River r, Fire selectedFire)
    {
        pathToWater = new BezierCurve(pathToRiverFromHelipad(hp,r));
        pathToSelectedFire = new BezierCurve(pathToSelectedFire(hp,r,
                selectedFire));
        pathToReturnToRiver = new BezierCurve(pathToReturnToRiver(r,
                selectedFire));
        pathToSelectedFire = new BezierCurve(pathToSelectedFire
                (hp,r,selectedFire));
    }
    public Point2D getStartPoint()
    {
        return pathToWater.getStartControlPoint();
    }

    public Point2D getRiverEndPoint()
    {
        return pathToWater.getEndingControlPoint();
    }

    public Point2D getFireEndPoint()
    {
        return pathToSelectedFire.getEndingControlPoint();
    }

    public boolean setPath(Transform NPHTranslation)
    {
        boolean hasPathChanged = false;
        BezierCurve oldpath = path;
        if(figureOutPathForNPH(NPHTranslation) != null) {
            path = figureOutPathForNPH(NPHTranslation);
        }
        if(path != oldpath)
            hasPathChanged = true;
        return hasPathChanged;
    }
    public Point2D getPathTranslation(double t, Point2D c)
    {
        return path.getFlightTranslation(t,c);
    }

    private BezierCurve figureOutPathForNPH(Transform NPHTranslation)
    {

        if(NPHTranslationAndPathCompare(NPHTranslation,pathToWater))
            path = pathToWater;
        else if(NPHTranslationAndPathCompare(NPHTranslation,
                pathToSelectedFire))
            path = pathToSelectedFire;
        else if(NPHTranslationAndPathCompare(NPHTranslation,
                pathToReturnToRiver))
            path = pathToReturnToRiver;

        return path;
    }

    private boolean NPHTranslationAndPathCompare(Transform NPHTranslation,
                                                 BezierCurve path)
    {
        return (NPHTranslation.getTranslateY())
                == path.getStartControlPoint().getY()
                && NPHTranslation.getTranslateX()
                == path.getStartControlPoint().getX();
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
