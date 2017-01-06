package com.example.java.simpleplayer.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.views.MenuInteractionListener;
import com.example.java.simpleplayer.views.MusicActivity;
import com.example.java.simpleplayer.views.MusicActivity.PlayBackInteraction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maltsev on 21.12.2016.
 */

public class MainFragment extends Fragment {

    private MenuInteractionListener mListener = null;

    private PlayBackInteraction mPlayBackInteraction;

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
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        mPlayPauseButton = (ImageView) view.findViewById(R.id.btnPlay);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager = (ViewPager) view.findViewById(R.id.pager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(2);
        }

        mPlayPauseButton.setOnClickListener(iv -> {
            if(mPlayBackInteraction == null) {
                initPlayBackInteraction();
            }
            if(mPlayBackInteraction != null) {
                if(mPlayBackInteraction.isPaused()) {
                    mPlayBackInteraction.play();
                } else {
                    mPlayBackInteraction.pause();
                }
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new SongsFragment(), "Songs");
        adapter.addFragment(new AlbumFragment(), "Albums");
        adapter.addFragment(new ArtistFragment(), "Artists");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
