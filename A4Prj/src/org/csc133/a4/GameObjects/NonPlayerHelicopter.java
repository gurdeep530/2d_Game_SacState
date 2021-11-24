package org.csc133.a4.GameObjects;


import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;

public class NonPlayerHelicopter extends Helicopter{

    public NonPlayerHelicopter(Dimension worldSize,
                               River river, HeliPad heliPad) {
        super(ColorUtil.MAGENTA,ColorUtil.MAGENTA,worldSize, river, heliPad);
    }

}