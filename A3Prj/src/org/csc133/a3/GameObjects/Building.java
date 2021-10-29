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

public class Building extends Fixed implements Drawable {

    private final Random RND = new Random();
    private final int DISP_H;
    private final int DISP_W;
    private final static int[] buildingDamage = new int[3];
    private final static Point2D[] BUILDINGS_LOC = new Point2D[4];
    private final static Dimension[] BUILDING_DIM = new Dimension[4];
    private final static int[] BUILDING_VALUE = new int[4];
    private final static int[] BUILDING_DAMAGE = new int[4];;


    private final River r;

    public Building(int i, Dimension worldSize){
        this.DISP_H = worldSize.getHeight();
        this.DISP_W = worldSize.getWidth();
        setColor(ColorUtil.rgb(255,0,0));
        r = new River(worldSize);
        createBuildings(i);

        translate(DISP_W/2.0,DISP_H/2.0);
    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin, Point originScreen) {

        String[] labels;

        g.setColor(color);
        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
        g.setFont(f);

        Transform gBuildingForm = preLTTransform(g,originScreen);
        localTransform(gBuildingForm);
        postLTTransform(g, originScreen,gBuildingForm);

        labels = getLabels();
        drawBuilding(g, containerOrigin);
        drawBuildingLabels(g,containerOrigin,labels);

        resetTranformToOrginal(g);

        g.resetAffine();
    }

    public Fire setFireInBuilding(Fire fire, int i)
    {
        Point2D[] bounds = getBuildingBounds(i);

        int xBound = (int) (RND.nextInt(
                            (int)((bounds[1].getX() - 10) - bounds[0].getX()))
                                + (bounds[0].getX() + 10));
        int yBound = (int) (RND.nextInt(
                            (int)((bounds[2].getY() - 10) - bounds[0].getY()))
                                + (bounds[0].getY() - 10));

         fire.setLocation(xBound,yBound);
         return fire;
    }

    public void buildingDamages(ArrayList<Dimension> sizes) {

        int whichBuilding = 0;
        int i = 0;
        int nextThree = 3;
        while(i<nextThree)
        {
            buildingDamage[whichBuilding]
                    += Math.pow(sizes.get(i).getWidth(),2);
            i++;
            if(i == 3 || i == 6 && nextThree != 9)
            {
                nextThree += 3;
                whichBuilding +=1;
            }
        }
        setBuildingDamage();
    }

    public int[] getBuildingValue()
    {
        return BUILDING_VALUE;
    }

    public int[] getBuildingDamage()
    {
        return BUILDING_DAMAGE;
    }

    Point2D[] getBuildingBounds(int whichBuilding)
    {
        Point2D[] bounds = new Point2D[4];

        //top left
        bounds[0] = new Point2D(BUILDINGS_LOC[whichBuilding].getX(),
                                BUILDINGS_LOC[whichBuilding].getY());

        //top right
        bounds[1] = new Point2D(BUILDINGS_LOC[whichBuilding].getX()
                                + BUILDING_DIM[whichBuilding].getWidth(),
                                BUILDINGS_LOC[whichBuilding].getY());

        //bottom left
        bounds[2] = new Point2D(BUILDINGS_LOC[whichBuilding].getX(),
                             BUILDINGS_LOC[whichBuilding].getY()
                                + BUILDING_DIM[whichBuilding].getHeight());

        //bottom right
        bounds[3] = new Point2D(BUILDINGS_LOC[whichBuilding].getX()
                                + BUILDING_DIM[whichBuilding].getWidth(),
                                BUILDINGS_LOC[whichBuilding].getY()
                                + BUILDING_DIM[whichBuilding].getHeight());

        return bounds;
    }

    private void createBuildings(int i)
    {
        if(i == 1)
            createTheBuildingAboveRiver();

        else if(i == 2)
            createTheBuildingOnLeft();

        else if(i == 3)
            createTheBuildingOnRight();

    }

    private int whichBuildingIsMapViewTryingToDraw()
    {
        int whichBuilding = 0;
        for(int i = 1; i<=3;i++)
        {
            if( getDimensionsW() == BUILDING_DIM[i].getWidth()
                    && getDimensionsH() == BUILDING_DIM[i].getHeight())
            {
                whichBuilding = i;
            }
        }
        return whichBuilding;
    }

