package org.csc133.a3.GameObjects;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.Interfaces.Drawable;
import org.csc133.a3.Interfaces.Steerable;

import javax.swing.tree.FixedHeightLayoutCache;
import java.util.ArrayList;

import static com.codename1.ui.CN.*;

public class Helicopter extends Movable implements Steerable, Drawable {

    private static final String[] HELI_LABELS = new String[2];
    private final HeliPad hp;
    private final River r;
    public int MAX_SPEED = 10;
    private final int DISP_H;
    private final int DISP_W;
    private ArrayList<GameObject> heliParts;
    private HeliBlade heliBlade;
    private static double heliBladeRotation;
    private static int HELI_TRANS_X, HELI_TRANS_Y;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static final int BUBBLE_RADIUS = 125;

    private static class HeliBubble extends GameObject {
        public HeliBubble() {
            setDimensions(new Dimension(2 * BUBBLE_RADIUS,
                                        2 * BUBBLE_RADIUS));
        }

        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            containerTranslate(g, parentOrigin);

            g.drawArc(-getDimensionsW() / 2, -getDimensionsH() / 2,
                    getDimensionsW(), getDimensionsH(),
                    -135, -270);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static final int ENGINE_BLOCK_W = (int) (BUBBLE_RADIUS * 1.8);
    private static final int ENGINE_BLOCK_H = ENGINE_BLOCK_W / 3;

    private static class HeliEngineBlock extends GameObject {
        public HeliEngineBlock() {
            setDimensions(new Dimension(ENGINE_BLOCK_W, ENGINE_BLOCK_H));
        }


        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            containerTranslate(g, parentOrigin);

            g.drawRect(-getDimensionsW() / 2, -getDimensionsH() / 2,
                    getDimensionsW(), getDimensionsH());
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static final int TAIL_HEIGHT = 75;
    private static final int TAIL_WIDTH = 65;

    private static class HeliTailBody extends GameObject {
        public HeliTailBody() {
            setDimensions(new Dimension(TAIL_WIDTH, TAIL_HEIGHT));
        }

        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            containerTranslate(g, parentOrigin);

            drawBody(g);
            drawTailDetails(g);
            drawPole(g);
        }

        private void drawBody(Graphics g) {
            g.drawLine(-getDimensionsW() / 2, -getDimensionsH(),
                    -getDimensionsW() / 3, getDimensionsH() + 25);
            g.drawLine(getDimensionsW() / 2, -getDimensionsH(),
                    getDimensionsW() / 3, getDimensionsH() + 25);
        }

        private void drawPole(Graphics g) {

            g.setColor(ColorUtil.GRAY);
            g.fillRect(-getDimensionsW() / 16, -getDimensionsH(),
                    getDimensionsW() / 6,
                    (getDimensionsH() * 2) + 25);
        }

