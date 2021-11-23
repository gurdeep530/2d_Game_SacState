package org.csc133.a4.Views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import org.csc133.a4.Commands.*;
import org.csc133.a4.GameWorld;

public class ControlCluster extends Container {

    GameWorld gw;
    private final Container centerContainer = new Container(new BorderLayout());
    private final Container leftContainer = new Container(new BorderLayout());
    private final Container rightContainer = new Container(new BorderLayout());
    private final Button leftBtn = new Button();
    private final Button rightBtn = new Button();
    private final Button fightBtn = new Button();
    private final Button exitBtn = new Button();
    private final Button drinkBtn = new Button();
    private final Button brakeBtn = new Button();
    private final Button accelBtn = new Button();
    private final Button engineBtn = new Button();

    public ControlCluster(GameWorld gw) {
        this.gw = gw;
        this.getAllStyles().setBgColor(ColorUtil.WHITE);
        this.getAllStyles().setBgTransparency(255);


        setLayout(new BorderLayout());

        setupButtonCommands();
        setupButtonLook();

        makeLeftContainer();
        makeCenterContainer();
        makeRightContainer();

        this.add(BorderLayout.WEST,leftContainer);

        this.add(BorderLayout.CENTER, centerContainer);

        this.add(BorderLayout.EAST,rightContainer);
    }

    private void setupButtonCommands()
    {
        leftBtn.setCommand(new TurnLeftCommand(gw));
        leftBtn.setText("Left");

        rightBtn.setCommand(new TurnRightCommand(gw));
        rightBtn.setText("Right");

        fightBtn.setCommand(new FightCommand(gw));
        fightBtn.setText("Fight");

        engineBtn.setCommand(new StartOrStopEngineCommand(gw));
        setEngineBtnText();

        exitBtn.setCommand(new QuitCommand(gw));
        exitBtn.setText("Exit");

        drinkBtn.setCommand(new DrinkCommand(gw));
        drinkBtn.setText("Drink");

        brakeBtn.setCommand(new DecelerateCommand(gw));
        brakeBtn.setText("Brake");

        accelBtn.setCommand(new AccelerateCommand(gw));
        accelBtn.setText("Accel");
    }

    private void makeRightContainer()
    {
        rightContainer.add(BorderLayout.WEST,drinkBtn);
        rightContainer.add(BorderLayout.CENTER,brakeBtn);
        rightContainer.add(BorderLayout.EAST,accelBtn);

    }
    private void makeLeftContainer()
    {
        leftContainer.add(BorderLayout.WEST,leftBtn);
        leftContainer.add(BorderLayout.CENTER,rightBtn);
        leftContainer.add(BorderLayout.EAST,fightBtn);
    }
    private void makeCenterContainer()
    {
        centerContainer.getAllStyles().setPaddingLeft(500);
        centerContainer.getAllStyles().setPaddingRight(500);
        centerContainer.add(BorderLayout.EAST,exitBtn);
        centerContainer.add(BorderLayout.WEST,engineBtn);
    }
    private void setupButtonLook()
    {
        buttonLookHelper(leftBtn);
        buttonLookHelper(rightBtn);
        buttonLookHelper(fightBtn);

        buttonLookHelper(engineBtn);
        buttonLookHelper(exitBtn);

        buttonLookHelper(drinkBtn);
        buttonLookHelper(brakeBtn);
        buttonLookHelper(accelBtn);
    }

    private void buttonLookHelper(Button btn)
    {
        Style button = btn.getAllStyles();
        button.setBgTransparency(200);
        button.setBgColor(ColorUtil.LTGRAY);
        button.setFgColor(ColorUtil.BLUE);
    }

    private void setEngineBtnText()
    {
        if(!gw.getHeliState()) {
            engineBtn.setText("Start Engine");
        }
        else
            engineBtn.setText("Stop Engine");

    }

    public void update()
    {
        setupButtonCommands();
    }

}
