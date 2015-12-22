package com.xu.flyer.jellycircle;

import android.content.Context;

import com.xu.flyer.jellycircle.common.Util;

/**
 * Created by flyer on 15/11/5.
 */
public class Circle {


    private float mXRadius;
    private float mYRadius;

    private float mRadius;
    private float mX;
    private float mY;


    public Circle(){

    }

    public Circle(float x, float y, float radius){
        this.mRadius = radius;
        this.mXRadius = mYRadius = this.mRadius;
    }

    public Circle(float x, float y, float radius,String type,Context context){
        if (type.equals("dp")){
            this.mRadius = Util.dpToPx(radius,context.getResources());
            this.mXRadius = mYRadius = this.mRadius;
            this.mX = Util.dpToPx(x,context.getResources());
            this.mY = Util.dpToPx(y,context.getResources());
        } else {
            new Circle(x,y,radius);
        }

    }

    public boolean isCircle() {
        return mXRadius == mYRadius;
    }
    public void setXRadius(float radiusMax){
        this.mXRadius = radiusMax;
    }
    public void setYRadius(float radiusMin){
        this.mYRadius = radiusMin;
    }
    public float getX(){
        return this.mX;
    }
    public void setX(float x){
        this.mX = x;
    }
    public float getY(){
        return this.mY;
    }
    public void setY(float y){
        this.mY = y;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public float getXRadius(){
        return this.mXRadius;
    }

    public float getYRadius(){
        return this.mYRadius;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
    }
}
