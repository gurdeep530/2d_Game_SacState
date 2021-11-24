package org.csc133.a4.GameObjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a4.Interfaces.Drawable;

import java.util.ArrayList;

public abstract class GameObject implements Drawable {

    final Point2D location;
    int color;
    final Dimension dimension;
    final Transform myTranslation, myRotation, myScale;
    Transform gOrigXForm;
    public GameObject()
    {
        location = new Point2D(0.0,0.0);
        dimension = new Dimension(0,0);

        myTranslation = Transform.makeIdentity();
        myRotation = Transform.makeIdentity();
        myScale = Transform.makeIdentity();
        scale(1,1);
    }

    public Transform getMyTranslation()
    {
        return myTranslation;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public int getColor()
    {
        return color;
    }

    public Dimension getDimension(){
        return dimension;
    }

    public int getDimensionsW() {
        return dimension.getWidth();
    }

    public int getDimensionsH() {
        return dimension.getHeight();
    }

    public void setDimensions(int w, int h) {
        dimension.setWidth(w);
        dimension.setHeight(h);
    }
    public void setDimensions(Dimension newDim) {
        dimension.setWidth(newDim.getWidth());
        dimension.setHeight(newDim.getHeight());
    }

    @Override
    public void draw(Graphics g, Point containerOrigin, Point originScreen)
    {

    }

    abstract public void localDraw(Graphics g, Point containerOrigin,
                                   Point originScreen);

    protected void rotate(double degrees) {
        myRotation.rotate((float)Math.toRadians(degrees), 0, 0);
    }
    protected void scale(double sx, double sy) {
        myScale.scale((float)sx, (float)sy);
    }
    protected void translate(double tx, double ty) {
        myTranslation.translate((float)tx, (float)ty);
    }

    protected void localTransform(Transform gXForm)
    {
        gXForm.translate(   myTranslation.getTranslateX(),
                            myTranslation.getTranslateY());
        gXForm.concatenate(myRotation);
        gXForm.scale(myScale.getScaleX(),myScale.getScaleY());
    }

    protected void resetTranformToOrginal(Graphics g)
    {
        g.setTransform(gOrigXForm);
    }

    protected Transform preLTTransform(Graphics g, Point originScreen)
    {
        Transform gXForm = Transform.makeIdentity();
        g.getTransform(gXForm);
        gOrigXForm = gXForm.copy();

        gXForm.translate(originScreen.getX(), originScreen.getY());

        return gXForm;

    }

    protected void postLTTransform(Graphics g, Point originScreen,
                                   Transform gXForm)
    {
        gXForm.translate(-originScreen.getX(),-originScreen.getY());
        g.setTransform(gXForm);
    }

    protected void containerTranslate(Graphics g, Point parentOrigin)
    {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        localTransform(gxForm);
        g.setTransform(gxForm);
    }

    public Transform flipGameObjectsAfterVTM(Point containerOrigin,
                                           Point screenOrigin)
    {
        Transform localTransform = Transform.makeIdentity();
        localTransform.translate(   -containerOrigin.getX(),
                                    -containerOrigin.getY());
        localTransform.translate(screenOrigin.getX(),screenOrigin.getY());

        localTransform.translate(   myTranslation.getTranslateX(),
                                    myTranslation.getTranslateY());
        localTransform.scale(myScale.getScaleX(),myScale.getScaleY());
        localTransform.concatenate(myRotation);

        localTransform.translate(-screenOrigin.getX(),-screenOrigin.getY());
        localTransform.translate(containerOrigin.getX(),containerOrigin.getY());

        return localTransform;

    }

    public boolean IsPointInsideBounds(Point2D[] bounds, int xValue,
                                       int yValue)
    {
        return (bounds[0].getX() <= xValue)
                && (bounds[1].getY() <= yValue)
                && (bounds[1].getX() >= xValue)
                && ((bounds[2].getY() >= yValue));
    }

    public boolean PointMatch(float xValue, float yValue, float xValue2,
                                       float yValue2)
    {
        return (xValue == xValue2) && (yValue == yValue2);
    }
}
