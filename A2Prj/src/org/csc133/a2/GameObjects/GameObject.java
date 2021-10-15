package org.csc133.a2.GameObjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a2.Interfaces.Drawable;

public class GameObject implements Drawable {

    final Point2D location;
    int color;
    final Dimension dimension;
    public GameObject()
    {
        location = new Point2D(0.0,0.0);
        dimension = new Dimension(0,0);

    }

    public void setColor(int color)
    {
        this.color = color;
    }
    public double getLocationX()
    {
        return location.getX();
    }

    public double getLocationY()
    {
        return location.getY();
    }

    public Point2D getLocation()
    {
        return location;
    }

    public Point convertToPoint(Point2D point)
    {
        return new Point((int) point.getX(),(int)point.getY());
    }

    public void setLocation(double x, double y)
    {
        location.setX(x);
        location.setY(y);
    }

    public void setLocation(Point2D newLoc)
    {
        location.setX(newLoc.getX());
        location.setY(newLoc.getY());

    }

    public Dimension getDimension(){
        return dimension;
    }
    public int getDimensionsW() {
        return dimension.getWidth();
    }

    public int getDimensionsH() {
        return dimension.getHeight();
    }

    public void setDimensions(int w, int h) {
        dimension.setWidth(w);
        dimension.setHeight(h);
    }
    public void setDimensions(Dimension newDim) {
        dimension.setWidth(newDim.getWidth());
        dimension.setHeight(newDim.getHeight());
    }

    @Override
    public void draw(Graphics g, Point containerOrigin) {

    }
}
