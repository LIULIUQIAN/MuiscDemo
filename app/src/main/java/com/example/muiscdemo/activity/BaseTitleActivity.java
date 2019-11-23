package com.example.muiscdemo.activity;


import android.text.TextUtils;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.muiscdemo.R;

public class BaseTitleActivity extends BaseCommonActivity {

    protected Toolbar toolbar;

    @Override
    protected void initViews() {
        super.initViews();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableBackMenu();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (TextUtils.isEmpty(title)){
            super.setTitle(title);
        }
    }

    protected void enableBackMenu(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void policeBackMenu(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
