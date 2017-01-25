package com.example.java.simpleplayer.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.presenters.SongsPresenter;
import com.example.java.simpleplayer.views.MenuActivity;
import com.example.java.simpleplayer.views.MusicActivity;
import com.example.java.simpleplayer.views.MusicActivity.PlayBackInteraction;
import com.example.java.simpleplayer.views.adapters.SongsAdapter;
import com.example.java.simpleplayer.views.SongsView;

import java.util.List;

import rx.Observable;

public class SongsFragment extends Fragment implements SongsView {

    private PlayBackInteraction mPlayBackInteraction;

    private static final int SPAN_COUNT = 2;

    private SongsPresenter mPresenter = new SongsPresenter();

    private Observable<Song> mSongsObservable = null;

    private RecyclerView mRecyclerView = null;
    private SongsAdapter mSongsAdapter = new SongsAdapter();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        initPlayBackInteraction();
    }

    private void initPlayBackInteraction() {
        if(getActivity() instanceof MusicActivity) {
            mPlayBackInteraction = ((MusicActivity) getActivity())
                    .getPlayBackInteraction();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mPresenter.onAttachToView(this);
        mPresenter.loadAllSongs();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final RecyclerView.LayoutManager manager = new GridLayoutManager(
                getActivity(),
                SPAN_COUNT);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);

        new Handler().postDelayed(() -> {
            if(getActivity() instanceof MenuActivity) {
                MenuActivity menuActivity = (MenuActivity) getActivity();
                menuActivity.getQueryObservable()
                        .flatMap(query ->
                                mSongsObservable
                                        .filter(song -> song.getTitle().contains(query))
                                        .toList())
                        .subscribe(songList -> mSongsAdapter.setDataSource(songList));

            }
        }, 2000);
    }

    @Override
    public void onAllSongsLoaded(List<Song> songList) {
        mSongsAdapter.setDataSource(songList);
        mSongsAdapter.setOnItemClickListener(view ->{
            final SongsAdapter.SongViewHolder holder =
                    (SongsAdapter.SongViewHolder)
                            mRecyclerView.findContainingViewHolder(view);
            if(holder == null) return;
            final Song song = holder.getSong();
            final long songId = song.getId();

            if(mPlayBackInteraction == null) {
                initPlayBackInteraction();
            }
            if(mPlayBackInteraction != null) {
                mPlayBackInteraction.play(songId);
            }

        });
        mRecyclerView.setAdapter(mSongsAdapter);


        mSongsObservable = Observable.from(songList);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onDetach();
    }
}