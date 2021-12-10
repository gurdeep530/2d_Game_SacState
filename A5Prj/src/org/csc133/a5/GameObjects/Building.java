package org.csc133.a5.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;

import java.util.ArrayList;
import java.util.Random;

import static com.codename1.ui.CN.*;

public class Building extends Fixed {

    private final Random RND = new Random();
    private final int DISP_H;
    private final int DISP_W;
    private int buildingDamage;
    private Dimension BUILDING_DIM;
    private int damage;
    private int value;
    private final Transform BUILDING;

    public Building(int i, Dimension worldSize) {
        this.DISP_H = worldSize.getHeight();
        this.DISP_W = worldSize.getWidth();
        BUILDING = Transform.makeIdentity();
        BUILDING_DIM = new Dimension(0,0);
        damage = 0;
        value = 0;
        setColor(ColorUtil.rgb(255, 0, 0));
        createBuildings(i);
    }


    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                          Point originScreen) {

        String[] labels;

        g.setColor(color);
        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
        g.setFont(f);

        Transform gBuildingForm = preLTTransform(g, originScreen);
        localTransform(gBuildingForm);
        postLTTransform(g, originScreen, gBuildingForm);

        g.setTransform(flipGameObjectsAfterVTM(containerOrigin, originScreen));

        labels = getLabels();
        drawBuilding(g, containerOrigin);
        drawBuildingLabels(g, containerOrigin, labels);

        resetTranformToOrginal(g);

        g.resetAffine();
    }

    public Fire setFireInBuilding(Fire fire) {
        Point2D[] bounds = getBuildingBounds();

        int xBound = (int) (RND.nextInt(
                (int) ((bounds[1].getX()) - bounds[0].getX()))
                + (bounds[0].getX()));
        int bound = (int) (bounds[0].getY() - bounds[2].getY());
        int yBound = (int) (RND.nextInt(bound)
                + (bounds[2].getY()));

        fire.translate(xBound, yBound);
        return fire;
    }

    public void buildingDamages(ArrayList<GameObject> fires) {
        Point2D[] bounds = getBuildingBounds();
        for (GameObject fire : fires) {
            if (bounds[0].getX() <= fire.getX()
                && (bounds[1].getY() >= fire.getY())
                    && (bounds[1].getX() >= fire.getX())
                    && ((bounds[2].getY() <= fire.getY())))
                buildingDamage += Math.pow(fire.getDimensionsW(), 2);
        }

        setBuildingDamage();
    }

    public int getBuildingValue() {
        return value;
    }

    public int getBuildingDamage(){
        return damage;
    }
    Point2D[] getBuildingBounds() {
        Point2D[] bounds = new Point2D[4];

        //top left
        bounds[0] = new Point2D(
                myTranslation.getTranslateX() - (getDimensionsW() / 2.0),
                myTranslation.getTranslateY() + (getDimensionsH() / 2.0));

        //top right
        bounds[1] = new Point2D(
                myTranslation.getTranslateX() + (getDimensionsW() / 2.0),
                myTranslation.getTranslateY() + (getDimensionsH() / 2.0));
        //bottom left
        bounds[2] = new Point2D(
                myTranslation.getTranslateX() - (getDimensionsW() / 2.0),
                myTranslation.getTranslateY() - (getDimensionsH() / 2.0));

        //bottom right
        bounds[3] = new Point2D(
                myTranslation.getTranslateX() + (getDimensionsW() / 2.0),
                myTranslation.getTranslateY() - (getDimensionsH() / 2.0));

        return bounds;
    }

    private void createBuildings(int i) {
        if (i == 1)
            createTheBuildingAboveRiver();

        else if (i == 2)
            createTheBuildingOnLeft();

        else if (i == 3)
            createTheBuildingOnRight();

    }



    private void createTheBuildingAboveRiver() {
        int RANDOM_NUM = (RND.nextInt(700 - 300) + 300);

        setDimensions((DISP_W - RANDOM_NUM), ((int) ((DISP_H * .2) / 2)));

        BUILDING_DIM = new Dimension(getDimensionsW(), getDimensionsH());
        RANDOM_NUM = RND.nextInt(1000 - 100) + 100;
        value = RANDOM_NUM;

        translate(DISP_W / 2.0, DISP_H * .20);
        BUILDING.setTranslation(myTranslation.getTranslateX(),
                myTranslation.getTranslateY());
    }

    private void createTheBuildingOnLeft() {
        int RANDOM_NUM = (RND.nextInt(4 - 2) + 2);

        setDimensions((int) (DISP_W / 8.0), ((DISP_H) / RANDOM_NUM));

        BUILDING_DIM = new Dimension(getDimensionsW(), getDimensionsH());

        RANDOM_NUM = RND.nextInt(1000 - 100) + 100;
        value = RANDOM_NUM;

        translate(DISP_W * .2, DISP_H / 1.5);
        BUILDING.setTranslation(myTranslation.getTranslateX(),
                myTranslation.getTranslateY());
    }

    private void createTheBuildingOnRight() {
        int RANDOM_NUM = (RND.nextInt(4 - 2) + 2);
        setDimensions((int) (DISP_W / 8.0), ((DISP_H) / RANDOM_NUM));

        BUILDING_DIM = new Dimension(getDimensionsW(), getDimensionsH());

        value = RND.nextInt(1000 - 100) + 100;

        translate(DISP_W - (DISP_W * .2), DISP_H / 1.5);
        BUILDING.setTranslation(myTranslation.getTranslateX(),
                myTranslation.getTranslateY());
    }



    private void setBuildingDamage() {
        float dam = (BUILDING_DIM.getWidth() * BUILDING_DIM.getHeight())/4f;

        damage = (int) ((buildingDamage / dam) * 100);

        if (damage > 100)
            damage = 100;

    }

    private String[] getLabels() {
        String[] labels = new String[2];
        labels[0] = ("V: " + value);
        labels[1] = ("D: " + damage + "%");

        return labels;
    }

    private void drawBuilding(Graphics g, Point containerOrigin) {
        g.drawRect(-getDimensionsW() / 2,
                -getDimensionsH() / 2,
                getDimensionsW(),
                getDimensionsH(), 5);

    }

    private void drawBuildingLabels(Graphics g,
                                    Point containerOrigin, String[] labels) {
        g.drawString(labels[0],
                (getDimensionsW() / 2) + 20,
                (getDimensionsH() / 2) - 50);
        g.drawString(labels[1],
                (getDimensionsW() / 2) + 20,
                (getDimensionsH() / 2));
    }

}


