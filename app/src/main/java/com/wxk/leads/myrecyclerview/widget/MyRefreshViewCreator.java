package com.wxk.leads.myrecyclerview.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.wxk.leads.myrecyclerview.R;

/**
 * Created by Administrator on 2017/3/21
 */
public class MyRefreshViewCreator extends RefreshViewCreator {

    private ImageView refresh_iv;

    @Override
    protected View getRefreshView(Context context, ViewGroup parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        refresh_iv = (ImageView) refreshView.findViewById(R.id.refresh_iv);
        return refreshView;
    }

    @Override
    protected void onPull(int currentHeight, int refreshViewHeight, int currentRefreshStatus) {

        float rotate = ((float) currentHeight) / refreshViewHeight;
        refresh_iv.setRotation(rotate * 360);
    }

    @Override
    public void onRefreshing() {

        RotateAnimation rotateAnimation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(-1);
        refresh_iv.startAnimation(rotateAnimation);
    }

    @Override
    public void onStopRefresh() {

        refresh_iv.setRotation(0);
        refresh_iv.clearAnimation();
    }
}
