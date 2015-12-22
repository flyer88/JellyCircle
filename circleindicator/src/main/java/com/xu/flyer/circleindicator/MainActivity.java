package com.xu.flyer.circleindicator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    Button mStart;
    CircleIndicator mIndicator;
    SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSeekBar = (SeekBar) findViewById(R.id.seek_bar);
        mStart = (Button) findViewById(R.id.start);
        mIndicator= (CircleIndicator) findViewById(R.id.indicator);
        mIndicator.setDefaultFirstPoint(40,100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mIndicator.moveCircleByOffset(((float)(progress)) / ((float)(mSeekBar.getMax())));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mIndicator.setTouchMoveListener(new CircleIndicator.CircleOnTouchMoveListener() {
            @Override
            public void onOutOfEdgeUp() {
                Toast.makeText(MainActivity.this,"拉出边界了,手指离开屏幕",Toast.LENGTH_SHORT).show();
            }

        });

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIndicator.moveCircle(81,0);
            }
        });
    }


}
