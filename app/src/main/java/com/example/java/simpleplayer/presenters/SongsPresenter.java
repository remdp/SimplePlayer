package com.example.java.simpleplayer.presenters;

import android.support.annotation.NonNull;

import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.repositories.PlayListRepository;
import com.example.java.simpleplayer.repositories.SongsRepository;
import com.example.java.simpleplayer.views.SongsView;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SongsPresenter {

    private SongsView mView = null;

    private PlayListRepository mPlayListRepository = new PlayListRepository();

    public void onAttachToView(@NonNull SongsView songsView) {
        mView = songsView;
    }

    private Subscription subscription = null;

    public void loadAllSongs() {
        subscription = Observable.just(SongsRepository.getAllSongs(mView.getContext()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songs -> { mView.onAllSongsLoaded(songs);},
                        Throwable::printStackTrace);

    }

    public void addToPlayList(Song song) {
        mPlayListRepository.addSong(song);
    }

    public void onDetach() {
        if(subscription != null)
            subscription.unsubscribe();
    }

}
