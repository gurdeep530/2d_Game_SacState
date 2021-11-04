package org.csc133.a3.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.Interfaces.Drawable;

import java.util.ArrayList;
import java.util.Random;

import static com.codename1.ui.CN.*;

public class Fire extends Fixed implements Drawable {
    private static final ArrayList<Dimension> FIRE_SIZE = new ArrayList<>();
    private static final ArrayList<Integer> RANDOM_NUM = new ArrayList<>();
    private static final ArrayList<Transform> FIRES = new ArrayList<>();
    private int DISP_W;

    private final Random RND = new Random();

    public Fire(Fire fire, Dimension worldSize)
    {
        setDimensions(new Dimension(0,0));
        setColor(ColorUtil.MAGENTA);

        this.DISP_W = worldSize.getWidth();
        getFire(fire.myTranslation);
    }

    public Fire()
    {}

    @Override
    public void draw(Graphics g, Point containerOrigin, Point screenOrigin)
    {
        localDraw(g,containerOrigin,screenOrigin);
        g.resetAffine();
    }
    void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {

        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_LARGE);

        g.setFont(f);
        g.setColor(color);

        Transform gFireForm = preLTTransform(g,screenOrigin);
        localTransform(gFireForm);
        postLTTransform(g, screenOrigin,gFireForm);

        createFires(g);

        resetTranformToOrginal(g);

        g.resetAffine();

    }

    public Point2D[] getFireBounds(GameObject go) {

        Point2D[] bounds = new Point2D[4];

        //top left point
        bounds[0] = new Point2D(go.myTranslation.getTranslateX() - 50,
                                go.myTranslation.getTranslateY() - 50);

        //top right point
        bounds[1] = new Point2D(go.myTranslation.getTranslateX()
                                        + go.getDimensionsW() + 50,
                                go.myTranslation.getTranslateY() - 50);

        //bottom left
        bounds[2] = new Point2D(go.myTranslation.getTranslateX() - 50,
                                go.myTranslation.getTranslateY()
                                        + go.getDimensionsW() + 50);

        //bottom right
        bounds[3] = new Point2D(go.myTranslation.getTranslateX()
                                        + go.getDimensionsW() + 50,
                                go.myTranslation.getTranslateY()
                                        + go.getDimensionsW() + 50);
        return bounds;
    }

    public void shrink(GameObject go, int water) {

        int i = whichFireIsMapViewTryingToDraw(go.myTranslation);

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

    private int whichFireIsMapViewTryingToDraw(Transform fire)
    {
        int whichFire = 0;
        for(int i = 0; i<FIRE_SIZE.size();i++)
        {
            if(FIRE_SIZE.get(i).getWidth() > 0 &&
                    fire.getTranslateX() == FIRES.get(i).getTranslateX() &&
                    fire.getTranslateY() == FIRES.get(i).getTranslateY())
            {
                whichFire = i;
                break;
            }
        }
        return whichFire;
    }

    private void getFire(Transform fire) {
        createLocalInstanceOfFires(fire);
    }

    private void createLocalInstanceOfFires(Transform fire) {

        int num = ((int) ((DISP_W * .005) + RND.nextInt(15)));
        RANDOM_NUM.add(changeRandomNumberIfItExistsInTheArray(num));
        int i = RANDOM_NUM.size() - 1;

        setDimensions(new Dimension(RANDOM_NUM.get(i), RANDOM_NUM.get(i)));
        FIRE_SIZE.add(new Dimension(getDimensionsW(),getDimensionsH()));
        FIRES.add(fire);
    }

    private int changeRandomNumberIfItExistsInTheArray(int num)
    {
        while(RANDOM_NUM.contains(num))
        {
            num +=1;
        }
        return num;
    }

    private void createFires(Graphics g)
    {

        int whichFire = whichFireIsMapViewTryingToDraw(myTranslation);
        if(FIRE_SIZE.get(whichFire).getWidth() > 0) {

            drawFireLabel(g,whichFire);

            drawFire(g, whichFire);
        }
    }

    private void drawFire(Graphics g, int whichFire)
    {
        g.fillArc(  -FIRE_SIZE.get(whichFire).getWidth()/2,
                    -FIRE_SIZE.get(whichFire).getWidth()/2,
                    FIRE_SIZE.get(whichFire).getWidth(),
                    FIRE_SIZE.get(whichFire).getWidth(),
                    0, 360);

    }
    private void drawFireLabel(Graphics g, int whichFire)
    {
        String label;

        label = String.valueOf(FIRE_SIZE.get(whichFire).getWidth());
        g.drawString(label,
                FIRE_SIZE.get(whichFire).getWidth(),
                FIRE_SIZE.get(whichFire).getWidth());

    }
}
