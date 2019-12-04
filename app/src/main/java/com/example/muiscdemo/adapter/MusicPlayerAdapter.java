package com.example.muiscdemo.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


public class MusicPlayerAdapter extends BaseFragmentPagerAdapter<Fragment> {
    public MusicPlayerAdapter(Context context, FragmentManager fm, int behavior) {
        super(context, fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {
        return getData(position);
    }
}
