package com.example.java.simpleplayer.presenters;

import android.support.annotation.NonNull;

import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.views.SongsView;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SongsPresenter {

    private SongsView mView = null;

    private Subscription subscription = null;

    public void onAttachToView(@NonNull SongsView songsView){
        mView = songsView;
    }

        public void loadAllSongs(){
            subscription = Observable.just(SongsRepository.getAllSongs(mView.getContext()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<ArrayList<Song>>() {
                                   @Override
                                   public void call(ArrayList<Song> songs) {
                                       mView.onAllSongsLoaded(songs);
                                   }
                               },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            });
        }

    public void onDetach(){
        if(subscription != null)
            subscription.unsubscribe();
    }

}
