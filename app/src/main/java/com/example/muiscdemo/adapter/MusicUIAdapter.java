package com.example.muiscdemo.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MusicUIAdapter extends BaseFragmentPagerAdapter<Fragment> {
    private static String[] titleNames = {"推荐", "朋友", "电台"};
    public MusicUIAdapter(Context context, FragmentManager fm, int behavior) {
        super(context, fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleNames[position];
    }
}
