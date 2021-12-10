package org.csc133.a5.GameObjects.HeliParts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a5.GameObjects.GameObject;

public class Arc extends GameObject {

    private final int startAngle, arcAngle;
    private final boolean fill;

    private Arc(){
        this.startAngle = 0;
        this.arcAngle = 0;
        this.fill = false;
    }

    public Arc( int color,
                int width, int height,
                float translateX, float translateY,
                float scaleX, float scaleY,
                float rotationDegrees,
                int startAngle, int arcAngle,
                boolean fill) {

        setColor(color);
        setDimensions(new Dimension(width,height));

        this.startAngle = startAngle;
        this.arcAngle = arcAngle;
        this.fill = fill;

        translate(translateX,translateY);
        scale(scaleX,scaleY);
        rotate(rotationDegrees);
    }

    @Override
    public void localDraw(Graphics g, Point parentOrigin, Point screenOrigin)
    {
        g.setColor(getColor());
        containerTranslate(g);
        g.drawArc(-getDimensionsW()/2,-getDimensionsH()/2,
                    getDimensionsW(), getDimensionsH(),
                    startAngle,arcAngle);
        if(fill)
        {
            g.fillArc(-getDimensionsW()/2,-getDimensionsH()/2,
                    getDimensionsW(), getDimensionsH(),
                    startAngle,arcAngle);
        }
        else {
            g.drawArc(-getDimensionsW()/2,-getDimensionsH()/2,
                    getDimensionsW(), getDimensionsH(),
                    startAngle,arcAngle);
        }
    }

}
