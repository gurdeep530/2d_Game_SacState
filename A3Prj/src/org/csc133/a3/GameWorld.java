package org.csc133.a3;

import com.codename1.ui.*;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.layouts.BorderLayout;
import org.csc133.a3.GameObjects.*;

import java.util.ArrayList;

public class GameWorld {
    private Dimension worldSize;
    private int ticker;
    private Fire f;
    private Helicopter h;
    private Building b;
    private static int HELI_FUEL;
    private static int counter = 0;
    private final ArrayList<GameObject> GAME_OBJECTS = new ArrayList<>();


    public GameWorld(){
        worldSize = new Dimension();
    }

    public void init() {
        ticker = 0;
        HELI_FUEL = 25000;

        h = new Helicopter(worldSize);
        f = new Fire();


        GAME_OBJECTS.add(new HeliPad(worldSize));
        GAME_OBJECTS.add(new River(worldSize));

        for(int i = 1; i<= 3; i++) {
            b = new Building(i, worldSize);
            GAME_OBJECTS.add(b);
            for (int j = 0; j < 3; j++) {
                f = new Fire(b.setFireInBuilding(f,i), worldSize);
                GAME_OBJECTS.add(f);
            }
        }
        GAME_OBJECTS.add(h);
        h.setLabels(HELI_FUEL);
    }

    public void quit() {
        Display.getInstance().exitApplication();
    }

    public void tick() {
        if (HELI_FUEL > 0) {

            HELI_FUEL = h.consumeFuel(HELI_FUEL);

            h.setLabels(HELI_FUEL);

            h.setHeliLocation();

            GAME_OBJECTS.set(GAME_OBJECTS.size()-1,new Helicopter(worldSize));

            ticker++;

            if ((ticker == 30)) {
                ticker = 0;
                f.grow(getGameObjectCollection());
                b.buildingDamages(f.FireSizes());
            }
        }
        if (HELI_FUEL <= 0) {
            getFuelLosingMenu();
        }
        else if(getDamagesForGlassCockpit() >= 100)
        {
            getDamageLosingMenu();
        }
        else if (f.areFiresOut() && h.canHeliLand())
        {
            getWinningMenu();
        }
    }

    public void Drink() {
        h.drink();
    }

    public void Fight() {
       h.fight(f, getGameObjectCollection());
    }

    public void StartOrStopEngine()
    {
        h.startOrStopEngine();
    }

    public void Accelerate() {
        h.accelerate();
    }

    public void Decelerate() {
        h.decelerate();
    }

    public void turnLeft() {
        h.steerLeft();
    }

    public void turnRight() {
        h.steerRight();
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

    public ArrayList<GameObject> getGameObjectCollection()
    {
        return GAME_OBJECTS;
    }
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
        return h.Speed();
    }
    public int getHeadingForGlassCockpit()
    {
        return h.Heading();
    }
    public int getFuelGlassCockpit()
    {
        return HELI_FUEL;
    }
    public int getDamagesForGlassCockpit()
    {
        int totalDam = 0;
        int[] Dams = b.getBuildingDamage();
        for(int i = 1; i<= 3; i++)
        {
            totalDam += Dams[i];
        }
        totalDam = totalDam/3;

        if(totalDam > 100)
            totalDam = 100;

        return totalDam;
    }
    public int getLossesForGlassCockpit()
    {
        int totalLoss = 0;
        int[] Values = b.getBuildingValue();
        for(int i = 1; i<= 3; i++)
        {
            totalLoss += Values[i];
        }
        totalLoss = (totalLoss * getDamagesForGlassCockpit()) /100;

        return totalLoss;
    }
    public void setDimension(Dimension worldSize) {
        this.worldSize = worldSize;
    }

    public void updateLocalTransforms()
    {
        h.updateLocalTransforms();
    }

    public boolean getHeliState()
    {
        if(counter == 0) {
            counter++;
            return false;
        }
        else {
            return h.getHeliState();
        }
    }
}
