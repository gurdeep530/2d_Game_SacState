package org.csc133.a4.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;


public class NonPlayerHelicopter extends Helicopter{
    private static int DISP_W;
    private static int DISP_H;
    public NonPlayerHelicopter(Dimension worldSize,
                               River river, HeliPad heliPad) {
        super(  ColorUtil.MAGENTA,
                ColorUtil.MAGENTA,
                worldSize,
                river, heliPad);

        DISP_H = worldSize.getHeight();
        DISP_W = worldSize.getWidth();

    }
    @Override
    public void setHeliLocation() {

        HELI_TRANS_X = (int) (DISP_W / 2.0);
        HELI_TRANS_Y = (int) (DISP_H - (DISP_H - (DISP_H * .92)));

    }

}