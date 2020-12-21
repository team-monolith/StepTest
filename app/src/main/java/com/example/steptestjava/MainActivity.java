package com.example.steptestjava;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener,
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    protected SensorManager mSensorManager;
    private TextView mStepsTextView;
    private ImageButton mStartStopButton;
    private RadioGroup mRadioGroup;
    private float mPrevCount;
    private float mCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mStepsTextView = (TextView) findViewById(R.id.textView1);
        mStartStopButton = (ImageButton) findViewById(R.id.button1);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        mStartStopButton.setOnClickListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (mPrevCount != 0) {
                mCount += event.values[0] - mPrevCount;
            }
            mPrevCount = event.values[0];
        } else {
            mCount++;
        }
        mStepsTextView.setText("Running!!\n" + (int) mCount);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing to do
    }

    @Override
    public void onClick(View v) {
        int checkedId = mRadioGroup.getCheckedRadioButtonId();
        Object tag = mStartStopButton.getTag();
        int resid;
        if (Integer.valueOf(android.R.drawable.ic_media_pause).equals(tag)) {
            resid = android.R.drawable.ic_media_play;
            onCheckedChanged(mRadioGroup, checkedId);
        } else {
            int type = checkedId == R.id.radio0 ? Sensor.TYPE_STEP_COUNTER
                    : Sensor.TYPE_STEP_DETECTOR;
            Sensor sensor = mSensorManager.getDefaultSensor(type);
            resid = android.R.drawable.ic_media_pause;
            mSensorManager.registerListener(this, sensor,
                    SensorManager.SENSOR_DELAY_UI);
            mStepsTextView.setText("Running\n");
        }
        mStartStopButton.setImageResource(resid);
        mStartStopButton.setTag(resid);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mCount = mPrevCount = 0;
        mSensorManager.unregisterListener(this);
        mStartStopButton.setImageResource(android.R.drawable.ic_media_play);
        mStartStopButton.setTag(android.R.drawable.ic_media_play);
        mStepsTextView.setText("Stopped\n");
    }
}
