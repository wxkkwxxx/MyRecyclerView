package com.wxk.leads.myrecyclerview.use;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.wxk.leads.myrecyclerview.R;
import com.wxk.leads.myrecyclerview.common.CommonRecyclerAdapter;
import com.wxk.leads.myrecyclerview.common.CommonViewHolder;
import com.wxk.leads.myrecyclerview.widget.LoadRefreshRecyclerView;
import com.wxk.leads.myrecyclerview.widget.MyLoadViewCreator;
import com.wxk.leads.myrecyclerview.widget.MyRefreshViewCreator;
import com.wxk.leads.myrecyclerview.widget.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21
 */

public class LoadRefreshActivity extends AppCompatActivity implements RefreshRecyclerView.OnRefreshListener, LoadRefreshRecyclerView.OnLoadMoreListener {

    private HomeAdapter mAdapter;
    private LoadRefreshRecyclerView refresh_view;
    private List<String> mDatas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);

        mDatas = new ArrayList<>();
        initData();

        refresh_view = (LoadRefreshRecyclerView) findViewById(R.id.refresh_view);
        refresh_view.setLayoutManager(new LinearLayoutManager(this));
        refresh_view.addRefreshViewCreator(new MyRefreshViewCreator());
        refresh_view.addLoadViewCreator(new MyLoadViewCreator());

        refresh_view.addEmptyView(findViewById(R.id.empty_view));
        refresh_view.addLoadingView(findViewById(R.id.load_view));

        refresh_view.setOnRefreshListener(this);
        refresh_view.setOnLoadMoreListener(this);

        refresh_view.postDelayed(new Runnable() {
            @Override
            public void run() {

                mAdapter = new HomeAdapter(LoadRefreshActivity.this, mDatas);
                refresh_view.setAdapter(mAdapter);
            }
        }, 1000);
    }

    private void initData(){
        for (int i = 'A'; i <= 'Z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                refresh_view.onStopRefresh();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
                refresh_view.onStopLoad();
                mAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    class HomeAdapter extends CommonRecyclerAdapter<String> {

        public HomeAdapter(Context context, List<String> data) {
            super(context, R.layout.basic_item ,data);
        }

        @Override
        protected void covert(CommonViewHolder holder, String s, int position) {
            holder.setText(R.id.tv_text, s);
        }
    }
}
