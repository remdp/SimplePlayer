package com.example.java.simpleplayer.views;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.presenters.SongsPresenter;
import com.example.java.simpleplayer.services.PlayBackService;
import com.example.java.simpleplayer.views.base.BaseActivity;

import java.util.List;

public class MusicActivity extends BaseActivity {

    public interface PlayBackInteraction{
        void play();
        void pause();
        void play(long songId);
    }

    private PlayBackService mService;
    private boolean mBound = false;

    @Nullable
    public PlayBackInteraction getPlayBackService(){
        return mService;
    }

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

    private SongsPresenter mPresenter = new SongsPresenter();

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent playBackIntent = PlayBackService.newInstance(this);
        startService(playBackIntent);
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
}
