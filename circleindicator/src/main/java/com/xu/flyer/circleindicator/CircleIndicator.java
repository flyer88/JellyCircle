package com.xu.flyer.circleindicator;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by flyer on 15/11/5.
 */
public class CircleIndicator extends View{


    private Circle mStartCircle;//动画开始的圆，变小的圆，消失的圆
    private Circle mEndCircle;//手拖动的圆,在移动的圆，变大的圆

    private Point mStartA;//第一个圆的上方切点
    private Point mEndD;//第二个圆的上方切点
    private Point mStartB;//第一个圆的下方切点
    private Point mEndC;//第二个愿的下方切点

    private Point mControlPointO;// AD 两点的控制点
    private Point mControlPointP;// BC 两点的控制点


    Path mBezierPath;//贝塞尔曲线的path
    Paint mBezierPaint;//贝塞尔曲线的paint

    private boolean mCanDrawBezier = false;//判断是否可以画贝塞尔曲线，如果两个圆重叠，则不可以画

    private float mDefaultFirstX = 200;//默认第一个圆的x
    private float mDefaultFirstY = 20;//默认圆的y
    private float mDefaultMinRadius = 5;//默认圆的最小半径
    private float mDefaultMaxRadius = 10;//默认圆的最大半径

    private float mCurrentDistance = 0f;//当前两个圆心距离
    private float mMaxDistance = 80;//两个圆能到达的最大距离，大于最大距离将断开

    private float mDownX;
    private float mDownY;
    private float mMoveX;
    private float mMoveY;


    public CircleIndicator(Context context) {
        super(context);
        initDefaultValue();
    }

