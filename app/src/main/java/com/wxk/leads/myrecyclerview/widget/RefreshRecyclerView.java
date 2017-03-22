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

import com.wxk.leads.myrecyclerview.common.WrapRecyclerView;

/**
 * Created by Administrator on 2017/3/21
 */

public class RefreshRecyclerView extends WrapRecyclerView{

    // 下拉刷新的辅助类
    private RefreshViewCreator mRefreshCreator;

    //下拉刷新view的高度
    private int mRefreshViewHeight;
    //下拉刷新的view
    private View mRefreshView;

    //手指按下的位置
    private int mFingerDownY;

    //是否在拖动
    private boolean isDrag = false;

    //当前状态
    private int mCurrentRefreshStatus;

    // 手指拖拽的阻力指数
    protected float mDragIndex = 0.35f;

    //普通的状态
    private int REFRESH_STATUS_NORMAL = 0x0001;
    //下拉刷新的状态
    public int REFRESH_STATUS_PULL_DOWN = 0X0002;
    //松开刷新的状态
    public int REFRESH_STATUS_LOOSEN_REFRESH = 0X0003;
    //正在刷新的状态
    public int REFRESH_STATUS_REFRESH = 0X0004;

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addRefreshViewCreator(RefreshViewCreator refreshViewCreator){
        this.mRefreshCreator = refreshViewCreator;
        addRefreshView();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }

    private void addRefreshView() {

        RecyclerView.Adapter adapter = getAdapter();
        if(adapter != null && mRefreshCreator != null){
            View refreshView = mRefreshCreator.getRefreshView(getContext(), this);
            if(refreshView != null){
                addHeaderView(refreshView);
                this.mRefreshView = refreshView;
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

                if (isDrag) {
                    restoreRefreshView();
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

                //以下四种情况不做处理,没有到达最顶端,即还可以向上滚动
                if(canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESH
                        || mRefreshView == null || mRefreshCreator == null){
                    return super.onTouchEvent(e);
                }

                // 解决下拉刷新自动滚动问题
                if (isDrag) {
                    scrollToPosition(0);
                }

                // 获取手指触摸拖拽的距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);

                Log.d("========",+distanceY+"=-=-");
                //如果已经到达头部,不断的向下拉不断的改变refreshView的marginTop的值
                if (distanceY > 0) {
                    int marginTop = distanceY - mRefreshViewHeight;
                    Log.e("========",marginTop+"=-=-");
                    setRefreshViewMarginTop(marginTop);
                    updateRefreshStatus(distanceY);
                    isDrag = true;
                    return false;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 更新刷新的状态
     */
    private void updateRefreshStatus(int distanceY) {
        if (distanceY <= 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (distanceY < mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESH;
        }

        if (mRefreshCreator != null) {
            mRefreshCreator.onPull(distanceY, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    //重置刷新状态
    private void restoreRefreshView(){

        int currentMargin = ((MarginLayoutParams)mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        if(mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESH){
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESH;
            if (mRefreshCreator != null) {
                mRefreshCreator.onRefreshing();
            }
            if (mListener != null) {
                mListener.onRefresh();
            }
        }

        int distance = currentMargin - finalTopMargin;

        ValueAnimator animator = ValueAnimator.ofFloat(currentMargin, finalTopMargin).setDuration(distance);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentTopMargin = (float) animation.getAnimatedValue();
                setRefreshViewMarginTop((int) currentTopMargin);
            }
        });
        animator.start();
        isDrag = false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(mRefreshView != null && mRefreshViewHeight <= 0){
            mRefreshViewHeight = mRefreshView.getMeasuredHeight();
            if(mRefreshViewHeight > 0){
                //隐藏刷新的view
                setRefreshViewMarginTop(-mRefreshViewHeight + 1);
            }
        }
    }

    //设置刷新view的marginTop
    private void setRefreshViewMarginTop(int marginTop){

        MarginLayoutParams params = (MarginLayoutParams) mRefreshView.getLayoutParams();
        if(marginTop < -mRefreshViewHeight + 1){
            marginTop = -mRefreshViewHeight + 1;
        }
        params.topMargin = marginTop;
        mRefreshView.requestLayout();
    }

//    /**
//     * @return Whether it is possible for the child view of this layout to
//     *         scroll up. Override this if the child view is a custom view.
//     *         是否滚动到了最顶部
//     */
//    private boolean canScrollUp(){
//
////        public boolean canChildScrollUp() {
////            if (mChildScrollUpCallback != null) {
////                return mChildScrollUpCallback.canChildScrollUp(this, mTarget);
////            }
////            if (android.os.Build.VERSION.SDK_INT < 14) {
////                if (mTarget instanceof AbsListView) {
////                    final AbsListView absListView = (AbsListView) mTarget;
////                    return absListView.getChildCount() > 0
////                            && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
////                            .getTop() < absListView.getPaddingTop());
////                } else {
////                    return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
////                }
////            } else {
////                return ViewCompat.canScrollVertically(mTarget, -1);
////            }
////        }
//        if(android.os.Build.VERSION.SDK_INT < 14){
//            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
//        }else {
//            return ViewCompat.canScrollVertically(this, -1);
//        }
//    }

    private boolean canScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            return ViewCompat.canScrollVertically(this, -1) || this.getScrollY() > 0;
        } else {
            return ViewCompat.canScrollVertically(this, -1);
        }
    }

    //停止刷新
    public void onStopRefresh(){
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if(mRefreshCreator != null){
            mRefreshCreator.onStopRefresh();
        }
    }

    // 处理刷新回调监听
    private OnRefreshListener mListener;

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
}
