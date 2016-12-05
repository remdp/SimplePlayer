package com.example.java.simpleplayer.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.presenters.SongsPresenter;
import com.example.java.simpleplayer.services.PlayBackService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SongsView {

    public static final String TAG = MainActivity.class.getSimpleName();

    private SongsPresenter mPresenter = new SongsPresenter();

    private PlayBackService mService;
    private boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter.onAttachToView(this);
        mPresenter.loadAllSongs();

      //  final Intent playbackIntent = PlayBackService.newInstance(this);
        //playbackIntent.setAction(PlayBackService.ACTION_PLAY);
        //startService(playbackIntent);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                stopService(PlayBackService.newInstance(MainActivity.this));
//            }
//        },2000);

    };

    private ServiceConnection mConnection  = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            PlayBackService.PlayBackBinder binder =
                    (PlayBackService.PlayBackBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent playBackIntent = PlayBackService.newInstance(this);
        bindService(playBackIntent,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound){
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onAllSongsLoaded(List<Song> songList) {
       // Log.d(TAG, "" + songList.size());
        Toast.makeText(this, ""+songList.size(), Toast.LENGTH_SHORT).show();
    }
}
