package com.example.muiscdemo.fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.MusicUIAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends BaseCommonFragment {

    private MagicIndicator tabs;
    private ViewPager vp;
private MusicUIAdapter adapter;

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        tabs = findViewById(R.id.tabs);
        vp = findViewById(R.id.vp);

        List<Fragment> list = new ArrayList<>();
        list.add(new RecommendFragment());
        list.add(new FeedFragment());
        list.add(new FMFragment());

        adapter = new MusicUIAdapter(getContext(),getChildFragmentManager(),0);
        adapter.setDatas(list);
        vp.setAdapter(adapter);

        CommonNavigator commonNavigator = new CommonNavigator(getMainActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {

                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);

                //默认颜色
                colorTransitionPagerTitleView.setNormalColor(Color.WHITE);

                //选中后的颜色
                colorTransitionPagerTitleView.setSelectedColor(Color.WHITE);

                colorTransitionPagerTitleView.setText(adapter.getPageTitle(index));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vp.setCurrentItem(index);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.WHITE);
                return indicator;
            }
        });

        commonNavigator.setAdjustMode(true);
        tabs.setNavigator(commonNavigator);
        ViewPagerHelper.bind(tabs,vp);
    }
}
