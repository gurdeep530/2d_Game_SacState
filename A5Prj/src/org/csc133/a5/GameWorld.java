package org.csc133.a5;

import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import org.csc133.a5.GameObjects.*;

import java.util.ArrayList;

public class GameWorld {
    private Dimension worldSize;
    private int ticker;
    private GameObjectCollection goc;
    private Fire f;
    private PlayerHelicopter ph;
    private NonPlayerHelicopter nph;
    private Building b;
    private River r;
    private HeliPad hp;
    private FlightPath fp;
    private int counter;
    public boolean canNPHSpawn;

    public GameWorld(){
        worldSize = new Dimension();
    }

    public void init() {
        ticker = 0;
        counter = 0;
        canNPHSpawn = false;

        r = new River(worldSize);
        hp = new HeliPad(worldSize);
        goc = new GameObjectCollection();
        f = new Fire();


        goc.gameObjectCollection.add(0,null);
        goc.gameObjectCollection.add(1,null);
        goc.gameObjectCollection.add(2,null);
        goc.gameObjectCollection.add(hp);
        goc.gameObjectCollection.add(r);

        for(int i = 1; i<= 3; i++) {
            b = new Building(i, worldSize);
            goc.gameObjectCollection.add(b);
            for (int j = 0; j < 3; j++) {
                f = new Fire(b.setFireInBuilding(f), worldSize);
                goc.gameObjectCollection.add(f);
            }
        }

        nph = new NonPlayerHelicopter(worldSize,r,hp,f,goc);
        nph.setUpHelicopter();
        ph = new PlayerHelicopter(worldSize,r,hp);
        ph.setUpHelicopter();

        goc.gameObjectCollection.add(nph);
        goc.gameObjectCollection.add(ph);

        new Fire(goc);
    }

    public void quit() {
        Display.getInstance().exitApplication();
    }

    public void tick() {
        if (ph.getFuel() > 0) {

            ph.updateHeli();

            ticker++;

            if ((ticker == 30)) {
                ticker = 0;
                f.grow(goc.getFires());
                for(GameObject go : goc.getBuildings()) {
                    b = (Building) go;
                    b.buildingDamages(goc.getFires());
                }
            }

            if(f.getIsAFireSelected()) {
                if(fp == null) {
                    fp = new FlightPath(hp, r, f.getSelectedFire(getFires()));
                    goc.gameObjectCollection.set(1, fp);
                    nph.setHeliLocation(fp);
                }
                nph.updateHeli(fp);

                if(f.getFd().didSelectedFireSwitch())
                    fp.updateFlightPath(hp,r,f.getSelectedFire(getFires()));
            }

            for(GameObject go : goc.getBuildings()){
                    b = (Building) go;
                    if (f.matchSpawnKey(b)) {
                        f = new Fire(f.spawnNewFire(b, f),
                        worldSize);
                        goc.gameObjectCollection.add(2, f);
            }
            }
        }
        whichMenuToDisplay();
    }

    public ArrayList<GameObject> getGameObjectCollection()
    {
        return goc.getGameObjectCollection();
    }

    public ArrayList<GameObject> getFires()
    {
        return goc.getFires();
    }

    public Fire getFireForMapview()
    {
        return f;
    }

    public void FireDispatchSelector(Fire selectedFire)
    {
        f.getFd().whichFireIsSelected(selectedFire);
    }

    //region Commands
    public void Drink() {
        ph.drink();
    }

    public void Fight() {
       ph.fight(f,ph.getWater(),goc.getFires());
    }

    public void StartOrStopEngine()
    {
        ph.startOrStopEngine();
    }

    public void Accelerate() {
        ph.accelerate();
    }

    public void Decelerate() {
        ph.decelerate();
    }

    public void turnLeft() {
        ph.steerLeft();
    }

    public void turnRight() {
        ph.steerRight();
    }

    //endregion

