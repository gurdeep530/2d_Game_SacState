package org.csc133.a5.Interfaces;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public interface Drawable {

    void draw(Graphics g, Point containerOrigin, Point originScreen);
}
