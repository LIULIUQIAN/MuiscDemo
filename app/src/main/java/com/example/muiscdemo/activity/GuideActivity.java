package com.example.muiscdemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.muiscdemo.MainActivity;
import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.GuideAdapter;
import com.example.muiscdemo.fragment.GuideFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class GuideActivity extends BaseCommonActivity {

    @BindView(R.id.vp)
    ViewPager vp;

    @BindView(R.id.indicator)
    CircleIndicator indicator;

    private GuideAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        adapter = new GuideAdapter(getActivity(), getSupportFragmentManager(),0);
        vp.setAdapter(adapter);

        indicator.setViewPager(vp);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());

        ArrayList<GuideFragment> datas = new ArrayList<>();
        datas.add(GuideFragment.newInstance(R.drawable.guide1));
        datas.add(GuideFragment.newInstance(R.drawable.guide2));
        datas.add(GuideFragment.newInstance(R.drawable.guide3));
        adapter.setDatas(datas);
    }

    @OnClick(R.id.bt_login_or_register)
    public void onLogin(){
        startActivityAfterFinishThis(LoginActivity.class);
    }

    @OnClick(R.id.bt_enter)
    public void onEnter(){
        startActivityAfterFinishThis(MainActivity.class);
    }
}
