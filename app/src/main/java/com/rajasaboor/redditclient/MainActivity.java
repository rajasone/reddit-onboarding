package com.rajasaboor.redditclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rajasaboor.redditclient.retrofit.RetrofitController;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Initiate the call to the base URI
         */
        RetrofitController controller = new RetrofitController();
        controller.start();
        Log.d(TAG, "onCreate: end");
    }
}
