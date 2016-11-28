package com.example.java.simpleplayer;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import Services.PlayBackService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent playbackIntent = new Intent(this, PlayBackService.class);
        startService(playbackIntent);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopService(playbackIntent);
            }
        },500);
    }
}
