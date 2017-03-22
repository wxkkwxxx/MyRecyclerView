package com.wxk.leads.library;

import android.view.View;

/**
 * Created by Administrator on 2017/3/17
 */

public abstract class BannerAdapter {

    //提供视图
    public abstract View getView(int position, View convertView);

    public abstract int getCount();

    public String getBannerDesc(int position){
        return "";
    }
}
