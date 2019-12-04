package com.example.muiscdemo.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.muiscdemo.R;
import com.example.muiscdemo.parser.domain.Line;
import com.example.muiscdemo.view.LyricLineView;

import java.util.List;

public class LyricAdapter extends BaseQuickAdapter<Object,BaseViewHolder> {

    private int index;
    private boolean isAccurate;

    public LyricAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Object item) {

        LyricLineView llv = helper.getView(R.id.llv);

        if (item instanceof String){
            llv.setLine(null);
            llv.setAccurate(false);
        }else {
            llv.setLine((Line) item);
            llv.setAccurate(isAccurate);
        }

        if (index == helper.getLayoutPosition()){
            llv.setLineSelected(true);
        }else {
            llv.setLineSelected(false);
        }

    }

    public void setSelectedIndex(int index) {
        //先刷新上一行
        notifyItemChanged(this.index);
        this.index=index;
        //刷新当前行
        notifyItemChanged(this.index);
    }

    public void setAccurate(boolean accurate) {
        isAccurate = accurate;
    }


}
