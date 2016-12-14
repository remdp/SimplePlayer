package com.example.java.simpleplayer.views;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.example.java.simpleplayer.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   startActivity(MainActivity.newIntent(this));
      //  overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
       // setContentView(R.layout.activity_splash);
     }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(MainActivity.newIntent(SplashActivity.this));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }, 1000);
    }

}
