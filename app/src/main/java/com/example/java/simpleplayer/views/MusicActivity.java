package com.example.java.simpleplayer.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.example.java.simpleplayer.services.PlayBackService;
import com.example.java.simpleplayer.views.base.BaseActivity;

import rx.Observable;

public class MusicActivity extends BaseActivity {

    public interface PlayBackInteraction {

        boolean play();

        void pause();

        void play(long songId);

        boolean isPaused();

        Observable<Integer> gerDurationObservable();

        void onUserSeek(int progress);



    }

    private PlayBackService mService;

    private boolean mBound = false;

    @Nullable
    public PlayBackInteraction getPlayBackInteraction() {
        return mService;
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder iBinder) {
            PlayBackService.PlayBackBinder binder
                    = (PlayBackService.PlayBackBinder) iBinder;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent playBackIntent = PlayBackService.newInstance(this);
        startService(playBackIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent playBackIntent = PlayBackService.newInstance(this);
        bindService(playBackIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
}
