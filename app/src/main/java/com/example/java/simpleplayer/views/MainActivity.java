package com.example.java.simpleplayer.views;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.presenters.SongsPresenter;
import com.example.java.simpleplayer.services.PlayBackService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SongsView, View.OnClickListener {

    private SongsPresenter mPresenter = new SongsPresenter();

    private PlayBackService mService;
    private boolean mBound = false;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private static final int SPAN_COUNT = 2;

    private Button buttonStop;

    public static Intent newIntent (Context context){
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation translateAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.activity_in);
                mProgressBar.startAnimation(translateAnimation);
            }
        },2000);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mPresenter.onAttachToView(this);
        mPresenter.loadAllSongs();

        buttonStop = (Button) findViewById(R.id.stop_button);
        buttonStop.setOnClickListener(this);
        Intent playbackIntent = PlayBackService.newInstance(this);
        playbackIntent.setAction(PlayBackService.ACTION_PLAY);
        startService(playbackIntent);

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
        //Toast.makeText(this, ""+songList.size(), Toast.LENGTH_SHORT).show();

        final RecyclerView.LayoutManager manager = new GridLayoutManager(
                this,
                SPAN_COUNT);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        final SongsAdapter adapter = new SongsAdapter();
        adapter.setDataSource(songList);
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClicklistener(new View.OnClickListener() {
            @Override
            public void onClick(View item) {
                final SongsAdapter.SongViewHolder holder =
                        (SongsAdapter.SongViewHolder) mRecyclerView.findContainingViewHolder(item);
                if (holder == null) return;
                final Song song = holder.getSong();
                final long songId = song.id;
                if (mBound) {
                    mService.playSongId(songId);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
        mService.stopPlay();
    }
}
