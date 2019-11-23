package com.example.muiscdemo.adapter;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragmentPagerAdapter<T> extends FragmentPagerAdapter {

    protected final Context context;
    protected final List<T> datas = new ArrayList<T>();

    public BaseFragmentPagerAdapter(Context context, FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }


    public void setDatas(List<T> data) {
        if (data != null && data.size() > 0) {
            datas.clear();
            datas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addDatas(List<T> data) {
        if (data != null && data.size() > 0) {
            datas.addAll(data);
            notifyDataSetChanged();
        }
    }

    public T getData(int position) {
        return datas.get(position);
    }
}
