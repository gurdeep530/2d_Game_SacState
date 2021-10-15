package org.csc133.a2;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.UITimer;
import org.csc133.a2.Commands.*;
import org.csc133.a2.Views.ControlCluster;
import org.csc133.a2.Views.GlassCockpit;
import org.csc133.a2.Views.MapView;


public class Game extends Form implements Runnable {
    private final GameWorld gw;
    private final MapView mv;
    private final GlassCockpit gc;
    private final ControlCluster cc;

    final static int DISP_W = Display.getInstance().getDisplayWidth();
    final static int DISP_H = Display.getInstance().getDisplayHeight();

    public Game() {

        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        this.setLayout(new BorderLayout());

        gw = new GameWorld();
        mv = new MapView(gw);
        gc = new GlassCockpit(gw);
        cc = new ControlCluster(gw);

        addKeyListener('Q', new QuitCommand(gw));
        addKeyListener('d', new DrinkCommand(gw));
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
        gw.tick();
        mv.update();
        gc.update();
    }

}