    private void createTheBuildingAboveRiver()
    {
        Point2D riverLoc = r.RiverLocation();
        int RANDOM_NUM = (RND.nextInt(700 - 300) + 300);

        setLocation(riverLoc.getX() + (DISP_W * .1),
                riverLoc.getY() - (DISP_H / 6.0));
        setDimensions((DISP_W - RANDOM_NUM), ((int) ((DISP_H * .2) / 2)));

        BUILDINGS_LOC[1] = new Point2D(-getDimensionsW()/2.0, -getDimensionsH()/2.0);
        BUILDING_DIM[1] = new Dimension(getDimensionsW(),getDimensionsH());

        RANDOM_NUM = RND.nextInt(1000 - 100) +100;
        BUILDING_VALUE[1] = RANDOM_NUM;

        translate(0, riverLoc.getY() - 40);
    }

    private void createTheBuildingOnLeft()
    {
        int RANDOM_NUM = (RND.nextInt(4 - 2) +2 );

        setLocation(0 + (DISP_W * .1), DISP_H /2.0);
        setDimensions((int) (DISP_W / 8.0), ((DISP_H) / RANDOM_NUM));

        BUILDINGS_LOC[2] =  new Point2D(getLocationX(), getLocationY());
        BUILDING_DIM[2] = new Dimension(getDimensionsW(),getDimensionsH());

        RANDOM_NUM = RND.nextInt(1000 - 100) +100;
        BUILDING_VALUE[2] = RANDOM_NUM;

        translate((DISP_W/-3.0),200);
    }

    private void createTheBuildingOnRight()
    {
        setLocation(DISP_W - (DISP_W * .2), DISP_H /2.0);

        setDimensions((int) (DISP_W / 8.0), getUniqueRandomNum());

        BUILDINGS_LOC[3] =  new Point2D(DISP_W/3.0,200);
        BUILDING_DIM[3] = new Dimension(getDimensionsW(),getDimensionsH());

        int RANDOM_NUM = RND.nextInt(1000 - 100) +100;
        BUILDING_VALUE[3] = RANDOM_NUM;

        translate(DISP_W/3.0,200);
    }

    private Boolean isBuildingDimTheSame()
    {
        Boolean check = false;
        if(BUILDING_DIM[3].getHeight() == BUILDING_DIM[2].getHeight())
        {
            check = true;
        }
        return check;
    }

    private int getUniqueRandomNum()
    {
        int RANDOM_NUM = (RND.nextInt(4 - 2) +2 );
        int height = ((DISP_H) / RANDOM_NUM);
        while(isBuildingDimTheSame())
        {
            RANDOM_NUM = (RND.nextInt(4 - 2) +2 );
        }
        return height;
    }

    private void setBuildingDamage()
    {
        for(int i = 1; i<=3; i++) {
            float dam =
                    (BUILDING_DIM[i].getWidth() * BUILDING_DIM[i].getHeight());

            BUILDING_DAMAGE[i] = (int) ((buildingDamage[i-1] / dam) * 100);

            if(BUILDING_DAMAGE[i]>100)
                BUILDING_DAMAGE[i] = 100;
        }
    }

    private String[] getLabels()
    {
        String[] labels = new String[2];
        int i = whichBuildingIsMapViewTryingToDraw();
        labels[0] = ("V: " + BUILDING_VALUE[i]);
        labels[1] = ("D: " + BUILDING_DAMAGE[i] + "%");

        return labels;
    }

    private void drawBuilding(Graphics g, Point containerOrigin)
    {
        g.drawRect( -getDimensionsW()/2,
                    -getDimensionsH()/2,
                    getDimensionsW(),
                    getDimensionsH(),5);
        g.drawLine(-getDimensionsW()/2,0,getDimensionsW()/2,0);
        g.drawLine(0,-getDimensionsH()/2,0,getDimensionsH()/2);
    }

    private void drawBuildingLabels(Graphics g, Point containerOrigin, String[] labels)
    {
        g.drawString(labels[0],
                 (getDimensionsW()/2) + 20,
                 (getDimensionsH()/2) - 50);
        g.drawString(labels[1],
                 (getDimensionsW()/2) + 20,
                    (getDimensionsH()/2));
    }


}
