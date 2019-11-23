package com.example.muiscdemo.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.muiscdemo.fragment.GuideFragment;

public class GuideAdapter extends BaseFragmentPagerAdapter<GuideFragment>{

    public GuideAdapter(Context context, FragmentManager fm, int behavior) {
        super(context, fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return getData(position);
    }
}
