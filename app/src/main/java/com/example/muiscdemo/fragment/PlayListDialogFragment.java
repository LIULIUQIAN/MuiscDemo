package com.example.muiscdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.muiscdemo.R;
import com.example.muiscdemo.adapter.PlayListAdapter;
import com.example.muiscdemo.domain.Song;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class PlayListDialogFragment extends BottomSheetDialogFragment {

    private RecyclerView rv;
    private PlayListAdapter adapter;

    private List<Song> data;
    private Song currentSong;

    private OnPlayListDialogListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_play_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        //添加Android自带的分割线
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), RecyclerView.VERTICAL);
        rv.addItemDecoration(decoration);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(layoutManager);

        adapter = new PlayListAdapter(R.layout.item_play_list, getContext());
        createHeaderView();
        rv.setAdapter(adapter);

        adapter.setCurrentSong(currentSong);
        adapter.setNewData(data);

        rv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener != null){
                    listener.onItemClick(position);
                }
            }
        });

        rv.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener != null){
                    listener.onRemoveClick(position);
                }
            }
        });

    }

    private void createHeaderView() {
        View headView = getLayoutInflater().inflate(R.layout.header_play_list, (ViewGroup) rv.getParent(), false);
        adapter.setHeaderView(headView);
    }

    public void setData(List<Song> data) {
        this.data = data;
        if (adapter != null) {
            adapter.setNewData(data);
        }
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
        if (adapter != null) {
            adapter.setCurrentSong(currentSong);
        }
    }

    public void setListener(OnPlayListDialogListener listener) {
        this.listener = listener;
    }

    public interface OnPlayListDialogListener {
        void onRemoveClick(int position);

        void onItemClick(int position);
    }
}
