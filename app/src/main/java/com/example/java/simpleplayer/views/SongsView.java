package com.example.java.simpleplayer.views;

import android.content.Context;

import com.example.java.simpleplayer.models.Song;

import java.util.List;


public interface SongsView {

    Context getContext();

     void onAllSongsLoaded(List<Song> songList);
}
