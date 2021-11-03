package org.csc133.a3.GameObjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.Interfaces.Drawable;

public class GameObject implements Drawable {

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

    public void setColor(int color)
    {
        this.color = color;
    }
    public double getLocationX()
    {
        return location.getX();
    }

    public double getLocationY()
    {
        return location.getY();
    }

    public Point2D getLocation()
    {
        return location;
    }

    public Point convertToPoint(Point2D point)
    {
        return new Point((int) point.getX(),(int)point.getY());
    }

    public void setLocation(double x, double y)
    {
        location.setX(x);
        location.setY(y);
    }

    public void setLocation(Point2D newLoc)
    {
        location.setX(newLoc.getX());
        location.setY(newLoc.getY());

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
    public void draw(Graphics g, Point containerOrigin, Point originScreeen)
    {

    }

    void localDraw(Graphics g, Point containerOrigin, Point originScreen)
    {

    }

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

    protected void postLTTransform(Graphics g, Point originScreen, Transform gXForm)
    {
        gXForm.translate(-originScreen.getX(),-originScreen.getY());
        g.setTransform(gXForm);
    }

    protected void containerTranslate(Graphics g, Point parentOrigin)
    {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        myTranslation.setTranslation(parentOrigin.getX(),parentOrigin.getY());
        localTransform(gxForm);
        g.setTransform(gxForm);
    }

    protected void cn1ForwardPrimitiveTranslate(Graphics g, Dimension pDimension)
    {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(-pDimension.getWidth()/2f,
                        -pDimension.getHeight()/2f);
        g.setTransform(gxForm);
    }

    protected void cn1ReversePrimitiveTranslate(Graphics g, Dimension pDimension)
    {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(pDimension.getWidth()/2f,
                        pDimension.getHeight()/2f);
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
        localTransform.concatenate(myRotation);
        localTransform.scale(myScale.getScaleX(),myScale.getScaleY());

        localTransform.translate(-screenOrigin.getX(),-screenOrigin.getY());
        localTransform.translate(containerOrigin.getX(),containerOrigin.getY());

        return localTransform;

    }


    

}
