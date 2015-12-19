package com.xu.flyer.jellycircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by flyer on 15/11/13.
 */
public class JellyCircle extends View{

    Point mA;
    Point mControlAB1;
    Point mControlAB2;
    Point mB;
    Point mControlBC1;
    Point mControlBC2;
    Point mC;
    Point mControlCD1;
    Point mControlCD2;
    Point mD;
    Point mControlDA1;
    Point mControlDA2;
    Paint mBezierPaint;
    Path  mBezierPath;
    Circle mCurrentCircle;
    List<Circle> mCircles;


    private float mMaxRadius = 15;
    private float mMinRadius = 5;

    public JellyCircle(Context context) {
        super(context);
        initDefaultValue();
    }

    public JellyCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultValue();
    }

    public JellyCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultValue();
    }

    public void initDefaultValue(){
        mA = new Point();
        mControlAB1 = new Point();
        mControlAB2 = new Point();
        mB = new Point();
        mControlBC1 = new Point();
        mControlBC2 = new Point();
        mC = new Point();
        mControlCD1 = new Point();
        mControlCD2 = new Point();
        mD = new Point();
        mControlDA1 = new Point();
        mControlDA2 = new Point();
        mBezierPaint = new Paint();
        mBezierPath = new Path();
        mCurrentCircle = new Circle();
        mBezierPaint.setColor(Color.BLACK);
    }



    public void moveJellyCircle(int position,float offset){
        float offValue = Util.dpToPx(offset * (mMaxRadius - mMinRadius),getResources());
        mCurrentCircle.setXRadius(mCurrentCircle.getRadius() + offValue);
        mCurrentCircle.setYRadius(mCurrentCircle.getRadius() - offValue);
        calculateArc();
        invalidate();
    }

    public void initCircles(List<Circle> Circles){
        this.mCircles = Circles;
        mCurrentCircle = this.mCircles.get(0);
        calculateArc();
        invalidate();
    }

    /**
     * 计算 12 个点
     */
    public void calculateArc(){

        mA.setX(mCurrentCircle.getX());
        mA.setY(mCurrentCircle.getY() + mCurrentCircle.getYRadius());
        mB.setX(mCurrentCircle.getX() + mCurrentCircle.getXRadius());
        mB.setY(mCurrentCircle.getY());
        mC.setX(mCurrentCircle.getX());
        mC.setY(mCurrentCircle.getY() - mCurrentCircle.getYRadius());
        mD.setX(mCurrentCircle.getX() - mCurrentCircle.getXRadius());
        mD.setY(mCurrentCircle.getY());

        float offset = (float) (mCurrentCircle.getRadius() * 2 / 3.6);

        mControlAB1.setX(mCurrentCircle.getX() + offset);
        mControlAB1.setY(mCurrentCircle.getY() + mCurrentCircle.getYRadius());
        mControlAB2.setX(mCurrentCircle.getX() + mCurrentCircle.getXRadius());
        mControlAB2.setY(mCurrentCircle.getY() + offset);
        mControlBC1.setX(mCurrentCircle.getX() + mCurrentCircle.getXRadius());
        mControlBC1.setY(mCurrentCircle.getY() - offset);
        mControlBC2.setX(mCurrentCircle.getX() + offset);
        mControlBC2.setY(mCurrentCircle.getY() - mCurrentCircle.getYRadius());
        mControlCD1.setX(mCurrentCircle.getX() - offset);
        mControlCD1.setY(mCurrentCircle.getY() - mCurrentCircle.getYRadius());
        mControlCD2.setX(mCurrentCircle.getX() - mCurrentCircle.getXRadius());
        mControlCD2.setY(mCurrentCircle.getY() - offset);
        mControlDA1.setX(mCurrentCircle.getX() - mCurrentCircle.getXRadius());
        mControlDA1.setY(mCurrentCircle.getY() + offset);
        mControlDA2.setX(mCurrentCircle.getX() - offset);
        mControlDA2.setY(mCurrentCircle.getY() + mCurrentCircle.getYRadius());

    }



    /**
     * 绘制两个控制点的贝塞尔曲线
     * @return
     */
    public void drawBezierPath(){
        mBezierPath.reset();
        mBezierPath.moveTo(mA.getX(),mA.getY());
        mBezierPath.cubicTo(mControlAB1.getX(), mControlAB1.getY(), mControlAB2.getX(), mControlAB2.getY(), mB.getX(), mB.getY());
        mBezierPath.cubicTo(mControlBC1.getX(), mControlBC1.getY(), mControlBC2.getX(), mControlBC2.getY(), mC.getX(), mC.getY());
        mBezierPath.cubicTo(mControlCD1.getX(), mControlCD1.getY(), mControlCD2.getX(), mControlCD2.getY(), mD.getX(), mD.getY());
        mBezierPath.cubicTo(mControlDA1.getX(),mControlDA1.getY(),mControlDA2.getX(),mControlDA2.getY(),mA.getX(),mA.getY());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBezierPath();
        canvas.drawPath(mBezierPath,mBezierPaint);
    }
}
