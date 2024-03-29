package org.csc133.a5.Views;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a5.GameObjects.*;
import org.csc133.a5.GameWorld;


public class MapView extends Container{

    GameWorld gw;
    private int counter;



    public MapView(GameWorld gw)
    {
        this.gw = gw;
        counter = 0;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        if(gw.getGameObjectCollection() != null) {
            for (GameObject go : gw.getGameObjectCollection()) {
                setupVTM(g);
                Point parentOrigin = new Point(this.getX(), this.getY());
                Point screenOrigin = new Point(getAbsoluteX(), getAbsoluteY());
                if (go instanceof NonPlayerHelicopter) {
                    if (gw.canNPHSpawn)
                        go.localDraw(g, parentOrigin, screenOrigin);
                } else if (go != null)
                    go.localDraw(g, parentOrigin, screenOrigin);

            }

            g.resetAffine();
        }
    }

    @Override
    public void laidOut(){
        gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
        if(counter == 0) {
            gw.init();
            counter += 1;
        }
    }

    @Override
    public void pointerPressed(int x, int y)
    {
        Point mouseClick = new Point(x - getParent().getAbsoluteX(),
                                        y - getParent().getAbsoluteY());
        for(GameObject go: gw.getFires())
        {
            if(go.IsPointInsideBounds(gw.getFireForMapview().getFireBounds(go),
                    mouseClick.getX(), mouseClick.getY()))
            {
                gw.canNPHSpawn = true;
                gw.FireDispatchSelector((Fire) go);
            }
        }
    }

    public void update()
    {
        gw.updateLocalTransforms();
        repaint();
    }


    private Transform buildWorldND(float winWidth, float winHeight,
                                   float winLeft, float winBottom)
    {
        Transform transform = Transform.makeIdentity();
        transform.scale((1/winWidth), (1/winHeight));
        transform.translate(-winLeft,-winBottom);
        return transform;
    }

    private Transform buildNDToDisplay(float displayWidth, float displayHeight)
    {
        Transform transform = Transform.makeIdentity();
        transform.translate(0,displayHeight);
        transform.scale(displayWidth, -displayHeight);
        return transform;
    }

    private void setupVTM(Graphics g)
    {
        Transform worldToND, ndToDisplay, theVTM;
        float winBottom;
        float winLeft = winBottom = 0;
        float winRight = this.getWidth();
        float winTop = this.getHeight();

        float winHeight = winTop - winBottom;
        float winWidth = winRight - winLeft;

        worldToND = buildWorldND(winWidth, winHeight, winLeft, winBottom);
        ndToDisplay = buildNDToDisplay(this.getWidth(),this.getHeight());
        theVTM = ndToDisplay.copy();
        theVTM.concatenate(worldToND);

        createTransformForVTM(g,theVTM);
    }
    private void createTransformForVTM(Graphics g,Transform theVTM)
    {
        Transform transform = Transform.makeIdentity();
        g.getTransform(transform);
        transform.translate(getAbsoluteX(),getAbsoluteY());
        transform.concatenate(theVTM);
        transform.translate(-getAbsoluteX(),-getAbsoluteY());
        g.setTransform(transform);
    }

}
