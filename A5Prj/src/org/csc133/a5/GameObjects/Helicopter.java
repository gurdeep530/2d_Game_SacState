package org.csc133.a5.GameObjects;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a5.GameObjects.HeliParts.Arc;
import org.csc133.a5.GameObjects.HeliParts.Rectangle;
import org.csc133.a5.Interfaces.Steerable;

import java.util.ArrayList;

import static com.codename1.ui.CN.*;

public class Helicopter extends Movable implements Steerable {

    private final HeliPad hp;
    private final River r;
    public int MAX_SPEED = 10;
    private ArrayList<GameObject> heliParts;
    private HeliBladeLeft heliBladeLeft;
    private HeliBladeRight heliBladeRight;
    private float rotationSpeed;
    private int water;
    private int fuel;
    public int HELI_TRANS_X, HELI_TRANS_Y;
    private final int DISP_H;
    private final int DISP_W;
    private int oldHeading = 0;

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //region Helicopter State Pattern
    //

    HeliState heliState;
    boolean isOn;


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
        protected int drink(){return 0;}
        protected GameObject fight(Fire fire,
                                   int water, ArrayList<GameObject> GameObjects)
        {return null;}
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
            return IsHeliOverHeliPad() && getSpeed() == 0;
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
                heliBladeRight.updateLocalTransform(rotationSpeed);
                heliBladeLeft.updateLocalTransform(rotationSpeed);
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
                heliBladeLeft.updateLocalTransform(rotationSpeed);
                heliBladeRight.updateLocalTransform(rotationSpeed);
            }
            getHeli().changeState(new Off());
        }
        @Override
        protected void decelerate() {
            if(getSpeed() == 0)
                getHeli().changeState(new Off());
        }
    }

    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private class Ready extends HeliState {

        protected Ready()
        {
            spinBlade();
        }
        private void spinBlade()
        {
            heliBladeLeft.updateLocalTransform(100);
            heliBladeRight.updateLocalTransform(100);
        }
        @Override
        protected void startOrStopEngine() {
            if(getSpeed() == 0)
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
            if (getSpeed() < MAX_SPEED)
                ChangeSpeed(1);
        }
        @Override
        protected void decelerate() {
            if (getSpeed() > 0)
                ChangeSpeed(-1);
        }
        @Override
        protected int drink()
        {
            if (getSpeed() < 2
                    && getWater() != 1000
                    && IsHeliOverRiver())
            {
                return 100;
            }
            return 0;
        }
        @Override
        protected GameObject fight(Fire fire,
                                   int water, ArrayList<GameObject> gameObjects)
        {
            for(GameObject go: gameObjects) {
                if (IsHeliOverFire(fire.getFireBounds(go))) {
                        fire = (Fire) fire.shrink(go, water);
                        break;
                }
            }
            Helicopter.this.water = 0;
            return fire;
        }
        @Override
        protected int consumeFuel(int fuel)
        {
            if(fuel < 0)
                fuel = 0;
            else if(getSpeed() == 0)
            {
                fuel -= .5;
            }
            else
                fuel -= (int) Math.pow(getSpeed(), 2);

            return fuel;
        }
    }
    //endregion
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //region HeliParts
    private static final int BUBBLE_RADIUS = 125;

    private static class HeliBubble extends Arc {
        public HeliBubble(int color) {
            super(  color,
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
        public HeliEngineBlock(int color) {
            super(  color,
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
            containerTranslate(g);

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
            containerTranslate(g);

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
    private static class HeliBladeLeft extends Rectangle
    {

        public HeliBladeLeft() {
            super(  ColorUtil.GRAY,
                    BLADE_WIDTH,
                    BLADE_LENGTH*4,
                    0,ENGINE_BLOCK_H*3.75f,
                    1,1,
                    42,
                    true);
        }
        public void updateLocalTransform(double rotationSpeed) {
            this.rotate(-rotationSpeed);
        }
    }
    private static class HeliBladeRight extends Rectangle
    {
        public HeliBladeRight() {
            super(  ColorUtil.GRAY,
                    BLADE_WIDTH,
                    BLADE_LENGTH *4,
                    0,ENGINE_BLOCK_H*3.75f,
                    1,-1,
                    42,
                    true);
        }
        public void updateLocalTransform(double rotationSpeed) {
            this.rotate(-rotationSpeed);
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

    //endregion
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //region Helicopter

    public Helicopter(int bubbleColor,
                      int engineColor,
                      Dimension worldSize,
                      River river,
                      HeliPad heliPad) {
        hp = heliPad;
        r = river;
        DISP_W = worldSize.getWidth();
        DISP_H = worldSize.getHeight();
        setUpHelicopter();
        isOn = false;
        water = 0;
        fuel = 25000;
        heliState = new Off();
        heliParts = addAllHeliParts(bubbleColor,engineColor);

        setColor(ColorUtil.YELLOW);
    }
    //.......................................................................
    //region Drawing

    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                          Point originScreen) {
        for (GameObject go : heliParts) {
            g.setTransform
                    (flipGameObjectsAfterVTM(containerOrigin, originScreen));
            go.localDraw(g, containerOrigin, originScreen);

        }

        drawFuelAndWaterLabels(g);
        g.resetAffine();
    }
    private void drawFuelAndWaterLabels(Graphics g) {

        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_SMALL);
        g.setFont(f);
        g.setColor(ColorUtil.YELLOW);
        Transform labelForm = Transform.makeIdentity();
        labelForm.setTranslation((int)myTranslation.getTranslateX(),
                (int)myTranslation.getTranslateY() + 150);

        g.setTransform(labelForm);

        g.drawString("F: " + fuel ,0, 0);
        g.drawString("W: " + water,0, 30);

    }
    public void updateHeli()
    {
        HELI_TRANS_X = -Move()[1];
        HELI_TRANS_Y = -Move()[0];
        translate(HELI_TRANS_X, HELI_TRANS_Y);

        if(oldHeading != getHeading() || getHeading() == 0) {
            rotate(-getHeading() + oldHeading);
            oldHeading = getHeading();
        }
        consumeFuel(fuel);
        setLabels(fuel);
        updateLocalTransforms();
    }

    public void updateLocalTransforms() {
        heliBladeRight.updateLocalTransform(rotationSpeed);
        heliBladeLeft.updateLocalTransform(rotationSpeed);
    }
    //endregion
    //.........................................................................
    //region Setting up parts for Helicopter.
    ArrayList<GameObject> addAllHeliParts(int bubbleColor, int engineColor)
    {
        ArrayList<GameObject>heliParts = new ArrayList<>();
        heliParts.add(new HeliBubble(bubbleColor));
        heliParts.add(new HeliEngineBlock(engineColor));
        heliParts.add(new HeliTailBody());
        heliParts.add(new HeliTailRotator());
        heliParts.add(new HeliRightSkid());
        heliParts.add(new HeliRightTopSkidConnector());
        heliParts.add(new HeliRightBottomSkidConnector());
        heliParts.add(new HeliLeftSkid());
        heliParts.add(new HeliLeftTopSkidConnector());
        heliParts.add(new HeliLeftBottomSkidConnector());
        heliBladeLeft = new HeliBladeLeft();
        heliBladeRight = new HeliBladeRight();
        heliParts.add(heliBladeLeft);
        heliParts.add(heliBladeRight);
        heliParts.add(new HeliShaft());

        return heliParts;

    }

    public void setUpHelicopter()
    {
        setHeliLocation();
        translate(HELI_TRANS_X, HELI_TRANS_Y);
        scale(.5f, .5f);
        rotate(-getHeading());
    }
    public void setHeliLocation() {
        HELI_TRANS_X = (int) (DISP_W / 2.0);
        HELI_TRANS_Y = (int) (DISP_H - (DISP_H - (DISP_H * .92)));
    }

    public void setLabels(int fuel) {
      this.fuel= fuel;
    }
    public int getWater()
    {
        return water;
    }
    public int getFuel()
    {
        return fuel;
    }
    public void setFuel(int fuel)
    {
        this.fuel = fuel;
    }
    public Point2D[] getHeliHitBox()
    {
        Point2D[] hitBox = new Point2D[4];

        //top left
        hitBox[0] = new Point2D(myTranslation.getTranslateX() - ENGINE_BLOCK_H,
                (myTranslation.getTranslateY()+50) - ENGINE_BLOCK_H);
        //top right
        hitBox[1] = new Point2D(hitBox[0].getX() + ENGINE_BLOCK_H*2,
                hitBox[0].getY());
        //bottom left
        hitBox[2] = new Point2D(hitBox[0].getX(),
                hitBox[0].getY()+ ENGINE_BLOCK_H*2);
        //bottom right
        hitBox[3] = new Point2D(hitBox[0].getX() + ENGINE_BLOCK_H*2,
                hitBox[0].getY() + ENGINE_BLOCK_H*2);


        return hitBox;
    }
    //endregion
    //..........................................................................
    //region Checks for if heli is over the River and fire.
    public boolean IsHeliOverRiver() {
        Point2D[] riverBounds;
        riverBounds = r.getRiverBounds();

        return (getY() <= riverBounds[0].getY())
                && (getY() >= riverBounds[2].getY());
    }

    public boolean IsHeliOverFire(Point2D[] bounds) {

        return IsPointInsideBounds(bounds, getX(), getY());

    }

    public boolean IsHeliOverHeliPad() {
        Point2D[] circleBounds = hp.CircleBounds();

        return getSpeed() == 0
                && IsPointInsideBounds(circleBounds, getX(), getY());
    }
    //endregion
    //..........................................................................
    //region Behavior
    public void consumeFuel(int fuel)
    {
        setFuel(heliState.consumeFuel(fuel));
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
        water += heliState.drink();
    }
    public void fight(Fire fire,int water,ArrayList<GameObject> gameObjects)
    {
      heliState.fight(fire,water, gameObjects);
    }
    public Boolean canHeliLand()
    {
        return heliState.hasLandedAt();
    }

    //endregion

    //endregion

}

