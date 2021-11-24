package org.csc133.a4.GameObjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;

public class PlayerHelicopter extends Helicopter{

    public PlayerHelicopter(Dimension worldSize, River river, HeliPad heliPad) {
        super(ColorUtil.YELLOW,ColorUtil.YELLOW,
                worldSize, river, heliPad);
    }
}
