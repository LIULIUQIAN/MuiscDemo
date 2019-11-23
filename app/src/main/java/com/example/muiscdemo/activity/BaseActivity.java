package com.example.muiscdemo.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.example.muiscdemo.R;
import com.example.muiscdemo.util.Consts;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        init();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        init();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 找控件
     */
    protected void initViews() {
    }

    /**
     * 设置数据
     */
    protected void initDatas() {

    }

    /**
     * 绑定监听器
     */
    protected void initListener() {
    }

    private void init() {
        initViews();
        initDatas();
        initListener();
    }

    protected void startActivity(Class<?> clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    protected void startActivityAfterFinishThis(Class<?> clazz) {
        startActivity(new Intent(getActivity(), clazz));
        finish();
    }

    protected void startActivityExtraId(Class<?> clazz, String id) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(Consts.ID, id);
        startActivity(intent);
    }

    protected void startActivityExtraString(Class<?> clazz, String string) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(Consts.STRING, string);
        startActivity(intent);
    }

    protected BaseActivity getActivity() {
        return this;
    }

    public void showLoading() {
        showLoading(getResources().getString(R.string.loading));
    }

    public void showLoading(String message) {
//        if (getActivity() != null && !getActivity().isFinishing()) {
//            progressDialog = new ProgressDialog(this);
//            progressDialog.setTitle("提示");
//            progressDialog.setMessage(message);
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
    }

    public void showLoading(int resId) {
        showLoading(getResources().getString(resId));
    }

    public void hideLoading() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.hide();
//        }
    }

}
