package org.csc133.a4.GameObjects.HeliParts;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a4.GameObjects.GameObject;

public class Rectangle extends GameObject {

    private final boolean fill;
    private Rectangle(){
        this.fill = false;
    }

    public Rectangle(int color,
                     int width, int height,
                     float translateX, float translateY,
                     float scaleX, float scaleY,
                     float rotationDegrees,
                     boolean fill) {

        setColor(color);
        setDimensions(new Dimension(width,height));

        this.fill = fill;

        translate(translateX,translateY);
        scale(scaleX,scaleY);
        rotate(rotationDegrees);
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point originScreen) {
        g.setColor(getColor());
        containerTranslate(g);
        if(fill)
        {
            g.fillRect(-getDimensionsW()/2,-getDimensionsH()/2,
                    getDimensionsW(), getDimensionsH());
        }
        else {
            g.drawRect(-getDimensionsW() / 2, -getDimensionsH() / 2,
                    getDimensionsW(), getDimensionsH());
        }
    }

}
