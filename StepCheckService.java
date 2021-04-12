package com.example.menu;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class StepCheckService extends Service implements SensorEventListener {

    int count = StepValue.Step;
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;

    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 800;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("onCreate", "IN");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    } // end of onCreate

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i("onStartCommand", "IN");
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        } // end of if

        return START_STICKY;
    } // end of onStartCommand

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy", "IN");
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            StepValue.Step = 0;
        } // end of if
    } // end of onDestroy

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("onSensorChanged", "IN");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);

            if (gabOfTime > 100) { //  gap of time of step count
                Log.i("onSensorChanged_IF", "FIRST_IF_IN");
                lastTime = currentTime;

                x = event.values[0];
                y = event.values[1];
                z = event.values[2];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    Log.i("onSensorChanged_IF", "SECOND_IF_IN");
                    Intent myFilteredResponse = new Intent("make.a.yong.manbo");

                    StepValue.Step = count++;

                    String msg = StepValue.Step / 2 + "";  // StepValue.java에 있는 step을 위에 생성한 count 이름에 넣어 증가
                    myFilteredResponse.putExtra("stepService", msg); //브로드 캐스트 리시버(BroadcastReceiver)의 stepService에 저장

                    sendBroadcast(myFilteredResponse); // 값 보냄 (StepActivity 맨 밑에 PlayingReceiver에 저장되어 보냄)
                } // end of if

                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];
            } // end of if
        } // end of if

    } // end of onSensorChanged

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
