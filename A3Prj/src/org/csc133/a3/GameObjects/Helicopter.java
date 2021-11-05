package org.csc133.a3.GameObjects;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.GameObjects.HeliParts.Arc;
import org.csc133.a3.GameObjects.HeliParts.Rectangle;
import org.csc133.a3.Interfaces.Drawable;
import org.csc133.a3.Interfaces.Steerable;

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
    private static float rotationSpeed;
    private static int water;
    private static int HELI_TRANS_X, HELI_TRANS_Y;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //Helicopter State Pattern
    //

    HeliState heliState;
    static boolean isOn = false;
    private void changeState(HeliState heliState)
    {
         this.heliState = heliState;
    }
    public boolean getHeliState()
    {
        return isOn;
    }

    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private abstract class HeliState{
        protected Helicopter getHeli()
        {
            return Helicopter.this;
        }
        protected void startOrStopEngine(){}
        protected void accelerate(){}
        protected void decelerate(){}
        protected void steerLeft() {}
        protected void steerRight(){}
        protected int consumeFuel( int fuel)
        {
            return fuel;
        }
        protected void drink(){}
        protected void fight(Fire fire, ArrayList<GameObject> GameObjects){}
        protected boolean hasLandedAt()
        {
            return false;
        }
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private class Off extends HeliState{

        @Override
        protected void startOrStopEngine()
        {
            getHeli().changeState(new Starting());
        }
        @Override
        protected boolean hasLandedAt() {
            return IsHeliOverHeliPad() && Speed() == 0;
        }

    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private class Starting extends HeliState{

        private Starting()
        {
            beginRotatingBlade();
        }
        @Override
        protected void startOrStopEngine()
        {
            isOn = false;
            getHeli().changeState(new Stopping());
        }
        protected void beginRotatingBlade()
        {
            while(rotationSpeed != -10) {
                rotationSpeed--;
                updateLocalTransforms();
            }
        }
        @Override
        protected void accelerate(){
            if(rotationSpeed == -10) {
                getHeli().changeState(new Ready());
            }
        }
        @Override
        protected int consumeFuel(int fuel)
        {
            if(fuel < 0)
                fuel = 0;
            else
                fuel -= .5;
            return fuel;
        }
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private class Stopping extends HeliState{

        protected Stopping()
        {
            stopRotatingBlade();
        }
        @Override
        protected void startOrStopEngine()
        {
            isOn = true;
            getHeli().changeState(new Starting());
        }
        protected void stopRotatingBlade()
        {
            while(rotationSpeed != 0) {
                rotationSpeed++;
                updateLocalTransforms();
            }
            getHeli().changeState(new Off());
        }
        @Override
        protected void decelerate() {
            if(Speed() == 0)
                getHeli().changeState(new Off());
        }
    }

    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private class Ready extends HeliState {

        @Override
        protected void startOrStopEngine() {
            if(Speed() == 0)
                getHeli().changeState(new Stopping());
            else {
                getHeli().changeState(new Ready());
            }
        }
        @Override
        protected void steerLeft()
        {
            ChangeDirection(15);
        }
        @Override
        protected void steerRight()
        {
            ChangeDirection(-15);
        }
        @Override
        protected void accelerate()
        {
            if (Speed() < MAX_SPEED)
                ChangeSpeed(1);
        }
        @Override
        protected void decelerate() {
            if (Speed() > 0)
                ChangeSpeed(-1);
        }
        @Override
        protected void drink()
        {
            if (Speed() < 2
                    && water != 1000
                    && IsHeliOverRiver())
            {
                water += 100;
            }
        }
        @Override
        protected void fight(Fire fire, ArrayList<GameObject> gameObjects)
        {
            int i = 0;
            for(GameObject go: gameObjects) {
                if(go instanceof Fire) {
                    if (IsHeliOverFire(fire.getFireBounds(go))) {
                        fire.shrink(go, water);
                        break;
                    }
                    i = i + 1;
                }
            }
            water = 0;
        }
        @Override
        protected int consumeFuel(int fuel)
        {
            if(fuel < 0)
                fuel = 0;
            else if(Speed() == 0)
            {
                fuel -= .5;
            }
            else
                fuel -= (int) Math.pow(Speed(), 2);

            return fuel;
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //Parts

    private static final int BUBBLE_RADIUS = 125;

    private static class HeliBubble extends Arc {
        public HeliBubble() {
            super(  ColorUtil.YELLOW,
                    2*BUBBLE_RADIUS,2*BUBBLE_RADIUS,
                    0, (float) (BUBBLE_RADIUS *.80),
                    1,1,
                    0,
                    -135,-270,
                    false);
        }

    }

    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private static final int ENGINE_BLOCK_W = (int) (BUBBLE_RADIUS * 1.8);
    private static final int ENGINE_BLOCK_H = ENGINE_BLOCK_W / 3;

    private static class HeliEngineBlock extends Rectangle {
        public HeliEngineBlock() {
            super(  ColorUtil.YELLOW,
                    ENGINE_BLOCK_W,ENGINE_BLOCK_H,
                    0,ENGINE_BLOCK_H *3,
                    1,1,
                    0,
                    false);
        }
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private static final int TAIL_HEIGHT = 75;
    private static final int TAIL_WIDTH = 65;
    private static final int TAIL_Y_LOC = (int) (ENGINE_BLOCK_H*4.5f);

    private static class HeliTailBody extends GameObject {
        public HeliTailBody() {
            setDimensions(new Dimension(TAIL_WIDTH, TAIL_HEIGHT));
            translate(0,TAIL_Y_LOC);
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
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private static class HeliTailRotator extends GameObject {
        public HeliTailRotator() {
            setDimensions(45, 45);
            translate(0,TAIL_Y_LOC + TAIL_HEIGHT + 25);
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
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private static final int SKID_WIDTH = 20;
    private static final int SKID_HEIGHT = 300;
    private static final int SKID_X_LOC = 160;
    private static class HeliRightSkid extends Rectangle {

        public HeliRightSkid() {
            super(ColorUtil.YELLOW,
                    SKID_WIDTH, SKID_HEIGHT,
                    SKID_X_LOC, ENGINE_BLOCK_H * 2,
                    1, 1,
                    0,
                    false);
        }
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private static class HeliLeftSkid extends Rectangle{

        public HeliLeftSkid() {
            super(  ColorUtil.YELLOW,
                    SKID_WIDTH, SKID_HEIGHT,
                    -SKID_X_LOC, ENGINE_BLOCK_H * 2,
                    1, 1,
                    0,
                    false);
        }
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private static final int CONNECTOR_WITDH = 35;
    private static final int CONNECTOR_HEIGHT = 15;
    private static class HeliLeftBottomSkidConnector extends Rectangle{

        public HeliLeftBottomSkidConnector()
        {
            super(  ColorUtil.GRAY,
                    CONNECTOR_WITDH, CONNECTOR_HEIGHT,
                    -SKID_X_LOC/1.25f, ENGINE_BLOCK_H *3,
                    1, 1,
                    0,
                    true);
        }
    }
    private static class HeliLeftTopSkidConnector extends Rectangle{

        public HeliLeftTopSkidConnector()
        {
            super(  ColorUtil.GRAY,
                    (int) (CONNECTOR_WITDH/1.25), CONNECTOR_HEIGHT,
                    -SKID_X_LOC/1.18f, ENGINE_BLOCK_H,
                    1, 1,
                    0,
                    true);
        }
    }

    private static class HeliRightBottomSkidConnector extends Rectangle{

        public HeliRightBottomSkidConnector()
        {
            super(  ColorUtil.GRAY,
                    CONNECTOR_WITDH, CONNECTOR_HEIGHT,
                    SKID_X_LOC/1.25f, ENGINE_BLOCK_H *3,
                    1, 1,
                    0,
                    true);
        }
    }
    private static class HeliRightTopSkidConnector extends Rectangle{

        public HeliRightTopSkidConnector()
        {
            super(  ColorUtil.GRAY,
                    (int) (CONNECTOR_WITDH/1.25), CONNECTOR_HEIGHT,
                    SKID_X_LOC/1.18f, ENGINE_BLOCK_H,
                    1, 1,
                    0,
                    true);
        }
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private final static int BLADE_WIDTH = 25;
    private final static int BLADE_LENGTH = BUBBLE_RADIUS;
    private static int rotation = 1;
    private static class HeliBlade extends Rectangle {

        public HeliBlade() {
            super(  ColorUtil.GRAY,
                    BLADE_WIDTH,
                    BLADE_LENGTH*5,
                    0,ENGINE_BLOCK_H*3.75f,
                    1,1,
                    rotation,
                    true);
        }

        public void updateLocalTransform(double rotationSpeed) {
            if(rotationSpeed != 0)
                rotation += (3 * rotationSpeed);
        }
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    final static int SHAFT_WIDTH = 12;
    final static int SHAFT_HEIGHT = 12;
    private static class HeliShaft extends Arc{

        public HeliShaft()
        {
            super(  ColorUtil.BLACK,
                    SHAFT_WIDTH,SHAFT_HEIGHT,
                    0,ENGINE_BLOCK_H*3,
                    1,1,
                    0,
                    0, 360,
                    true);
        }

    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //Helicopter

    public Helicopter(Dimension worldSize) {
        hp = new HeliPad(worldSize);
        r = new River(worldSize);
        DISP_W = worldSize.getWidth();
        DISP_H = worldSize.getHeight();

        setUpHelicopter();
        heliState = new Off();
        addAllHeliParts();

        setColor(ColorUtil.YELLOW);
    }


    //.......................................................................
    //Drawing

    @Override
    public void draw(Graphics g, Point containerOrigin,
                         Point originScreen) {
        g.setColor(color);

        localDraw(g,containerOrigin,originScreen);

        g.resetAffine();
    }

    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                          Point originScreen) {
        for (GameObject go : heliParts) {
            g.setTransform
                    (flipGameObjectsAfterVTM(containerOrigin, originScreen));
            go.localDraw(g, containerOrigin, originScreen);
        }

        drawFuelAndWaterLabels(g);
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

    public void updateLocalTransforms() {
        heliBlade.updateLocalTransform(rotationSpeed);
    }

    //.........................................................................
    //Setting up parts for Helicopter.
    private void addAllHeliParts()
    {
        heliParts = new ArrayList<>();
        heliParts.add(new HeliBubble());
        heliParts.add(new HeliEngineBlock());
        heliParts.add(new HeliTailBody());
        heliParts.add(new HeliTailRotator());
        heliParts.add(new HeliRightSkid());
        heliParts.add(new HeliRightTopSkidConnector());
        heliParts.add(new HeliRightBottomSkidConnector());
        heliParts.add(new HeliLeftSkid());
        heliParts.add(new HeliLeftTopSkidConnector());
        heliParts.add(new HeliLeftBottomSkidConnector());
        heliBlade = new HeliBlade();
        heliParts.add(heliBlade);
        heliParts.add(new HeliShaft());

    }

    private void setUpHelicopter()
    {
        setHeliLocation();
        translate( HELI_TRANS_X , HELI_TRANS_Y);
        scale(.25f, .25f);
        rotate(-Heading());
    }
    public void setHeliLocation() {

        HELI_TRANS_X = (int) (DISP_W / 2.0) -Move()[1];
        HELI_TRANS_Y = (int) (DISP_H - (DISP_H - (DISP_H * .92)))
                                - Move()[0];
    }

    public void setLabels(int fuel) {
        HELI_LABELS[0] = ("F: " + fuel);
        HELI_LABELS[1] = ("W: " + water);
    }

    //..........................................................................
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

    public boolean IsHeliOverHeliPad() {
        Point2D[] circleBounds = hp.CircleBounds();

        return Speed() == 0
                && circleBounds[0].getX() <= HELI_TRANS_X
                && circleBounds[1].getY() <= HELI_TRANS_Y
                && circleBounds[1].getX() >= HELI_TRANS_X
                && circleBounds[2].getY() >= HELI_TRANS_Y;
    }


    //..........................................................................
    //Behavior
    public int consumeFuel(int fuel)
    {
        return heliState.consumeFuel(fuel);
    }
    public void startOrStopEngine(){
        heliState.startOrStopEngine();
    }
    public void accelerate()
    {
        heliState.accelerate();
    }
    public void decelerate()
    {
        heliState.decelerate();
    }
    @Override
    public void steerLeft() {
        heliState.steerLeft();
    }
    @Override
    public void steerRight() {
        heliState.steerRight();
    }
    public void drink()
    {
        heliState.drink();
    }
    public void fight(Fire fire, ArrayList<GameObject> gameObjects)
    {
      heliState.fight(fire,gameObjects);
    }
    public Boolean canHeliLand()
    {
        return heliState.hasLandedAt();
    }
}

