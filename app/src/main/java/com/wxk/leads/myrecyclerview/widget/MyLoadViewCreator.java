package com.wxk.leads.myrecyclerview.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.wxk.leads.myrecyclerview.R;

/**
 * Created by Administrator on 2017/3/21
 */

public class MyLoadViewCreator extends LoadViewCreator{

    // 加载数据的ImageView
    private TextView mLoadTv;
    private View mRefreshIv;

    @Override
    protected View getLoadView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_load_footer_view, parent, false);
        mLoadTv = (TextView) refreshView.findViewById(R.id.load_tv);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        return refreshView;
    }

    @Override
    protected void onPull(int currentHeight, int loadViewHeight, int currentLoadStatus) {

        float rotate = (float)currentHeight / loadViewHeight;
        mRefreshIv.setRotation(rotate * 360);

        if(currentLoadStatus == LoadRefreshRecyclerView.LOAD_STATUS_PULL_DOWN_LOADING){
            mLoadTv.setText("上拉加载更多...");
        }
        if(currentLoadStatus == LoadRefreshRecyclerView.LOAD_STATUS_LOOSEN_LOADING){
            mLoadTv.setText("松开加载更多...");
        }
    }

    @Override
    public void onLoading() {
        mLoadTv.setText("加载中...");

        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopLoad() {

        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
        mLoadTv.setText("上拉加载更多...");
    }
}
