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
    private final static Dimension[] BUILDING_DIM = new Dimension[4];
    private final static int[] BUILDING_VALUE = new int[4];
    private final static int[] BUILDING_DAMAGE = new int[4];
    private final static Transform BUILDING_ONE = Transform.makeIdentity();
    private final static Transform BUILDING_TWO = Transform.makeIdentity();
    private final static Transform BUILDING_THREE = Transform.makeIdentity();
    private final River r;

    public Building(int i, Dimension worldSize){
        this.DISP_H = worldSize.getHeight();
        this.DISP_W = worldSize.getWidth();
        setColor(ColorUtil.rgb(255,0,0));
        r = new River(worldSize);
        createBuildings(i);
    }

    @Override
    public void draw(Graphics g, Point containerOrigin, Point screenOrigin)
    {
        localDraw(g,containerOrigin,screenOrigin);
        g.resetAffine();
    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                          Point originScreen) {

        String[] labels;

        g.setColor(color);
        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
        g.setFont(f);

        Transform gBuildingForm = preLTTransform(g,originScreen);
        localTransform(gBuildingForm);
        postLTTransform(g, originScreen,gBuildingForm);

        g.setTransform(flipGameObjectsAfterVTM( containerOrigin, originScreen));

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
                            (int)((bounds[1].getX()) - bounds[0].getX()))
                                + (bounds[0].getX()));
        int bound = (int) (bounds[0].getY() - bounds[2].getY());
        int yBound = (int) (RND.nextInt(bound)
                                + (bounds[2].getY()));

         fire.translate(xBound,yBound);
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
        bounds[0] = new Point2D(
                getBuildingTransform(whichBuilding).getTranslateX()
                                            - (getDimensionsW()/2.0),
                getBuildingTransform(whichBuilding).getTranslateY()
                                            + (getDimensionsH()/2.0));

        //top right
        bounds[1] = new Point2D(
                getBuildingTransform(whichBuilding).getTranslateX()
                                            + (getDimensionsW()/2.0),
                getBuildingTransform(whichBuilding).getTranslateY()
                                            + (getDimensionsH()/2.0));

        //bottom left
        bounds[2] = new Point2D(
                getBuildingTransform(whichBuilding).getTranslateX()
                                            - (getDimensionsW()/2.0),
                getBuildingTransform(whichBuilding).getTranslateY()
                                            - (getDimensionsH()/2.0));

        //bottom right
        bounds[3] = new Point2D(
                getBuildingTransform(whichBuilding).getTranslateX()
                                            + (getDimensionsW()/2.0),
                getBuildingTransform(whichBuilding).getTranslateY()
                                            - (getDimensionsH()/2.0));

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

    private Transform getBuildingTransform(int whichBuilding)
    {
        Transform transform = Transform.makeIdentity();

        if(whichBuilding == 1)
            transform = BUILDING_ONE;
        else if(whichBuilding == 2)
            transform = BUILDING_TWO;
        else if(whichBuilding == 3)
            transform = BUILDING_THREE;

        return transform;
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
                getBuildingTransform(whichBuilding).setTranslation
                        (       myTranslation.getTranslateX(),
                                myTranslation.getTranslateY());
            }
        }
        return whichBuilding;
    }

    private void createTheBuildingAboveRiver()
    {
        int RANDOM_NUM = (RND.nextInt(700 - 300) + 300);

        setDimensions((DISP_W - RANDOM_NUM), ((int) ((DISP_H * .2) / 2)));

        BUILDING_DIM[1] = new Dimension(getDimensionsW(),getDimensionsH());
        RANDOM_NUM = RND.nextInt(1000 - 100) +100;
        BUILDING_VALUE[1] = RANDOM_NUM;

        translate(DISP_W/2.0, DISP_H*.20);
        BUILDING_ONE.setTranslation(myTranslation.getTranslateX(),
                                    myTranslation.getTranslateY());
    }

    private void createTheBuildingOnLeft()
    {
        int RANDOM_NUM = (RND.nextInt(4 - 2) +2 );

        setDimensions((int) (DISP_W / 8.0), ((DISP_H) / RANDOM_NUM));

        BUILDING_DIM[2] = new Dimension(getDimensionsW(),getDimensionsH());

        RANDOM_NUM = RND.nextInt(1000 - 100) +100;
        BUILDING_VALUE[2] = RANDOM_NUM;

        translate(DISP_W * .2,DISP_H/1.5);
        BUILDING_TWO.setTranslation(myTranslation.getTranslateX(),
                myTranslation.getTranslateY());
    }

    private void createTheBuildingOnRight()
    {
        setDimensions((int) (DISP_W / 8.0), getUniqueRandomNum());

        BUILDING_DIM[3] = new Dimension(getDimensionsW(),getDimensionsH());

        int RANDOM_NUM = RND.nextInt(1000 - 100) +100;
        BUILDING_VALUE[3] = RANDOM_NUM;

        translate(DISP_W - (DISP_W *.2),DISP_H/1.5);
        BUILDING_THREE.setTranslation(myTranslation.getTranslateX(),
                myTranslation.getTranslateY());
    }

    private Boolean isBuildingDimTheSame()
    {
        return getDimensionsH() == BUILDING_DIM[2].getHeight();
    }

    private int getUniqueRandomNum()
    {
        int RANDOM_NUM = (RND.nextInt(4 - 2) +2 );
        int height = ((DISP_H) / RANDOM_NUM);
        while(isBuildingDimTheSame())
        {
            RANDOM_NUM = (RND.nextInt(4 - 2) +2 );
            height = ((DISP_H) / RANDOM_NUM);
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
