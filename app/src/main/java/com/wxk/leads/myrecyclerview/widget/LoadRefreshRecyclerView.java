package com.wxk.leads.myrecyclerview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/3/21
 */

public class LoadRefreshRecyclerView extends RefreshRecyclerView{

    // 上拉加载更多的辅助类
    private LoadViewCreator mLoadCreator;

    // 上拉加载更多头部的高度
    private int mLoadViewHeight = 0;
    // 上拉加载更多的头部View
    private View mLoadView;

    // 手指按下的Y位置
    private int mFingerDownY;

    //是否正在拖拽
    private boolean isDrag;

    //当前状态
    private int mCurrentStatus;
    //默认状态
    public static int LOAD_STATUS_NORMAL = 0x0001;
    //上拉加载更多状态
    public static int LOAD_STATUS_PULL_DOWN_LOADING = 0x0002;
    //松开加载更多状态
    public static int LOAD_STATUS_LOOSEN_LOADING = 0x0003;
    //正在加载更多状态
    public static int LOAD_STATUS_LOADING = 0x0004;

    public LoadRefreshRecyclerView(Context context) {
        super(context);
    }

    public LoadRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addLoadViewCreator(LoadViewCreator loadViewCreator){
        this.mLoadCreator = loadViewCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    //添加底部加载更多View
    private void addRefreshView() {

        RecyclerView.Adapter adapter = getAdapter();
        if(adapter != null && mLoadCreator != null){

            View loadView = mLoadCreator.getLoadView(getContext(), this);
            if(loadView != null){
                addFooterView(loadView);
                this.mLoadView = loadView;
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action){

            case MotionEvent.ACTION_DOWN:

                mFingerDownY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:

                if(isDrag){

                    restoreLoadView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int action = e.getAction();
        switch (action){

            case MotionEvent.ACTION_MOVE:

                if(canScrollDown() || mCurrentStatus == LOAD_STATUS_LOADING
                        || mLoadView == null || mLoadCreator == null){

                    Log.e("--=-=-=-=-","到底了，到底了");
                    return super.onTouchEvent(e);
                }

                if(mLoadCreator != null){

                    mLoadViewHeight = mLoadView.getMeasuredHeight();
                }

                if(isDrag){

                    scrollToPosition(getAdapter().getItemCount() - 1);
                }

                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);

                if(distanceY < 0){
                    Log.d("========",-distanceY+"=-=-");
                    setLoadViewMarginBottom(-distanceY);
                    updateLoadStatus(-distanceY);
                    isDrag = true;
                    return true;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    //重置上拉状态
    private void restoreLoadView(){

        int currentBottomMargin = ((MarginLayoutParams)mLoadView.getLayoutParams()).bottomMargin;
        int finalMargin = 0;
        if(mCurrentStatus == LOAD_STATUS_LOOSEN_LOADING){
            mCurrentStatus = LOAD_STATUS_LOADING;
            if(mLoadCreator != null){
                mLoadCreator.onLoading();
            }
            if(mListener != null){
                mListener.onLoad();
            }
        }

        int distance = currentBottomMargin - finalMargin;
        ValueAnimator animator = ValueAnimator.ofFloat(currentBottomMargin, finalMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentBottomMargin = (float) animation.getAnimatedValue();
                setLoadViewMarginBottom((int) currentBottomMargin);
            }
        });
        animator.start();
        isDrag = false;
    }

    private void updateLoadStatus(int distanceY){

        if(distanceY <= 0){
            mCurrentStatus = LOAD_STATUS_NORMAL;
        }else if(distanceY < mLoadViewHeight){
            mCurrentStatus = LOAD_STATUS_PULL_DOWN_LOADING;
        }else {
            mCurrentStatus = LOAD_STATUS_LOOSEN_LOADING;
        }

        if(mLoadCreator != null){
            mLoadCreator.onPull(distanceY, mLoadViewHeight, mCurrentStatus);
        }
    }

    public void setLoadViewMarginBottom(int marginBottom){

        MarginLayoutParams params = (MarginLayoutParams) mLoadView.getLayoutParams();
        if(marginBottom < 0){

            marginBottom = 0;
        }
        params.bottomMargin = marginBottom;
        mLoadView.requestLayout();
    }

    //判断是不是滚动到了最顶部，
    public boolean canScrollDown() {
        return ViewCompat.canScrollVertically(this, 1);
    }

    //停止加载更多
    public void onStopLoad(){
        mCurrentStatus = LOAD_STATUS_NORMAL;
        restoreLoadView();
        if(mLoadCreator != null){
            mLoadCreator.onStopLoad();
        }
    }

    // 处理加载更多回调监听
    private OnLoadMoreListener mListener;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    public interface OnLoadMoreListener {
        void onLoad();
    }
}
