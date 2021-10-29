package org.csc133.a3.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.Interfaces.Drawable;

public class HeliPad extends Fixed implements Drawable {

    private final int DISP_H;
    private final int DISP_W;

    public HeliPad(Dimension worldSize)
    {
        this.DISP_H = worldSize.getHeight();
        this.DISP_W = worldSize.getWidth();
        setColor(ColorUtil.LTGRAY);

        translate(DISP_W/2.0,DISP_H - (DISP_H - (DISP_H*.95)));

    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin, Point originScreen) {
        g.setColor(color);

        setDimensions(HeliPadSquareDimension());

        Transform gHeliPadForm = preLTTransform(g,originScreen);
        localTransform(gHeliPadForm);
        postLTTransform(g, originScreen,gHeliPadForm);

        drawHeliPadSquare(g,containerOrigin);

        setDimensions(HeliPadCircleDimension());

        drawHeliPadCircle(g, containerOrigin);

        resetTranformToOrginal(g);

        g.resetAffine();
    }

    Point2D HeliPadSquareLocation() {
        setDimensions(HeliPadSquareDimension());
        setLocation
                ((DISP_W / 2.0) - (getDimensionsW() / 2.0),
                        DISP_H * .9);

        return getLocation();
    }

    Dimension HeliPadSquareDimension(){
        return new Dimension(
                            ((int)((DISP_H * .25) / 2)),
                            ((int)((DISP_H * .25) / 2)));
    }

    Point2D HeliPadCircleLocation() {
        setDimensions(HeliPadCircleDimension());

        return new Point2D(
                    ((DISP_W / 2.0) - (getDimensionsW() / 2.0)),
                    (int)(DISP_H - (getDimensionsH() * .9)));
    }

    Dimension HeliPadCircleDimension(){
        return new Dimension(
                    (int)(DISP_H * .18) / 2,
                    (int)(DISP_H * .18) / 2);
    }

    Point2D[] CircleBounds() {
        Point2D circleLocation = HeliPadCircleLocation();
        Dimension size = HeliPadCircleDimension();
        Point2D[] bounds = new Point2D[4];

        //upper left corner
        bounds[0] = circleLocation;
        //upper right corner
        bounds[1] = new Point2D(circleLocation.getX() + size.getWidth(),
                                circleLocation.getY());
        //lower left corner
        bounds[2] = new Point2D(circleLocation.getX(),
                             circleLocation.getY() + size.getWidth());
        //lower right corner
        bounds[3] = new Point2D(circleLocation.getX() + size.getWidth(),
                                circleLocation.getY() + size.getWidth());

        return bounds;
    }


    private void drawHeliPadSquare(Graphics g, Point containerOrigin)
    {
        containerOrigin = new Point(-getDimensionsW()/2,-getDimensionsW()/2);

        g.drawRect( containerOrigin.getX(),
                    containerOrigin.getY(),
                    getDimensionsW(),
                    getDimensionsH(),
                    5);
        g.drawLine(-getDimensionsW()/2,0,getDimensionsW()/2,0);
        g.drawLine(0,-getDimensionsH()/2,0,getDimensionsH()/2);
    }

    private void drawHeliPadCircle(Graphics g, Point containerOrigin)
    {
        containerOrigin = new Point(-getDimensionsW()/2,-getDimensionsW()/2);
        g.drawArc(  containerOrigin.getX(),
                    containerOrigin.getY(),
                    getDimensionsW(),
                    getDimensionsH(),
                0, 360);
    }



}
