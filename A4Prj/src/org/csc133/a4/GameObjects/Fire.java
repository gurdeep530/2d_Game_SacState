package org.csc133.a4.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a4.Interfaces.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.codename1.ui.CN.*;

public class Fire extends Fixed implements Drawable {
    private static final ArrayList<Dimension> FIRE_SIZE = new ArrayList<>();
    private static final ArrayList<Integer> RANDOM_NUM = new ArrayList<>();
    private static final ArrayList<Transform> FIRES = new ArrayList<>();
    private static List<Integer> spawnNewFireKey = new ArrayList<>();
    private int DISP_W;

    private final Random RND = new Random();

    //region Fire patterns

    //region FireDispatch

    private static class FireDispatch
    {
        private final List<Fire> register = new ArrayList<>();

        public void addToRegister(Fire fire)
        {
            this.register.add(fire);
        }
        public void removeFromRegister(Fire fire)
        {
            this.register.remove(fire);
        }

    }



    //endregion

    //region Fire State Pattern
    FireState fireState;
    private void changeState(FireState fireState)
    {
        this.fireState = fireState;
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private abstract class FireState
    {
        protected Fire getFire()
        {
            return Fire.this;
        }
        protected void startFire(Transform fire){}
        protected void grow(ArrayList<GameObject> gameObjects){}
        protected void shrink(GameObject go, int water){}
        protected Boolean areAllFiresOut(){
            boolean firesAreOut = false;
            for(Dimension dim : FIRE_SIZE)
            {
                firesAreOut = dim.getWidth() <= 0;
                if(!firesAreOut)
                    break;
            }
            return firesAreOut;}

    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private class UnStarted extends FireState {
        @Override
        protected void startFire(Transform fire)
        {
            int num = ((int) ((DISP_W * .005) + RND.nextInt(15)));
            RANDOM_NUM.add(changeRandomNumberIfItExistsInTheArray(num));
            int i = RANDOM_NUM.size() - 1;

            setDimensions(new Dimension(RANDOM_NUM.get(i), RANDOM_NUM.get(i)));
            FIRE_SIZE.add(new Dimension(getDimensionsW(),getDimensionsH()));
            FIRES.add(fire);

            getFire().changeState(new Burning());
        }
    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private class Burning extends FireState {
        @Override
        public void shrink(GameObject go, int water) {

            int i = whichFireIsMapViewTryingToDraw(go.myTranslation);

            FIRE_SIZE.get(i).setWidth(FIRE_SIZE.get(i).getWidth() - (water / 5));

            if(FIRE_SIZE.get(i).getWidth() <= 0) {
                FIRE_SIZE.get(i).setWidth(0);
                if(areAllFiresOut()) {
                    getFire().changeState(new Extinguished());
                }
            }
        }
        @Override
        protected void grow(ArrayList<GameObject> gameObjects) {
            int i = 0;
            for(GameObject go: gameObjects) {
                if (go instanceof Fire) {
                    if(FIRE_SIZE.get(i).getWidth() > 0) {
                        FIRE_SIZE.get(i).setWidth(
                                FIRE_SIZE.get(i).getWidth()
                                        + RND.nextInt(5));
                    }
                    i++;
                }
            }
        }

    }
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    private class Extinguished extends FireState {
    }
    //endregion

    //endregion

    //region Fire Class
    public Fire(Fire fire, Dimension worldSize)
    {
        fireState = new UnStarted();
        spawnNewFireKey = generateSpawnKey();
        setDimensions(new Dimension(0,0));
        setColor(ColorUtil.MAGENTA);

        this.DISP_W = worldSize.getWidth();
        startFire(fire.myTranslation);
    }

    public Fire() {
        spawnNewFireKey = generateSpawnKey();
    }

    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    //region Drawing
    @Override
    public void draw(Graphics g, Point containerOrigin, Point screenOrigin)
    {
        localDraw(g,containerOrigin,screenOrigin);
        g.resetAffine();
    }
    @Override
    public void localDraw(Graphics g, Point containerOrigin,
                         Point screenOrigin) {

        Font f = Font.createSystemFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_LARGE);

        g.setFont(f);
        g.setColor(color);

        Transform gFireForm = preLTTransform(g,screenOrigin);
        localTransform(gFireForm);
        postLTTransform(g, screenOrigin,gFireForm);

        g.setTransform(flipGameObjectsAfterVTM( containerOrigin, screenOrigin));

        drawFire(g);

        resetTranformToOrginal(g);

        g.resetAffine();

    }
    private void drawFire(Graphics g)
    {
        int whichFire = whichFireIsMapViewTryingToDraw(myTranslation);
        if(FIRE_SIZE.get(whichFire).getWidth() > 0) {

            drawFireLabel(g,whichFire);

            drawFireArc(g, whichFire);
        }
    }
    private void drawFireArc(Graphics g, int whichFire)
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
    //endregion
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    //region Creating Fires

    private static int CHANCE_FOR_NEW_FIRE = 5;

    private void startFire(Transform fire) {
        fireState.startFire(fire);
    }

    private int changeRandomNumberIfItExistsInTheArray(int num)
    {
        while(RANDOM_NUM.contains(num))
        {
            num +=1;
        }
        return num;
    }

    public Fire spawnNewFire(Building b, Fire f, Dimension worldSize){

        if(matchSpawnKey(generateSpawnKey()))
        {
            CHANCE_FOR_NEW_FIRE++;
            int whichBuilding = RND.nextInt(3-1) + 1;
            Fire fire = new Fire
                            (b.setFireInBuilding(f,whichBuilding),worldSize);
            System.out.println("Match");
            return fire;
        }
        return null;
    }

    private boolean matchSpawnKey(List<Integer> unlockKey)
    {
        return unlockKey.equals(spawnNewFireKey);
    }
    private List<Integer> generateSpawnKey()
    {
        List<Integer> spawnKey = new ArrayList<>();
        int key1 = RND.nextInt(CHANCE_FOR_NEW_FIRE);
        int key2 = RND.nextInt(CHANCE_FOR_NEW_FIRE);
        int key3 =  RND.nextInt(CHANCE_FOR_NEW_FIRE);
        int key4 = RND.nextInt(CHANCE_FOR_NEW_FIRE);

        spawnKey.add(key1);
        spawnKey.add(key2);
        spawnKey.add(key3);
        spawnKey.add(key4);

        return spawnKey;
    }
    //endregion
    //'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    //region Getter for Fire Bounds and Sizes

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

    public ArrayList<Dimension> FireSizes()
    {
        return FIRE_SIZE;
    }

    //endregion
    //''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
    //region Behavior
    public void shrink(GameObject go, int water) {
        fireState.shrink(go,water);
    }

    public void grow(ArrayList<GameObject> gameObjects) {
        fireState.grow(gameObjects);
    }

    public Boolean areFiresOut()
    {
        return fireState.areAllFiresOut();
    }

    //endregion
}
