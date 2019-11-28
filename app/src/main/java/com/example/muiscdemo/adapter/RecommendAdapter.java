package com.example.muiscdemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.example.muiscdemo.R;
import com.example.muiscdemo.domain.RecommendedTitle;


import java.util.List;


public class RecommendAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity,BaseViewHolder> {

    public static final int TYPE_TITLE = 3;
    public static final int TYPE_LIST = 1;
    public static final int TYPE_SONG = 2;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public RecommendAdapter(List data) {
        super(data);
        addItemType(TYPE_TITLE, R.layout.item_title);
        addItemType(TYPE_LIST, R.layout.item_list);
        addItemType(TYPE_SONG, R.layout.item_song);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MultiItemEntity item) {

        switch (helper.getItemViewType()){
            case TYPE_TITLE:
                RecommendedTitle titleModel = (RecommendedTitle) item;
                helper.setText(R.id.tv_title,titleModel.getTitle());
                break;
            case TYPE_LIST:
                helper.setText(R.id.tv_title,"22222222222");
                break;
            case TYPE_SONG:
                helper.setText(R.id.tv_title,"333333333333");
                break;

        }

    }


    class MBaseViewHolder extends BaseViewHolder {

        public MBaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }


//
//    private class TitleViewHolder extends MBaseViewHolder {
//
//        private TextView tv_title;
//
//        public TitleViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tv_title = (TextView) findViewById(R.id.tv_title);
//        }
//
//        @Override
//        public void bindData(Object data) {
//            tv_title.setText(data.toString());
//        }
//    }
//
//    private class ListViewHolder extends MBaseViewHolder {
//        private ImageView iv_banner;
//        private TextView tv_count;
//        private TextView tv_title;
//
//        public ListViewHolder(@NonNull View itemView) {
//            super(itemView);
//            iv_banner = (ImageView) findViewById(R.id.iv_banner);
//            tv_count = (TextView) findViewById(R.id.tv_count);
//            tv_title = (TextView) findViewById(R.id.tv_title);
//        }
//
//        @Override
//        public void bindData(Object data) {
//            playlist d = (playlist) data;
//            ImageUtil.show((Activity) context, iv_banner, d.getBanner());
//            tv_count.setText(String.valueOf(d.getClicks_count()));
//            tv_title.setText(d.getTitle());
//        }
//    }
//
//    private class SongViewHolder extends MBaseViewHolder {
//        private final ImageView iv_avatar;
//        private final TextView tv_nickname;
//        private ImageView iv_icon;
//        private TextView tv_title;
//        private TextView tv_comment_count;
//
//        public SongViewHolder(View itemView) {
//            super(itemView);
//            iv_icon = (ImageView) findViewById(R.id.iv_icon);
//            iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
//            tv_title = (TextView) findViewById(R.id.tv_title);
//            tv_nickname = (TextView) findViewById(R.id.tv_nickname);
//            tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);
//        }
//
//        @Override
//        public void bindData(Object data) {
//            Song d = (Song) data;
//            ImageUtil.show((Activity) context, iv_icon, d.getBanner());
//            tv_title.setText(d.getTitle());
//            tv_nickname.setText(d.getArtist().getNickname());
//            tv_comment_count.setText(String.valueOf(d.getComments_count()));
//        }
//    }


//    public static final int TYPE_TITLE = 0;
//    public static final int TYPE_LIST = 1;
//    public static final int TYPE_SONG = 2;
//
//    public RecommendAdapter(Context context) {
//        super(context);
//    }
//
//    @NonNull
//    @Override
//    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//        if (viewType == TYPE_LIST){
//            return new ListViewHolder(getInflater().inflate(R.layout.item_list,parent,false));
//        }else if (viewType == TYPE_SONG){
//            return new SongViewHolder(getInflater().inflate(R.layout.item_song,parent,false));
//        }else {
//            return new TitleViewHolder(getInflater().inflate(R.layout.item_title,parent,false));
//        }
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        Object data = getData(position);
//        if (data instanceof playlist){
//            return TYPE_LIST;
//        }else if (data instanceof Song){
//            return TYPE_SONG;
//        }else {
//            return TYPE_TITLE;
//        }
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
//        holder.bindData(getData(position));
//    }
//
//    @Override
//    public int setSpanSizeLookup(int position) {
//        int viewType = getItemViewType(position);
//
//        if (viewType == TYPE_LIST){
//            return 1;
//        }else {
//            return 3;
//        }
//    }
//
//
//
//    abstract class BaseViewHolder extends BaseRecyclerViewAdapter.ViewHolder{
//
//        public BaseViewHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//
//        public abstract void bindData(Object data);
//    }
//
//
//    private class TitleViewHolder extends BaseViewHolder{
//
//        private TextView tv_title;
//
//        public TitleViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tv_title = (TextView) findViewById(R.id.tv_title);
//        }
//
//        @Override
//        public void bindData(Object data) {
//            tv_title.setText(data.toString());
//        }
//    }
//
//    private class ListViewHolder extends BaseViewHolder{
//        private ImageView iv_banner;
//        private TextView tv_count;
//        private TextView tv_title;
//
//        public ListViewHolder(@NonNull View itemView) {
//            super(itemView);
//            iv_banner = (ImageView) findViewById(R.id.iv_banner);
//            tv_count = (TextView) findViewById(R.id.tv_count);
//            tv_title = (TextView) findViewById(R.id.tv_title);
//        }
//
//        @Override
//        public void bindData(Object data) {
//            playlist d = (playlist) data;
//            ImageUtil.show((Activity) context, iv_banner, d.getBanner());
//            tv_count.setText(String.valueOf(d.getClicks_count()));
//            tv_title.setText(d.getTitle());
//        }
//    }
//
//    private class SongViewHolder extends BaseViewHolder {
//        private final ImageView iv_avatar;
//        private final TextView tv_nickname;
//        private ImageView iv_icon;
//        private TextView tv_title;
//        private TextView tv_comment_count;
//
//        public SongViewHolder(View itemView) {
//            super(itemView);
//            iv_icon = (ImageView) findViewById(R.id.iv_icon);
//            iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
//            tv_title = (TextView) findViewById(R.id.tv_title);
//            tv_nickname = (TextView) findViewById(R.id.tv_nickname);
//            tv_comment_count = (TextView) findViewById(R.id.tv_comment_count);
//        }
//
//        @Override
//        public void bindData(Object data) {
//            Song d = (Song) data;
//            ImageUtil.show((Activity) context, iv_icon, d.getBanner());
//            tv_title.setText(d.getTitle());
//            tv_nickname.setText(d.getArtist().getNickname());
//            tv_comment_count.setText(String.valueOf(d.getComments_count()));
//        }
//    }

}
