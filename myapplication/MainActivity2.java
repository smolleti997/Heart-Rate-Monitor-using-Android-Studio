package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

import static com.example.myapplication.Accelerometer.respiRateValue;
import static com.example.myapplication.MainActivity.heartratevalue;
import static com.example.myapplication.MainActivity.respRatevalue;


public class MainActivity2 extends AppCompatActivity {
    Button submit;
    Button exit;
    String[] ratings=new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        submit = findViewById(R.id.submit);
        exit = findViewById(R.id.exit);
        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view)
            {
                SQLiteDatabase db = openOrCreateDatabase("db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
                db.beginTransaction();
//                for(HashMap<String, Integer> hmap : hmap){
                    ContentValues cv = new ContentValues();
                    cv.put(("Heart Rate"),heartratevalue);
                    cv.put(("Respiratory Rate"),respiRateValue);
                    cv.put(("Headache"),0);
                    cv.put(("Feeling tired"),0);
                    cv.put(("Breath Shortness"),0);
                    cv.put(("Diarrhea"),0);
                    cv.put(("Fever"),0);
                    cv.put(("Nausea"),0);
                    cv.put(("Loss of Smell and Taste"),0);
                    cv.put(("Muscle Ache"),0);
                    cv.put(("Sore throat"),0);
                    cv.put(("cough"),0);
                    db.insert("db", null, cv);
                db.setTransactionSuccessful();
//                db.endTransaction();
//                db.close();
                Toast.makeText(MainActivity2.this, " database created", Toast.LENGTH_LONG).show();
                db.getPageSize();

//                db.endTransaction();
//                db.close();

            }
        });


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 finishAffinity();

            }
        });

    }

}