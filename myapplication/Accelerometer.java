package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.telephony.SmsManager;


public class Accelerometer extends Service implements SensorEventListener {
   // int rate =0;
    private static SensorManager accelManage;
    private static Sensor senseAccel;
    private final int measurementInterval = 45;
    float accelValuesX[] = new float[451];
    float accelValuesY[] = new float[451];
    float accelValuesZ[] = new float[451];
    int index = 0;
    int k=0;
    public static int respiRateValue=0;
    Bundle b;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // TODO Auto-generated method stub
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            index++;
            accelValuesX[index] = sensorEvent.values[0];
            accelValuesY[index] = sensorEvent.values[1];
            accelValuesZ[index] = sensorEvent.values[2];
            if(index >= 450){
                index = 0;
                accelManage.unregisterListener(this);


                //Toast.makeText(this, respiRateValue, Toast.LENGTH_LONG).show();
               callFallRecognition();
                accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
                Toast.makeText(getApplicationContext(), " respiratory rate  is "+respiRateValue*2, Toast.LENGTH_LONG).show();
            }
        }
    }
    public void callFallRecognition(){
        float prev = 0;
        float curr = accelValuesZ[0];
       // prev = 10;
        for(int i=1;i<450;i++){
            curr = accelValuesZ[i];
            if(prev - curr > 1 ){
             //   Toast.makeText(this, "Fall detected", Toast.LENGTH_LONG).show();
                resp();
            }
            prev = accelValuesZ[i];

        }

    }
    public void resp()
    {
        respiRateValue +=1;
    }
//    public void callGestureRecognition(){
//        float avgX = 0;
//        float avgY = 0;
//        float avgZ = 0;
//        //Toast.makeText(this, "I am in gesture recognition", Toast.LENGTH_LONG).show();
//        for(int i=0;i<450;i++){
//            avgX = avgX + accelValuesX[i];
//            avgY = avgY + accelValuesY[i];
//            avgZ = avgZ + accelValuesZ[i];
//
//        }
//        avgX = avgX/450;
//        avgY = avgY/450;
//        avgZ = avgZ/450;
//
//        boolean left = true;
//
//        int zeroCrossingX = 0;
//        if(accelValuesX[0] >= avgX){
//            left = true;
//            for(int i=0;i<450;i+=8){
//                if(left){
//                    if(accelValuesX[i] < avgX){
//                        zeroCrossingX++;
//                        left = false;
//                    }
//                }else{
//                    if(accelValuesX[i] >= avgX){
//                        zeroCrossingX++;
//                        left = true;
//                    }
//                }
//            }
//        }else{
//            left = false;
//            for(int i=0;i<450;i+=8){
//                if(left){
//                    if(accelValuesX[i] < avgX){
//                        zeroCrossingX++;
//                        left = false;
//                    }
//                }else{
//                    if(accelValuesX[i] >= avgX){
//                        zeroCrossingX++;
//                        left = true;
//                    }
//                }
//            }
//        }
//
//        int zeroCrossingY = 0;
//        if(accelValuesY[0] >= avgY){
//            left = true;
//            for(int i=0;i<450;i+=8){
//                if(left){
//                    if(accelValuesY[i] < avgY){
//                        zeroCrossingY++;
//                        left = false;
//                    }
//                }else{
//                    if(accelValuesY[i] >= avgY){
//                        zeroCrossingY++;
//                        left = true;
//                    }
//                }
//            }
//        }else{
//            left = false;
//            for(int i=0;i<450;i+=8){
//                if(left){
//                    if(accelValuesY[i] < avgY){
//                        zeroCrossingY++;
//                        left = false;
//                    }
//                }else{
//                    if(accelValuesY[i] >= avgY){
//                        zeroCrossingY++;
//                        left = true;
//                    }
//                }
//            }
//        }
//
//        int zeroCrossingZ = 0;
//        if(accelValuesZ[0] >= avgZ){
//            left = true;
//            for(int i=0;i<450;i+=8){
//                if(left){
//                    if(accelValuesZ[i] < avgZ){
//                        zeroCrossingZ++;
//                        left = false;
//                    }
//                }else{
//                    if(accelValuesZ[i] >= avgZ){
//                        zeroCrossingZ++;
//                        left = true;
//                    }
//                }
//            }
//        }else{
//            left = false;
//            for(int i=0;i<450;i+=8){
//                if(left){
//                    if(accelValuesZ[i] < avgZ){
//                        zeroCrossingZ++;
//                        left = false;
//                    }
//                }else{
//                    if(accelValuesZ[i] >= avgZ){
//                        zeroCrossingZ++;
//                        left = true;
//                    }
//                }
//            }
//        }
//
//        if(zeroCrossingX > 7 || zeroCrossingY > 7 || zeroCrossingZ > 7){
//
//            Toast.makeText(this, "Hi gesture", Toast.LENGTH_LONG).show();
//            if(k == 0){
//                //sendSMS();
//            }
//            zeroCrossingX = 0;
//            zeroCrossingY = 0;
//            zeroCrossingZ = 0;
//            k++;
//
//        }
//
//
//    }
    public void sendSMS() {

        String phoneNumber = b.getString("phone");
        Toast.makeText(Accelerometer.this, phoneNumber, Toast.LENGTH_LONG).show();
        String message = "Fall detected";

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public   void onCreate(){
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        accelManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senseAccel = accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
        Toast.makeText(Accelerometer.this, "Count started", Toast.LENGTH_LONG).show();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      b = intent.getExtras();
       String phoneNumber = b.getString("phone");
     Toast.makeText(Accelerometer.this, phoneNumber, Toast.LENGTH_LONG).show();
//        // We want this service to continue running until it is explicitly
//        // stopped, so return sticky.
//        k = 0;
//     return k;

        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}