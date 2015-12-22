package com.xu.flyer.circleindicator;

/**
 * Created by flyer on 15/11/6.
 */
public class Point {
    private float mX;
    private float mY;

    public Point(){

    }
    public Point(float x, float y){
        this.mX = x;
        this.mY = y;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        this.mX = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        this.mY = y;
    }


}
