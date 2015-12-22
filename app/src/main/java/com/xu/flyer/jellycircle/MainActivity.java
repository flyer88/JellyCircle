package com.xu.flyer.jellycircle;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ViewPager mPager;
    JellyCircle mJellyCircle;
    ArrayList<View> mPagerContainer = new ArrayList<View>();
    List<Circle> mCircles = new ArrayList<>();
    SeekBar mSeekBar;

    int mStartPosition = 0;
    float mPreOffset = 0;
    boolean mRest = true;


    String TO_LEFT = "to left";
    String TO_RIGHT = "to right";

    String direction = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJellyCircle = (JellyCircle) findViewById(R.id.jelly_circle);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

        mCircles.add(new Circle(50, 10, 10, "dp", this));
        mCircles.add(new Circle(100,10,10,"dp",this));
        mCircles.add(new Circle(150, 10, 10, "dp", this));

        mJellyCircle.setCurrentCircle(mCircles.get(0));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                mJellyCircle.changeJellyCircleWithOffset(((float) (progress))/((float)(mSeekBar.getMax())));
                mJellyCircle.changeJellyCircleWithOffset(((float) (progress))/((float)(mSeekBar.getMax())),true,0.6f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



    }
}
