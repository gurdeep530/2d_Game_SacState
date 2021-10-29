package org.csc133.a3.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.Interfaces.Drawable;

public class River extends Fixed implements Drawable {
    private final int DISP_H;
    private final int DISP_W;
    private Point2D riverLoc;

    public River(Dimension worldSize)
    {
        this.DISP_H = worldSize.getHeight();
        this.DISP_W = worldSize.getWidth();

        setColor(ColorUtil.BLUE);
        setDimensions(RiverDimension());
        //riverLoc = new Point2D(RiverLocation().getX(),RiverLocation().getY());

        translate(DISP_W/2.0,DISP_H*.3);
    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {

        g.setColor(color);

        Transform gRiverForm = preLTTransform(g,screenOrigin);
        localTransform(gRiverForm);
        postLTTransform(g, screenOrigin,gRiverForm);

        g.drawRect( (int) -getDimensionsW()/2,
                    (int)-getDimensionsH()/2,
                    getDimensionsW(),getDimensionsH(),5);
        g.drawLine(-getDimensionsW()/2,0,getDimensionsW()/2,0);
        g.drawLine(0,-getDimensionsH()/2,0,getDimensionsH()/2);;
        resetTranformToOrginal(g);

        g.resetAffine();
    }

    Point2D RiverLocation() {
        return new Point2D(-DISP_W/2.0, ((int) -(DISP_H * .30)));
    }
    private Dimension RiverDimension()
    {
        setDimensions((DISP_W ),((int) ((DISP_H * .2) / 2)));
        return getDimension();
    }

    //bounds of the riverDimension
    Point2D[] getRiverBounds() {
        //point 1 is top left, 2 is bottom right
        Point2D[] bounds = new Point2D[2];

        bounds[0] = new Point2D((int)getLocationX(),
                              (int)getLocationY());
        bounds[1] = new Point2D(
                (int)getLocationX() + getDimensionsW(),
                (int)getLocationY()+ getDimensionsH());

        return bounds;
    }


}
