package com.example.java.simpleplayer.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.views.MenuInteractionListener;
import com.example.java.simpleplayer.views.MusicActivity;

/**
 * Created by maltsev on 21.12.2016.
 */

public class MainFragment extends Fragment {

    private MenuInteractionListener mListener = null;

    private MusicActivity.PlayBackInteraction mPlayBackInteraction;

    public static final String SOME_VALUE = "SOME_VALUE";

    private ViewPager viewPager;

    private ImageView mPlayPauseButton;

    public static MainFragment newInstance(int value) {
        Bundle args = new Bundle();
        args.putInt(SOME_VALUE, value);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof MenuInteractionListener) {
            mListener = (MenuInteractionListener) activity;
        }

        if(activity instanceof MusicActivity){
            mPlayBackInteraction = ((MusicActivity)activity).getPlayBackService();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       viewPager = (ViewPager) view.findViewById(R.id.pager);
        if(viewPager!=null){
            //setupView
        }

        mPlayPauseButton.setOnClickListener(iv->){

        };

    }

    public void showText(CharSequence text) {
        final View view = getView();
        if(view == null) return;
        final TextView textView = (TextView) view.findViewById(R.id.tv);
        textView.setText(text);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
