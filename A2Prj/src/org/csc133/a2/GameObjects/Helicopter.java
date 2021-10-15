package org.csc133.a2.GameObjects;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a2.Interfaces.Drawable;
import org.csc133.a2.Interfaces.Steerable;

import static com.codename1.ui.CN.*;

public class Helicopter extends Movable implements Steerable, Drawable {

    private final boolean IS_MOVING = false;
    private int baseSize,labelSpacing;
    private static Point2D bottomNoseLocation, baseLocation,
                    labelLocation, topNoseLocation = new Point2D(0,0);
    private static final String[] HELI_LABELS = new String[2];
    private final HeliPad hp;
    private final River r;
    public int MAX_SPEED = 10;

    public Helicopter(Point2D loc, Dimension worldSize)
    {
        hp = new HeliPad(worldSize);
        r = new River(worldSize);

        setColor(ColorUtil.YELLOW);
        if(loc == null)
            setLocation(0,0);
        else
        setLocation(loc);
    }
    @Override
    public void steerLeft() {
        ChangeDirection(-15);
    }

    @Override
    public void steerRight() {
        ChangeDirection(15);
    }

    public int[] newPositions()
    {
        return Move();
    }

    //Checks for if heli is over the River and fire.
    //I put them in the Heli class because the heli is
    //performing the action on the riverDimension/fire so the heli needs

    //to know if it is over them
    public boolean IsHeliOverRiver(Point2D heliLocChange) {
        Point2D heliBaseCoords;
        Point2D[] riverBounds;
        heliBaseCoords = setBaseLocation(heliLocChange);
        riverBounds = r.getRiverBounds();

        return ((heliBaseCoords.getY() >= riverBounds[0].getY())
                && (heliBaseCoords.getY() <= riverBounds[1].getY()));
    }

    public boolean IsHeliOverFire(Point2D heliLocChange, Point2D[] bounds) {
        Point2D heliBaseCoords;
        Point2D[] fireBounds;
        heliBaseCoords = setBaseLocation(heliLocChange);
        fireBounds = bounds;

        return  ((fireBounds[0].getX() <= heliBaseCoords.getX())
                    && (fireBounds[1].getY() <= heliBaseCoords.getY())
                    && (fireBounds[1].getX() >= heliBaseCoords.getX())
                    && ((heliBaseCoords.getY() <= fireBounds[2].getY())));

    }

    public boolean CanHeliLand(Point2D heliLocChange) {
        Point2D[] circleBounds = hp.CircleBounds();
        Point2D heliBaseCoords = setBaseLocation(heliLocChange);

        return Speed() == 0
                &&circleBounds[0].getX() <= heliBaseCoords.getX()
                && circleBounds[1].getY() <= heliBaseCoords.getY()
                && circleBounds[1].getX() >= heliBaseCoords.getX()
                && circleBounds[2].getY() >= heliBaseCoords.getY();
    }

    //Setting the labels because they change threw out the game
    public void setLabels(int fuel, int water) {
        HELI_LABELS[0] = ("F: " + fuel);
        HELI_LABELS[1] = ("W: " + water);
    }

    //if statements are for the initial placement of the objects
    //after the heli starts moving it adds the new coords to the
    //old.
    //draw function
    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(color);

        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
        g.setFont(f);

        if(!IS_MOVING)
        {
            HeliBase();
            setDimensions(baseSize,baseSize);
        }

        Point base = setLocations(baseLocation, containerOrigin);

        g.fillArc(  base.getX(),
                    base.getY(),
                    getDimensionsW(),getDimensionsH(),0,360);

        if(!IS_MOVING) {
            HeliNoseLocation();
        }

        Point bottomNose = setLocations(bottomNoseLocation, containerOrigin);
        Point topNose = setLocations(topNoseLocation,containerOrigin);

        g.drawLine( bottomNose.getX(),
                    bottomNose.getY(),
                topNose.getX() + (int)(15*(AngleCos()*5)),
                topNose.getY() + (int)(15*(AngleSin()*5)));

        if (!IS_MOVING)
        {
            labelLocation = HeliLabelLocation();
        }

        Point labelLoc = setLocations(labelLocation,containerOrigin);

        g.drawString(HELI_LABELS[0],
                    labelLoc.getX(),
                    labelLoc.getY());

        g.drawString(HELI_LABELS[1],
                    labelLoc.getX(),
                    (labelLoc.getY() + labelSpacing));

    }


    //I made these initial functions so that we
    //wouldn't have to do the math over and over
    //if we needed it somewhere else
    //initial placement of the HeliNoseLocation
    private int[] HeliBase() {
        Point2D location = hp.HeliPadCircleLocation();
        Dimension dimension = hp.HeliPadCircleDimension();
        int[] coords  = new int[3];

        location.setX((int)(location.getX() + (dimension.getWidth() / 2.7)));
        location.setY((int)(location.getY() + (dimension.getWidth() / 2)));
        baseSize = (int) (dimension.getWidth() / 3.5);

        baseLocation = location;

        coords[0] = (int)location.getX();
        coords[1] = (int)location.getY();
        coords[2] = baseSize;

        return coords;
    }

    private void HeliNoseLocation() {
        int[] coords = HeliBase();
        Point2D location = new Point2D(coords[0],coords[1]);
        Dimension dimension = hp.HeliPadCircleDimension();

        if(!IS_MOVING) {
            location.setX(location.getX() + (dimension.getWidth() / 6.0));
            location.setY(location.getY() + (dimension.getWidth() / 4.0));
        }
        bottomNoseLocation = location;

        location.setX(location.getX());
        location.setY(location.getY());
        topNoseLocation = location;
    }

    private Point2D HeliLabelLocation() {
        int[] coords = new int[3];
        coords[0] = (int) hp.HeliPadSquareLocation().getX();
        coords[1] = (int) hp.HeliPadSquareLocation().getY();
        coords[2] =  hp.HeliPadSquareDimension().getWidth();

        coords[0] += coords[2] / 2;
        coords[1] += coords[2];
        labelSpacing = (int) (coords[2] * .25);

        return new Point2D(coords[0], coords[1]);
    }

    private Point2D setBaseLocation(Point2D location) {
        HeliBase();
        baseLocation.setX(baseLocation.getX() + location.getX());
        baseLocation.setY(baseLocation.getY() + location.getY());
        return baseLocation;
    }

    private Point setLocations(Point2D orgLoc, Point newLoc)
    {
        return new Point((int) orgLoc.getX() + newLoc.getX(),
                         (int)orgLoc.getY() + newLoc.getY());
    }

}
