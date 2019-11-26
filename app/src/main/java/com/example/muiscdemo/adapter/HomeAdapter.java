package com.example.muiscdemo.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class HomeAdapter extends BaseFragmentPagerAdapter<Fragment> {
    public HomeAdapter(Context context, FragmentManager fm, int behavior) {
        super(context, fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }
}
