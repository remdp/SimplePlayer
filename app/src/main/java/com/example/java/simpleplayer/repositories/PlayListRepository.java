package com.example.java.simpleplayer.repositories;

import com.example.java.simpleplayer.models.PlayListModel;
import com.example.java.simpleplayer.models.Song;

import io.realm.Realm;
import io.realm.RealmList;
import rx.Completable;
import rx.Single;

/**
 * Created by java on 23.01.2017.
 */

public class PlayListRepository {

    private Realm mRealm = Realm.getDefaultInstance();

    public Single<PlayListModel> loadPlayList() {
        return Single.create(singleSubscriber -> {
            mRealm.executeTransaction(realm -> {
                final PlayListModel result = realm
                        .where(PlayListModel.class)
                        .findFirst();
                singleSubscriber.onSuccess(result);
            });
        });
    }

    public void addSong(Song song) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PlayListModel playListModel = realm.where(PlayListModel.class).findFirst();
                playListModel.getSongRealmList().add(song);
            }
        });
    }

    private Completable clear() {
        return Completable.fromAction(() ->
                mRealm.executeTransactionAsync(realm ->
                        realm.delete(PlayListModel.class)
                ));
    }

    private Single<Song> getNextSongAfter(long id) {
        return Single.create(singleSubscriber -> {
            mRealm.executeTransaction(realm -> {
                PlayListModel playList = realm
                        .where(PlayListModel.class)
                        .findFirst();
                RealmList<Song> songs = playList.getSongRealmList();
                Song song = realm.where(Song.class).equalTo("id", id).findFirst();
                if (song == null){
                    singleSubscriber
                            .onError(new IllegalArgumentException("can't find song with id " + id));
                }
                int currentSongIndex = songs.indexOf(song);
                if(currentSongIndex == -1) {
                    singleSubscriber
                            .onError(new IllegalArgumentException("can't find song with id " + id));
                }
                currentSongIndex ++;
                if(currentSongIndex <= songs.size()) {
                    singleSubscriber.onSuccess(songs.get(currentSongIndex));
                }
                singleSubscriber.onError(new IllegalArgumentException("can't find song with id " + id));
            });
        });
    }

}