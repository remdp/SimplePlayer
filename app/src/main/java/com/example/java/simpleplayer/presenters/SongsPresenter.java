package com.example.java.simpleplayer.presenters;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.views.MainActivity;
import com.example.java.simpleplayer.views.SongsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by java on 05.12.2016.
 */

public class SongsPresenter {

    private SongsView mView = null;

    public void onAttachToView(@NonNull SongsView songsView){
        mView = songsView;
    }

    public void loadAllSongs(){

        new AsyncTask<Void, Void, List<Song>>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(List<Song> songs) {
                super.onPostExecute(songs);
                if (mView == null) return;
                mView.onAllSongsLoaded(songs);
            }

            List<Song> songs = SongsRepository.getAllSongs(mView.getContext());

            @Override
            protected List<Song> doInBackground(Void... voids) {
                try{
                    return SongsRepository.getAllSongs(mView.getContext());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return new ArrayList<Song>();
            }
        }.execute();


    }


    public void onDetach(){
        mView = null;
    }

}
