package com.wxk.leads.myrecyclerview.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/21
 */

public abstract class RefreshViewCreator {

    //获取下拉刷新的view
    protected abstract View getRefreshView(Context context, ViewGroup parent);

    //正在下拉
    protected abstract void onPull(int currentHeight, int refreshViewHeight, int currentRefreshStatus);

    //正在刷新中
    public abstract void onRefreshing();

    //停止刷新
    public abstract void onStopRefresh();
}
