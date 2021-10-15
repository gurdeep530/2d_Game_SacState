package org.csc133.a2.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a2.Interfaces.Drawable;

import java.util.ArrayList;
import java.util.Random;

import static com.codename1.ui.CN.*;

public class Fire extends Fixed implements Drawable {
    private static final ArrayList<Dimension> FIRE_SIZE = new ArrayList<>();
    private static final ArrayList<Point2D> FIRE = new ArrayList<>();
    private static final ArrayList<Integer> RANDOM_NUM = new ArrayList<>();
    private int DISP_W;
    private final Random RND = new Random();

    public Fire(Fire fire, int i, Dimension worldSize)
    {
        setLocation(new Point2D(0,0));
        setDimensions(new Dimension(0,0));
        setColor(ColorUtil.MAGENTA);

        this.DISP_W = worldSize.getWidth();
        getFire(i, fire.getLocation());
    }
    public Fire()
    {}

    @Override
    public void draw(Graphics g, Point containerOrigin) {
        String label;

        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_LARGE);

        g.setFont(f);
        g.setColor(color);

        int fire = whichFire(containerOrigin);
        if(FIRE_SIZE.get(fire).getWidth() > 0) {

            label = String.valueOf(FIRE_SIZE.get(fire).getWidth());
            g.drawString(label,
                        containerOrigin.getX()+FIRE_SIZE.get(fire).getWidth(),
                        containerOrigin.getY()+FIRE_SIZE.get(fire).getWidth());

            g.fillArc(    containerOrigin.getX(),
                          containerOrigin.getY(),
                          FIRE_SIZE.get(fire).getWidth(),
                          FIRE_SIZE.get(fire).getWidth(),
                          0, 360);
        }
    }

    private void getFire(int i, Point2D loc) {
        makeFires(loc);
        //setLocation(FIRE.get(i));
        setDimensions(FIRE_SIZE.get(i));
    }

    private int whichFire(Point containerOrigin)
    {
        int whichFire = 0;
        for(int i = 0; i<FIRE_SIZE.size();i++)
        {
            if(containerOrigin.getX() == FIRE.get(i).getX()
                    && containerOrigin.getY() == FIRE.get(i).getY())
            {
                whichFire = i;
                break;
            }
        }
        return whichFire;
    }

    private void makeFires(Point2D loc)
    {

        int num = ((int) ((DISP_W* .005) + RND.nextInt(15)));
        RANDOM_NUM.add(checkDim(num));
        int i = RANDOM_NUM.size() - 1;

        FIRE_SIZE.add(new Dimension(RANDOM_NUM.get(i), RANDOM_NUM.get(i)));
        FIRE.add(new Point2D(loc.getX(), loc.getY()));
        i = FIRE.size() - 1;
        //setLocation(FIRE.get(i));

    }

    private int checkDim(int num)
    {
        while(RANDOM_NUM.contains(num))
        {
            num +=1;
        }
        return num;
    }

    public Point2D[] fireBounds(GameObject go) {

        Point2D[] bounds = new Point2D[4];

        //top left point
        bounds[0] = new Point2D(go.getLocationX() - 50,
                                go.getLocationY() - 50);

        //top right point
        bounds[1] = new Point2D
                        (go.getLocationX() + go.getDimensionsW() + 50,
                                go.getLocationY() - 50);

        //bottom left
        bounds[2] = new Point2D
                        (  go.getLocationX() - 50,
                                go.getLocationY() + go.getDimensionsW() + 50);

        //bottom right
        bounds[3] = new Point2D
                        (go.getLocationX() + go.getDimensionsW() + 50,
                         go.getLocationY() + go.getDimensionsW() + 50);
        return bounds;
    }

    public void shrink(GameObject go, int water) {

        int i = whichFire(go.convertToPoint(go.getLocation()));

        FIRE_SIZE.get(i).setWidth(FIRE_SIZE.get(i).getWidth() - (water / 5));

        if(FIRE_SIZE.get(i).getWidth() <= 0) {
            FIRE_SIZE.get(i).setWidth(0);
        }
    }

    public void grow(int i) {

        if(FIRE_SIZE.get(i).getWidth() > 0) {
            FIRE_SIZE.get(i).setWidth(
                    FIRE_SIZE.get(i).getWidth() + RND.nextInt(5));
        }
    }

    public Boolean areFiresOut()
    {
        boolean firesAreOut = false;
        for(Dimension dim : FIRE_SIZE)
        {
            firesAreOut = dim.getWidth() <= 0;

        }
        return firesAreOut;
    }

    public ArrayList<Dimension> FireSizes()
    {
        return FIRE_SIZE;
    }

}