    public CircleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDefaultValue();

    }

    public CircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化默认设置
     */
    public void initDefaultValue(){
        mBezierPath = new Path();
        mBezierPaint = new Paint();
        mStartCircle = new Circle();
        mEndCircle = new Circle();

        mStartCircle.setX(dpToPx(mDefaultFirstX, getResources()));
        mStartCircle.setY(dpToPx(mDefaultFirstY, getResources()));
        mStartCircle.setRadius(dpToPx(mDefaultMaxRadius, getResources()));

        mEndCircle.setX(dpToPx(mDefaultFirstX, getResources()));
        mEndCircle.setY(dpToPx(mDefaultFirstY, getResources()));
        mEndCircle.setRadius(dpToPx(mDefaultMinRadius, getResources()));//开始，startCircle和endCircle处于同一位置，但是圆心大小不一样
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mEndCircle.setX(event.getX());
                mEndCircle.setY(event.getY());
                mCanDrawBezier = calculatePoint(mStartCircle,mEndCircle);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                mCanDrawBezier = true;
                mMoveX = event.getX();
                mMoveY = event.getY();
                mEndCircle.setX(event.getX());
                mEndCircle.setY(event.getY());
                double distance = Math.sqrt((mMoveX - mStartCircle.getX()) * (mMoveX - mStartCircle.getX())
                        + (mMoveY - mStartCircle.getY()) * (mMoveY - mStartCircle.getY()));
                mEndCircle.setRadius((float) (dpToPx(mDefaultMinRadius, getResources()) + distance / dpToPx(mMaxDistance, getResources()) * dpToPx(mDefaultMaxRadius - mDefaultMinRadius, getResources())));
                mStartCircle.setRadius((float) (dpToPx(mDefaultMaxRadius,getResources()) - distance / dpToPx( mMaxDistance ,getResources()) * dpToPx(mDefaultMaxRadius - mDefaultMinRadius,getResources())));
                mCanDrawBezier = calculatePoint(mStartCircle,mEndCircle);
                invalidate();
//                if (mCurrentDistance >= dpToPx(mMaxDistance,getResources())){
//                    if (mOnTouchMoveListener != null){
//                        mOnTouchMoveListener.onOutOfEdgeDown();
//                    }
//                }
                break;

            case MotionEvent.ACTION_UP:
                if (mCurrentDistance >= dpToPx(mMaxDistance,getResources())){
                    if (mOnTouchMoveListener != null){
                        mOnTouchMoveListener.onOutOfEdgeUp();
                    }
                }
                break;

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCurrentDistance >= dpToPx(mMaxDistance,getResources())){//如果到达最大值，就只绘制endCircle圆
            canvas.drawCircle(mEndCircle.getX(), mEndCircle.getY(), mEndCircle.getRadius(), mBezierPaint);
            return;
        }
        canvas.drawCircle(mStartCircle.getX(), mStartCircle.getY(), mStartCircle.getRadius(), mBezierPaint);//绘制startCircle
        if (mCanDrawBezier) {//如果可以绘制
            refreshBezierPath();//刷新贝塞尔曲线 path 值
            canvas.drawPath(mBezierPath, mBezierPaint);//绘制贝塞尔曲线
            canvas.drawCircle(mEndCircle.getX(), mEndCircle.getY(), mEndCircle.getRadius(), mBezierPaint);//绘制结束的圆
        }

    }


    /**
     * 贝塞尔曲线
     * 计算出6个点
     * 同时判断是否可以画（两个圆相同的时候画出的是直线，默认return false，不进行绘制）
     * @return
     */
    public boolean calculatePoint(Circle startCircle,Circle endCircle){

        float startX = startCircle.getX();
        float startY = startCircle.getY();
        float endX = endCircle.getX();
        float endY = endCircle.getY();
        mCurrentDistance = (float) Math.sqrt(((startX - endX) * (startX - endX)) + ((startY - endY) * (startY - endY)));
        if (mCurrentDistance == 0f){
            return false;
        }
        float cos = (endY - startY) / mCurrentDistance;
        float sin = (endX - startX) / mCurrentDistance;

        float xA = startX - startCircle.getRadius() * cos;
        float yA = startY + startCircle.getRadius() * sin;
        mStartA = new Point(xA,yA);
        float xB = startX + startCircle.getRadius() * cos;
        float yB = startY - startCircle.getRadius() * sin;
        mStartB = new Point(xB,yB);
        float xC = endX + endCircle.getRadius() * cos;
        float yC = endY - endCircle.getRadius() * sin;
        mEndC = new Point(xC,yC);
        float xD = endX - endCircle.getRadius() * cos;
        float yD = endY + endCircle.getRadius() * sin;
        mEndD = new Point(xD,yD);



        float controlOX = xA + mCurrentDistance /2 * sin;
        float controlOY = yA + mCurrentDistance /2 * cos;
        mControlPointO = new Point(controlOX,controlOY);
        float controlPX = xB + mCurrentDistance / 2 * sin;
        float controlPY = yB + mCurrentDistance /2 * cos;
        mControlPointP = new Point(controlPX,controlPY);


        return true;
    }

    /**
     * 闭合两条贝塞尔曲线，同时刷新 path
     */
    public void refreshBezierPath(){
        mBezierPath.reset();
        mBezierPath.moveTo(mStartA.getX(), mStartA.getY());
        mBezierPath.lineTo(mStartB.getX(), mStartB.getY());
        mBezierPath.quadTo(mControlPointP.getX(), mControlPointP.getY(), mEndC.getX(), mEndC.getY());
        mBezierPath.lineTo(mEndD.getX(), mEndD.getY());
        mBezierPath.quadTo(mControlPointO.getX(), mControlPointO.getY(), mStartA.getX(), mStartA.getY());
    }

    /**
     * 设置初始位置
     * @param x
     * @param y
     */
    public void setDefaultFirstPoint(float x, float y){
        this.mDefaultFirstX = x;
        this.mDefaultFirstY = y;
        mStartCircle.setX(dpToPx(mDefaultFirstX, getResources()));
        mStartCircle.setY(dpToPx(mDefaultFirstY, getResources()));
        mStartCircle.setRadius(dpToPx(mDefaultMaxRadius, getResources()));
        mEndCircle.setX(dpToPx(mDefaultFirstX, getResources()));
        mEndCircle.setY(dpToPx(mDefaultFirstY, getResources()));
        mEndCircle.setRadius(dpToPx(mDefaultMinRadius, getResources()));//开始，startCircle和endCircle处于同一位置，但是圆心大小不一样
        invalidate();
    }


    /**
     * 通过偏移量来移动
     * @param offset
     */
    public void moveCircleByOffset(float offset){//开始移动时，y值不变，x值开始根据偏移量变化
        mEndCircle.setX(dpToPx(mDefaultFirstX + mMaxDistance * offset,getResources()));
        mEndCircle.setRadius(dpToPx(mDefaultMinRadius,getResources()) + offset * dpToPx(mDefaultMaxRadius - mDefaultMinRadius,getResources()));//EndCircle半径变大
        mStartCircle.setRadius(dpToPx(mDefaultMaxRadius,getResources()) - offset * dpToPx(mDefaultMaxRadius - mDefaultMinRadius,getResources()));//StartCircle半径变小
        mCanDrawBezier = calculatePoint(mStartCircle, mEndCircle);
        invalidate();
    }

    /**
     * 设置开始和结束值，直接动画
     * @param offX
     * @param offY
     */
    public void moveCircle(float offX,float offY){
        mEndCircle.setRadius(dpToPx(mDefaultMinRadius,getResources()));
        mEndCircle.setY(mStartCircle.getY());
        animCircle(mStartCircle.getX() + 1, mStartCircle.getX() + dpToPx(offX, getResources()),
                mStartCircle.getY() + 1, mStartCircle.getY() + dpToPx(offY, getResources()));
    }

    private void animCircle(final float startXValue, final float endXValue,final float startYValue,final float endYValue){
        ValueAnimator animatorRadius = ValueAnimator.ofFloat(startXValue, endXValue);
        animatorRadius.setDuration(300);
        animatorRadius.setInterpolator(new DecelerateInterpolator());
        animatorRadius.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float floatValue = (float) animation.getAnimatedValue();
                mEndCircle.setX(floatValue);
                float floatYValue = ((floatValue - startXValue) / (endXValue - startXValue)) * (endYValue - startYValue) + startYValue;
                mEndCircle.setY(floatYValue);
                float startRadius = dpToPx(mDefaultMaxRadius - (((floatValue - startXValue) / (endXValue - startXValue)) * (mDefaultMaxRadius - mDefaultMinRadius)), getResources());
                mStartCircle.setRadius(startRadius);
                float endRadius = dpToPx(mDefaultMinRadius + (((floatValue - startXValue) / (endXValue - startXValue)) * (mDefaultMaxRadius - mDefaultMinRadius)), getResources());
                mEndCircle.setRadius(endRadius);
                mCanDrawBezier = calculatePoint(mStartCircle, mEndCircle);
                invalidate();
                if (floatValue == startXValue){
                    if (mAnimateListener != null){
                        mAnimateListener.onAnimateStart();
                    }
                }
                if (floatValue == endXValue){
                    if (mAnimateListener != null){
                        mAnimateListener.onAnimateEnd();
                    }
                }
            }
        });
        animatorRadius.start();
    }


    public int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    private CircleAnimateListener mAnimateListener;
    private CircleOnTouchMoveListener mOnTouchMoveListener;

    public interface CircleAnimateListener{
        public void onAnimateStart();
        public void onAnimateEnd();
    }

    public interface CircleOnTouchMoveListener{
        public void onOutOfEdgeUp();
//        public void onOutOfEdgeDown();
    }


    public void setTouchMoveListener(CircleOnTouchMoveListener onTouchMoveListener){
        this.mOnTouchMoveListener = onTouchMoveListener;
    }
    public void setAnimateListener(CircleAnimateListener animateListener){
        this.mAnimateListener = animateListener;
    }
}
