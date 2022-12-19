package edu.umich.dpm.sensorgrabber.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import edu.umich.dpm.sensorgrabber.stream.OutputStreamConnector;
import edu.umich.dpm.sensorgrabber.stream.Streamable;

public final class SensorConnector implements SensorEventListener, Streamable {
    private static final String TAG = "SensorConnector";

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private OutputStreamConnector mStreamConnector;

    private SensorBuffer mBuffer;



    public SensorConnector(Context context, int sensorType, OutputStreamConnector streamConnector,
                           int numQueues, int queueSize) {

        mStreamConnector = streamConnector;


        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(sensorType);
        Log.d("Sensor Connector","Sensor Connector constructor invoked with sensor"+ mSensor.getName());


        try {
            Log.d("Error Caught in try","Oh yesssssssssssssssss");

            mBuffer = new SensorBuffer(
                    mSensor.getType(),
                    numQueues,
                    queueSize
            );


        }
        catch (InstantiationException e) {
            Log.d("InstantiationException","InstantiationException in SensorConnector invoked");
            e.printStackTrace();
        }
        catch (IllegalAccessException e) {
            Log.d("IllegalAccessException","IllegalAccessException in SensorConnector invoked");
            e.printStackTrace();
        }
//        finally {
//            Log.d("Some Exception","Might be null pointer ");
//        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.v(TAG, "onSensorChanged");
        mBuffer.recordReading(event, mStreamConnector);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.v(TAG, "onAccuracyChanged");
    }

    @Override
    public void onStreamStarted() {
        Log.d(TAG, "onStreamStarted");

        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onStreamStopped() {
        Log.d(TAG, "onStreamStopped");

        mSensorManager.unregisterListener(this);
    }
}
