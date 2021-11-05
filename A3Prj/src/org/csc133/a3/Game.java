package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.UITimer;
import org.csc133.a3.Commands.*;
import org.csc133.a3.Views.ControlCluster;
import org.csc133.a3.Views.GlassCockpit;
import org.csc133.a3.Views.MapView;


public class Game extends Form implements Runnable {
    private final GameWorld gw;
    private final MapView mv;
    private final GlassCockpit gc;
    private final ControlCluster cc;

    public Game() {

        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        this.setLayout(new BorderLayout());

        gw = new GameWorld();
        mv = new MapView(gw);
        gc = new GlassCockpit(gw);
        cc = new ControlCluster(gw);

        addKeyListener('Q', new QuitCommand(gw));
        addKeyListener('d', new DrinkCommand(gw));
        addKeyListener('s', new StartOrStopEngineCommand(gw));
        addKeyListener('f', new FightCommand(gw));
        addKeyListener(-91, new AccelerateCommand(gw));
        addKeyListener(-92, new DecelerateCommand(gw));
        addKeyListener(-93, new TurnLeftCommand(gw));
        addKeyListener(-94, new TurnRightCommand(gw));


        this.addComponent(BorderLayout.NORTH, gc);
        this.addComponent(BorderLayout.CENTER, mv);
        this.addComponent(BorderLayout.SOUTH, cc);

        this.show();

        UITimer timer = new UITimer(this);
        timer.schedule(100, true, this);
    }

    @Override
    public void run() {
        mv.update();
        gw.tick();
        gc.update();
        cc.update();
    }

}
