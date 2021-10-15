package org.csc133.a2.Views;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a2.GameObjects.*;
import org.csc133.a2.GameWorld;


public class MapView extends Container{

    GameWorld gw;
    private static int counter = 0;

    public MapView(GameWorld gw)
    {
        this.gw = gw;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        for(GameObject go: gw.getGameObjectCollection()) {
            Point loc = go.convertToPoint(go.getLocation());
            go.draw(g,new Point(loc.getX(), loc.getY()));
        }
    }

    @Override
    public void laidOut(){
        gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
        if(counter == 0) {
            gw.init();
            counter +=1;
        }
    }
    public void update()
    {
       repaint();
    }

}
