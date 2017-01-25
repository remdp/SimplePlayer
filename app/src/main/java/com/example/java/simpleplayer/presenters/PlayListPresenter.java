package com.example.java.simpleplayer.presenters;

import android.support.annotation.NonNull;

import com.example.java.simpleplayer.models.PlayListModel;
import com.example.java.simpleplayer.repositories.PlayListRepository;
import com.example.java.simpleplayer.views.PlayListView;

import rx.Subscription;
import rx.internal.util.SubscriptionList;

/**
 * Created by java on 23.01.2017.
 */

public class PlayListPresenter {

    private PlayListView mView = null;

    private SubscriptionList mSubscriptionList = new SubscriptionList();

    private PlayListRepository mPlayListRepository = new PlayListRepository();

    public void onAttachToView(@NonNull PlayListView songsView) {
        mView = songsView;
    }

    public void loadPlayList() {
        Subscription subscription = mPlayListRepository.loadPlayList()
                .map(PlayListModel::getSongRealmList)
                .subscribe(songs -> mView.onPlayListLoaded(songs), Throwable::printStackTrace);
        mSubscriptionList.add(subscription);
    }

    public void onDetach() {
        mSubscriptionList.unsubscribe();
    }

}
