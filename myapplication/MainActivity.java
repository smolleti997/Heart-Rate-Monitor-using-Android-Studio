package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.os.Handler;
import android.os.Message;
import android.view.TextureView;
import android.view.Surface;

import java.util.HashMap;
import java.util.Map;
//import java.util.logging.Handler;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    String phoneNumber;
    Button sympthom;
    Button resprate;
    Button heartrate;
    public static int heartratevalue;
    public static int respRatevalue;
   public static Map< String, Integer> hmap = new HashMap<String,Integer>();

     {
         hmap.put("Heart Rate", 0);
         hmap.put("Respiratory Rate", 0);
         hmap.put("Headache", 0);
         hmap.put("Feeling tired", 0);
         hmap.put("Breath Shortness", 0);
         hmap.put("Diarrhea", 0);
         hmap.put("Fever", 0);
         hmap.put("Nausea", 0);
         hmap.put("Loss of Smell and Taste", 0);
         hmap.put("Muscle ache", 0);
         hmap.put("Cough", 0);
         hmap.put("Sore throat", 0);
     }
    private final CameraService cameraService = new CameraService(this);
    private final int REQUEST_CODE_CAMERA = 0;

    private boolean justShared = false;

    private boolean menuNewMeasurementEnabled = false;
    private boolean menuExportResultEnabled = false;
    private boolean menuExportDetailsEnabled = false;

    private OutputAnalyzer analyzer;

    public static final int MESSAGE_UPDATE_REALTIME = 1;
    public static final int MESSAGE_UPDATE_FINAL = 2;



//    @Override
//    protected void onPause() {
//        super.onPause();
//        cameraService.stop();
//        if (analyzer != null) analyzer.stop();
//        analyzer = new OutputAnalyzer(this, mainHandler);
//    }

    @SuppressLint("HandlerLeak")
    private final Handler mainHandler = new Handler() {
       @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            if (msg.what == MESSAGE_UPDATE_REALTIME) {
                ((TextView) findViewById(R.id.timerView)).setText(msg.obj.toString());
            }

            if (msg.what == MESSAGE_UPDATE_FINAL) {
                ((TextView) findViewById(R.id.timerView)).setText(msg.obj.toString());
               // heartratevalue = Integer.parseInt(msg.obj.toString());
                //)parseInt(msg.obj.toString());
               // hmap.put("Heart Rate", heartratevalue);

//                findViewById(R.id.floatingActionButton).setClickable(true);
                menuNewMeasurementEnabled = true;
                menuExportResultEnabled = true;
                menuExportDetailsEnabled = true;

            }
        }
   };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE_CAMERA);

        OutputAnalyzer analyzer = new OutputAnalyzer(this, mainHandler);
       // Sympthompage sym = new Sympthompage(this, mainHandler);
        sympthom = findViewById(R.id.button1);
       resprate = findViewById(R.id.respRate);
        heartrate = findViewById(R.id.heartRate);
        heartrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextureView cameraTextureView = findViewById(R.id.textureView);
                SurfaceTexture previewSurfaceTexture = cameraTextureView.getSurfaceTexture();
                Surface previewSurface = new Surface(previewSurfaceTexture);
                cameraService.start(previewSurface);
                analyzer.measurePulse(cameraTextureView, cameraService);
            }
        });

        resprate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent startAccelerometer = new Intent(MainActivity.this,Accelerometer.class);
                Bundle b1 = new Bundle();
                b1.putString("phone", phoneNumber);
                startAccelerometer.putExtras(b1);
                startService(startAccelerometer);
               // Accelerometer.onCreate();
            }
        });


        sympthom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });

    }




    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity2.class);
        startActivity(switchActivityIntent);

    }


}