    //region Menus
    private void whichMenuToDisplay(){
        if (ph.getFuel() <= 0) {
            getFuelLosingMenu();
        }
        else if(getDamagesForGlassCockpit() >= 100)
        {
            getDamageLosingMenu();
        }
        else if (f.areFiresOut() && ph.canHeliLand())
        {
            getWinningMenu();
        }
    }
    private void getFuelLosingMenu() {
        Dialog d = new Dialog("Game Over, You Lose!");
        final Button YES = new Button("Play Again?");
        final Button NO = new Button("No, too hard");
        Label REASON = new Label("You ran out of Fuel!");
        YES.addActionListener(evt -> new Game());
        NO.addActionListener(evt -> quit());
        REASON.setUIID("PopupBody");

        d.setLayout(new BorderLayout());
        d.add(BorderLayout.NORTH, REASON);
        d.add(BorderLayout.EAST, YES);
        d.add(BorderLayout.WEST, NO);
        d.show();

    }

    private void getDamageLosingMenu()
    {
        Dialog d = new Dialog("Game Over, You Lose");
        final Button YES = new Button("Play Again?");
        final Button NO = new Button("No, too hard");
        Label REASON = new Label("All buildings are destroyed!");
        YES.addActionListener(evt -> new Game());
        NO.addActionListener(evt -> quit());
        REASON.setUIID("PopupBody");

        d.setLayout(new BorderLayout());
        d.add(BorderLayout.NORTH, REASON);
        d.add(BorderLayout.EAST, YES);
        d.add(BorderLayout.WEST, NO);
        d.show();
    }
    private void getWinningMenu() {
        Dialog d = new Dialog("WINNER!");
        final Button YES = new Button("Play Again?");
        final Button NO = new Button("No, too hard");
        Label SCORE = new Label
                ("Score: " + (100 - getDamagesForGlassCockpit()));


        YES.addActionListener(evt -> new Game());
        NO.addActionListener(evt -> quit());
        SCORE.setUIID("PopupBody");

        d.setLayout(new BorderLayout());
        d.add(BorderLayout.NORTH, SCORE);
        d.add(BorderLayout.EAST, YES);
        d.add(BorderLayout.WEST, NO);
        d.show();
    }

    //endregion

    //region ControlCluster Values
    public int getNumberOfFiresForGlassCockpit()
    {
        int fireSize = 0;
        for(int i = 0; i <  f.FireSizes().size() - 1; i++)
        {
            if(f.FireSizes().get(i).getWidth() > 0)
            {
                fireSize += 1;
            }

        }
        return fireSize;
    }
    public int getFireSizesForGlassCockpit()
    {
        int fireSize = 0;
        for(int i = 0; i < getNumberOfFiresForGlassCockpit(); i++)
        {
            fireSize += f.FireSizes().get(i).getWidth();
        }
        return fireSize;
    }
    public int getSpeedForGlassCockpit()
    {
        return ph.getSpeed();
    }
    public int getHeadingForGlassCockpit()
    {
        return ph.getHeading();
    }
    public int getFuelGlassCockpit()
    {
        return ph.getFuel();
    }
    public int getDamagesForGlassCockpit()
    {
        int totalDam = 0;
        for(GameObject go : goc.getBuildings()){
            b = (Building) go;
            totalDam += b.getBuildingDamage();
        }
        totalDam = totalDam/3;

        if(totalDam > 100)
            totalDam = 100;

        return totalDam;
    }
    public int getLossesForGlassCockpit()
    {
        int totalLoss = 0;
        for(GameObject go : goc.getBuildings()){
            b = (Building) go;
            totalLoss += b.getBuildingValue();
        }
        totalLoss = (totalLoss * getDamagesForGlassCockpit()) /100;

        return totalLoss;
    }
    public void setDimension(Dimension worldSize) {
        this.worldSize = worldSize;
    }

    public void updateLocalTransforms()
    {
        ph.updateLocalTransforms();
    }

    public boolean getHeliState()
    {
        if(counter == 0) {
            counter++;
            return false;
        }
        else {
            return ph.getHeliState();
        }
    }

    //endregion
}
