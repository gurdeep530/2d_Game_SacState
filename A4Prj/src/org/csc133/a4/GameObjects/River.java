package org.csc133.a4.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a4.Interfaces.Drawable;

public class River extends Fixed implements Drawable {
    private final int DISP_H;
    private final int DISP_W;

    public River(Dimension worldSize)
    {
        this.DISP_H = worldSize.getHeight();
        this.DISP_W = worldSize.getWidth();

        setColor(ColorUtil.BLUE);
        setDimensions(RiverDimension());

        translate(DISP_W/2.0,DISP_H * .33);
    }

    @Override
    public void draw(Graphics g, Point containerOrigin, Point screenOrigin)
    {
        localDraw(g,containerOrigin,screenOrigin);
        g.resetAffine();
    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                         Point screenOrigin) {

        g.setColor(color);

        Transform gRiverForm = preLTTransform(g,screenOrigin);
        localTransform(gRiverForm);
        postLTTransform(g, screenOrigin,gRiverForm);

        g.setTransform(flipGameObjectsAfterVTM( containerOrigin, screenOrigin));

        g.drawRect( -getDimensionsW()/2,
                    -getDimensionsH()/2,
                    getDimensionsW(),getDimensionsH(),5);

        resetTranformToOrginal(g);

        g.resetAffine();
    }

    private Dimension RiverDimension()
    {
        setDimensions((DISP_W ),((int) ((DISP_H * .2) / 2)));
        return getDimension();
    }

    Point2D[] getRiverBounds() {
        //point 1 is top left, 2 is bottom right
        Point2D[] bounds = new Point2D[4];

        //top left
        bounds[0] = new Point2D(
                myTranslation.getTranslateX()
                        - (getDimensionsW()/2.0),
                myTranslation.getTranslateY()
                        + (getDimensionsH()/2.0));

        //top right
        bounds[1] = new Point2D(
                myTranslation.getTranslateX()
                        + (getDimensionsW()/2.0),
                myTranslation.getTranslateY()
                        + (getDimensionsH()/2.0));

        //bottom left
        bounds[2] = new Point2D(
                myTranslation.getTranslateX()
                        - (getDimensionsW()/2.0),
                myTranslation.getTranslateY()
                        - (getDimensionsH()/2.0));

        //bottom right
        bounds[3] = new Point2D(
                myTranslation.getTranslateX()
                        + (getDimensionsW()/2.0),
                myTranslation.getTranslateY()
                        - (getDimensionsH()/2.0));

        return bounds;
    }


}
