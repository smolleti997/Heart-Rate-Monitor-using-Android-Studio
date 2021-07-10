package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.TextureView;

import androidx.annotation.RequiresApi;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
public class OutputAnalyzer {
  // Handler mainHandler =new Handler();
//    private final Handler mainHandler = new Handler();

    private final Activity activity;

    private MeasureStore store;

    private final int measurementInterval = 45;
    private final int measurementLength = 45000; // ensure the number of data points is the power of two
    private final int clipLength = 3500;

    private int detectedValleys = 0;
    private int ticksPassed = 0;

    private final CopyOnWriteArrayList<Long> valleys = new CopyOnWriteArrayList<>();
    private CountDownTimer timer;
    private final Handler mainHandler;

    OutputAnalyzer(Activity activity, Handler mainHandler) {
        this.activity = activity;
        this.mainHandler = mainHandler;
    }

    private boolean detectValley() {
        final int valleyDetectionWindowSize = 13;
       CopyOnWriteArrayList<Measurement<Integer>> subList = store.getLastStdValues(valleyDetectionWindowSize);
        if (subList.size() < valleyDetectionWindowSize) {
            return false;
        } else {
            Integer referenceValue = subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement;

            for (Measurement<Integer> measurement : subList) {
               if (measurement.measurement < referenceValue) return false;
            }

            // filter out consecutive measurements due to too high measurement rate
            return (!subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f)).measurement.equals(
                    subList.get((int) Math.ceil(valleyDetectionWindowSize / 2f) - 1).measurement));
        }
    }

    void measurePulse(TextureView textureView, CameraService cameraService) {

        // 20 times a second, get the amount of red on the picture.
        // detect local minimums, calculate pulse.

        store = new MeasureStore();

        detectedValleys = 0;

        timer = new CountDownTimer(measurementLength, measurementInterval) {

        //    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTick(long millisUntilFinished) {
                // skip the first measurements, which are broken by exposure metering
                if (clipLength > (++ticksPassed * measurementInterval)) return;

                sendMessage(MainActivity.MESSAGE_UPDATE_REALTIME,millisUntilFinished/1000 );
//                Snackbar snackbar = Snackbar
//                        .make(consraintLayout, "www.journaldev.com", Snackbar.LENGTH_LONG);
//                snackbar.show();
                Thread thread = new Thread(() -> {
                    Bitmap currentBitmap = textureView.getBitmap();
                    int pixelCount = textureView.getWidth() * textureView.getHeight();
                    int measurement = 0;
                    int[] pixels = new int[pixelCount];

                    currentBitmap.getPixels(pixels, 0, textureView.getWidth(), 0, 0, textureView.getWidth(), textureView.getHeight());


                    for (int pixelIndex = 0; pixelIndex < pixelCount; pixelIndex++) {
                        measurement += (pixels[pixelIndex] >> 16) & 0xff;
                    }


                    store.add(measurement);

                    if (detectValley()) {
                        detectedValleys = detectedValleys + 1;
                        valleys.add(store.getLastTimestamp().getTime());

                    }
                });
                thread.start();
            }

           @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onFinish() {
                // clip the interval to the first till the last one - on this interval, there were detectedValleys - 1 periods
                String currentValue = String.valueOf(60f * (detectedValleys - 1) / (Math.max(1, (valleys.get(valleys.size() - 1) - valleys.get(0)) / 1000f)));
                sendMessage(MainActivity.MESSAGE_UPDATE_FINAL, currentValue);
                CameraService.stop();
            }
        };

        timer.start();
    }

    void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

   void sendMessage(int what, Object message) {
      Message msg = new Message();
        msg.what = what;
        msg.obj = message;
        mainHandler.sendMessage(msg);
    }
}