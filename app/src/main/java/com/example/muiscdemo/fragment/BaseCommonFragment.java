package com.example.muiscdemo.fragment;


import com.example.muiscdemo.util.OrmUtil;
import com.example.muiscdemo.util.SharedPreferencesUtil;

/**
 * Created by smile on 02/03/2018.
 */

public abstract class BaseCommonFragment extends BaseFragment {
    protected SharedPreferencesUtil sp;
    protected OrmUtil orm;

    @Override
    protected void initViews() {
        super.initViews();

        sp = SharedPreferencesUtil.getInstance(getActivity().getApplicationContext());
        orm = OrmUtil.getInstance(getActivity().getApplicationContext());
    }


}