        private void drawTailDetails(Graphics g) {
            g.drawLine(-getDimensionsW() / 2, -getDimensionsH(),
                    (int) (getDimensionsW() / 2.7), getDimensionsH() / 2);
            g.drawLine(getDimensionsW() / 2, -getDimensionsH(),
                    (int) (-getDimensionsW() / 2.7), getDimensionsH() / 2);

            g.drawLine((int) (-getDimensionsW() / 3.5),getDimensionsH() / 2,
                    getDimensionsW() / 3, getDimensionsH() + 25);
            g.drawLine((int) (getDimensionsW() / 3.5), getDimensionsH() / 2,
                    -getDimensionsW() / 3, getDimensionsH() + 25);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static class HeliTailRotator extends GameObject {
        public HeliTailRotator() {
            setDimensions(45, 45);
        }

        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            containerTranslate(g, parentOrigin);

            drawRotatorBoxOnLeft(g);
            drawBoxToBladeConnector(g);
            drawRotatorBox(g);
            drawRotatorBlade(g);
        }

        private void drawRotatorBox(Graphics g) {
            g.setColor(ColorUtil.GRAY);
            g.fillRect(-getDimensionsW() / 2, -getDimensionsH() / 2,
                    getDimensionsW(), getDimensionsH());
        }

        private void drawBoxToBladeConnector(Graphics g) {
            g.setColor(ColorUtil.YELLOW);
            g.fillRect(getDimensionsW() / 2, -getDimensionsH() / 4,
                    getDimensionsW() - 15, getDimensionsH() / 2);
        }

        private void drawRotatorBlade(Graphics g) {
            g.setColor(ColorUtil.GRAY);
            int BLADE_LENGTH = 100;
            g.fillRect(getDimensionsW(), -getDimensionsH(),
                    getDimensionsW() / 4, BLADE_LENGTH);
        }

        private void drawRotatorBoxOnLeft(Graphics g) {
            g.setColor(ColorUtil.YELLOW);
            g.drawRect(-getDimensionsW() - 20, -getDimensionsH() / 2 + 10,
                    getDimensionsW(), getDimensionsH() - 20);
        }

    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static class HeliSkids extends GameObject {
        private static final int SKID_WIDTH = 20;
        private static final int SKID_HEIGHT = 100;

        public HeliSkids() {
            setDimensions(SKID_WIDTH, SKID_HEIGHT);

        }

        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {
            containerTranslate(g, parentOrigin);

            drawSkids(g);
            drawSkidConnectors(g);
        }

        private void drawSkids(Graphics g) {
            g.setColor(ColorUtil.YELLOW);
            g.drawRect(150, -600,
                        getDimensionsW(), getDimensionsH() * 3);
            g.drawRect(-170, -600,
                        getDimensionsW(), getDimensionsH() * 3);
        }

        private void drawSkidConnectors(Graphics g) {
            g.setColor(ColorUtil.GRAY);

            g.fillRect(115, -550, 35, 15);
            g.fillRect(115, -375, 35, 15);

            g.fillRect(-145, -550, 35, 15);
            g.fillRect(-145, -375, 35, 15);

        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static class HeliBlade extends GameObject {

        public HeliBlade() {
            setDimensions(15, 500);
            translate(6, -125);
            rotate(heliBladeRotation);
        }

        public void localDraw(Graphics g, Point parentOrigin,
                              Point originScreen) {

            transformsToMakeBladeRotate(g,parentOrigin,originScreen);

            drawBlade(g);
            drawBladeConnector(g);
        }

        private void drawBlade(Graphics g) {
            g.setColor(ColorUtil.GRAY);
            g.fillRect(0, -getDimensionsH() / 2,
                    getDimensionsW(), getDimensionsH());
        }

        private void drawBladeConnector(Graphics g) {
            g.setColor(ColorUtil.BLACK);
            g.fillArc(0, 0,
                    12, 12, 0, 360);

        }

        public void updateLocalTransform(double rotationSpeed) {
            heliBladeRotation += rotationSpeed;
        }
        private void transformsToMakeBladeRotate(Graphics g,
                                                 Point parentOrigin,
                                                 Point originScreen) {
            Transform gxForm = Transform.makeIdentity();
            localTransform(gxForm);
            g.getTransform(gxForm);
            gxForm.translate(-getDimensionsW() / 2f,
                    -getDimensionsH() / 2f);
            gxForm.translate(-parentOrigin.getX(), -parentOrigin.getY());
            gxForm.translate(originScreen.getX(), originScreen.getY());

            gxForm.translate(   myTranslation.getTranslateX(),
                                myTranslation.getTranslateY());
            gxForm.concatenate(myRotation);
            gxForm.scale(myScale.getScaleX(), myScale.getScaleY());

            gxForm.translate(-originScreen.getX(), -originScreen.getY());
            gxForm.translate(parentOrigin.getX(), parentOrigin.getY());
            g.setTransform(gxForm);
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Helicopter(Dimension worldSize) {
        hp = new HeliPad(worldSize);
        r = new River(worldSize);
        DISP_W = worldSize.getWidth();
        DISP_H = worldSize.getHeight();

        setUpHelicopter();
        addAllHeliParts();

        setColor(ColorUtil.YELLOW);
    }

    private void addAllHeliParts()
    {
        heliParts = new ArrayList<>();
        heliParts.add(new HeliBubble());
        heliParts.add(new HeliEngineBlock());
        heliParts.add(new HeliTailBody());
        heliParts.add(new HeliTailRotator());
        heliParts.add(new HeliSkids());
        heliBlade = new HeliBlade();
        heliParts.add(heliBlade);
    }

    private void setUpHelicopter()
    {
        setHeliLocation();
        translate( HELI_TRANS_X , HELI_TRANS_Y);
        scale(.25f, .25f);
        rotate(-Heading());
    }
    @Override
    public void draw(Graphics g, Point containerOrigin,
                         Point originScreen) {
        g.setColor(color);

        g.setTransform(flipGameObjectsAfterVTM( containerOrigin, originScreen));

        for (GameObject go : heliParts) {

            go.localDraw(g, containerOrigin, originScreen);
        }

        drawFuelAndWaterLabels(g);
        g.resetAffine();
    }

    public void updateLocalTransforms() {
        heliBlade.updateLocalTransform(-200d);
    }

    @Override
    public void steerLeft() {
        ChangeDirection(15);
    }

    @Override
    public void steerRight() {
        ChangeDirection(-15);
    }

    public void setHeliLocation() {

        HELI_TRANS_X = (int) (DISP_W / 2.0) -Move()[1];
        HELI_TRANS_Y = (int) (DISP_H - (DISP_H - (DISP_H * .9)))
                                - Move()[0];
    }

    //Checks for if heli is over the River and fire.
    //I put them in the Heli class because the heli is
    //performing the action on the riverDimension/fire so the heli needs
    //to know if it is over them
    public boolean IsHeliOverRiver() {
        Point2D[] riverBounds;
        riverBounds = r.getRiverBounds();

        return ((HELI_TRANS_Y <= riverBounds[0].getY())
                && (HELI_TRANS_Y >= riverBounds[2].getY()));
    }

    public boolean IsHeliOverFire(Point2D[] bounds) {
        Point2D[] fireBounds;
        fireBounds = bounds;

        return ((fireBounds[0].getX() <= HELI_TRANS_X)
                && (fireBounds[1].getY() <= HELI_TRANS_Y)
                && (fireBounds[1].getX() >= HELI_TRANS_X)
                && ((fireBounds[2].getY() >= HELI_TRANS_Y)));

    }

    public boolean CanHeliLand() {
        Point2D[] circleBounds = hp.CircleBounds();

        return Speed() == 0
                && circleBounds[0].getX() <= HELI_TRANS_X
                && circleBounds[1].getY() <= HELI_TRANS_Y
                && circleBounds[1].getX() >= HELI_TRANS_X
                && circleBounds[2].getY() >= HELI_TRANS_Y;
    }

    public void setLabels(int fuel, int water) {
        HELI_LABELS[0] = ("F: " + fuel);
        HELI_LABELS[1] = ("W: " + water);
    }

    private void drawFuelAndWaterLabels(Graphics g) {

        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_SMALL);
        g.setFont(f);
        g.setColor(ColorUtil.YELLOW);
        Transform labelForm = Transform.makeIdentity();
        labelForm.setTranslation(   (int)myTranslation.getTranslateX(),
                                 (int)myTranslation.getTranslateY() + 150);

        g.setTransform(labelForm);

        g.drawString(HELI_LABELS[0],0, 0);
        g.drawString(HELI_LABELS[1],0, 30);

    }

    private float rotationalSpeed() {

        return 0;
    }
}

