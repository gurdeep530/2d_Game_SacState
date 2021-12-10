package org.csc133.a5.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;

public class PlayerHelicopter extends Helicopter{
    private static int DISP_W;
    private static int DISP_H;

    public PlayerHelicopter(Dimension worldSize, River river, HeliPad heliPad) {

        super(ColorUtil.YELLOW,
                ColorUtil.YELLOW,
                worldSize,
                river, heliPad);

        DISP_H = worldSize.getHeight();
        DISP_W = worldSize.getWidth();
    }

    @Override
    public void setHeliLocation() {
        HELI_TRANS_X = (int) (DISP_W / 2.0)
                - Move()[1];
        HELI_TRANS_Y = (int) (DISP_H - (DISP_H - (DISP_H * .92)))
                - Move()[0];

    }

}
