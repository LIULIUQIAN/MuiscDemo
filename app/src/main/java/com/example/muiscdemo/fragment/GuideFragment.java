package com.example.muiscdemo.fragment;


import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.muiscdemo.R;
import com.example.muiscdemo.util.Consts;
import com.example.muiscdemo.util.ImageUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends BaseCommonFragment {

    ImageView iv;
    private int imageId;

    public static GuideFragment newInstance(int imageId) {
       Bundle args = new Bundle();
       args.putSerializable(Consts.ID,imageId);
       GuideFragment fragment = new GuideFragment();
       fragment.setArguments(args);
       return fragment;
    }


    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    protected void initViews() {
        super.initViews();
        iv = findViewById(R.id.iv);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        imageId = getArguments().getInt(Consts.ID,-1);
        if (imageId == -1){
            getMainActivity().finish();
            return;
        }

        ImageUtil.showLocalImage(getMainActivity(),iv,imageId);

    }
}
