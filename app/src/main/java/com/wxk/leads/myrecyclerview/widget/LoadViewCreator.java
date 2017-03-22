package com.wxk.leads.myrecyclerview.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/21
 */

public abstract class LoadViewCreator {

    //获取上拉加载的view
    protected abstract View getLoadView(Context context, ViewGroup parent);

    //尚在上拉
    protected abstract void onPull(int currentHeight, int loadViewHeight, int currentLoadStatus);

    //正在加载中
    public abstract void onLoading();

    //停止加载
    public abstract void onStopLoad();
}
