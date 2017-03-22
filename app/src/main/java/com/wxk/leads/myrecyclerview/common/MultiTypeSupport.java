package com.wxk.leads.myrecyclerview.common;

/**
 * Created by Administrator on 2017/3/19
 */

public interface MultiTypeSupport<T>{

    //根据不同的位置或数据返回不同布局
    int getLayoutId(T data, int position);
}
