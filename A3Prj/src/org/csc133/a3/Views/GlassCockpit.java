package org.csc133.a3.Views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.GridLayout;
import org.csc133.a3.GameWorld;

public class GlassCockpit extends Container {
    private final GameWorld gw;
    private final Label heading;
    private final Label fires;
    private final Label firesSizes;
    private final Label speed;
    private final Label fuel;
    private final Label damages;
    private final Label loss;

    public GlassCockpit(GameWorld gw)
    {
        this.gw = gw;

        this.setLayout(new GridLayout(2,7));

        this.getAllStyles().setBgColor(ColorUtil.WHITE);
        this.getAllStyles().setBgTransparency(255);

        this.add("Heading");
        this.add("Speed");
        this.add("Fuel");
        this.add("Fires");
        this.add("Fire Sizes");
        this.add("Damage");
        this.add("Loss");

        heading = new Label("0");
        fires = new Label("0");
        speed = new Label("0");
        firesSizes = new Label("0");
        fuel = new Label("0");
        damages = new Label("0");
        loss = new Label("0");

        this.add(heading);
        this.add(speed);
        this.add(fuel);
        this.add(fires);
        this.add(firesSizes);
        this.add(damages);
        this.add(loss);

    }

    public void update()
    {
        fires.setText(String.valueOf(gw.getNumberOfFiresForGlassCockpit()));
        firesSizes.setText(String.valueOf(gw.getFireSizesForGlassCockpit()));
        speed.setText(String.valueOf(gw.getSpeedForGlassCockpit()));
        heading.setText(String.valueOf(gw.getHeadingForGlassCockpit()));
        fuel.setText(String.valueOf(gw.getFuelGlassCockpit()));
        damages.setText(String.valueOf(gw.getDamagesForGlassCockpit()));
        loss.setText(String.valueOf(gw.getLossesForGlassCockpit()));
    }
}
