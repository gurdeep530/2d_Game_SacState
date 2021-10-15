package org.csc133.a2.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a2.Interfaces.Drawable;

public class River extends Fixed implements Drawable {
    private final int DISP_H;
    private final int DISP_W;

    public River(Dimension worldSize)
    {
        this.DISP_H = worldSize.getHeight();
        this.DISP_W = worldSize.getWidth();

        setColor(ColorUtil.BLUE);
        setLocation(RiverLocation());
        setDimensions(RiverDimension());
    }

    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(color);
        g.drawRect((int)getLocationX(), (int)getLocationY(),
                getDimensionsW(), getDimensionsH());
    }

    Point2D RiverLocation() {

        setLocation(-20, ((int) (DISP_H * .30)));
        return getLocation();
    }
    private Dimension RiverDimension()
    {
        setDimensions((DISP_W + 20),((int) ((DISP_H * .2) / 2)));
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
