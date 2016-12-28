package com.example.java.simpleplayer.views.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.presenters.SongsPresenter;
import com.example.java.simpleplayer.views.MusicActivity;
import com.example.java.simpleplayer.views.SongsAdapter;
import com.example.java.simpleplayer.views.SongsView;

import java.util.List;

public class SongsFragment extends Fragment implements SongsView {

    MusicActivity.PlayBackInteraction mPlayBackInteraction;

    private SongsPresenter mPresenter = new SongsPresenter();

    private RecyclerView mRecyclerView = null;
    private SongsAdapter mSongsAdapter = new SongsAdapter();


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MusicActivity){
            mPlayBackInteraction = ((MusicActivity) activity).getPlayBackService();
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
    }

    @Override
    public void onAllSongsLoaded(List<Song> songList) {
        mSongsAdapter.setDataSource(songList);
        mSongsAdapter.setOnItemClickListener(view ->{

            final SongsAdapter.SongViewHolder holder
                    = (SongsAdapter.SongViewHolder) mRecyclerView.findContainingViewHolder(view);


        });
        mRecyclerView.setAdapter(mSongsAdapter);
    }

    private void initPlayBackInteraction(){
     if (getActivity() instanceof MusicActivity){
         mPlayBackInteraction =
     }
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onDetach();
    }
}
