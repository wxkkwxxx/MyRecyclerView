package com.wxk.leads.library;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17
 */

public class BannerViewPager extends ViewPager {

    private WeakReference<Activity> mActivityReference;
    private static final String TAG = "BannerViewPager";
    private final int SCROLL_MSG = 0x001;
    //切换时间
    private int mCutDown = 4000;
    private Activity mActivity;
    private BannerAdapter mAdapter;

    private BannerScroller mBannerScroller;

    private List<View> mConvertViews;
    private static Handler mHandler;
    private boolean isAttached = false;

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        mActivity = (Activity) context;

        mBannerScroller = new BannerScroller(context);

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(this, mBannerScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mConvertViews = new ArrayList<>();

    }

    private void initHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Activity activity = mActivityReference.get();

                if(activity != null){
                    setCurrentItem(getCurrentItem() + 1);
                    startScroll();
                }
            }
        };
    }

    public void setAdapter(BannerAdapter adapter, WeakReference<Activity> mActivityReference) {

        this.mActivityReference = mActivityReference;
        initHandler();

        this.mAdapter = adapter;
        setAdapter(new BannerPageAdapter());

        setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % mAdapter.getCount());

        mActivity.getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    //开始滚动
    public void startScroll() {

        Log.e(TAG, "startScroll");
        if(mHandler != null){
            mHandler.removeMessages(SCROLL_MSG);
            mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mCutDown);
        }
    }

    //停止滚动
    public void stopScroll() {
        if(mHandler != null)
            mHandler.removeMessages(SCROLL_MSG);
    }

    //设置翻页速度
    public void setDuration(int duration) {

        this.mCutDown = duration;
    }

    //设置滚动速度
    public void setScrollerDuration(int duration){
        mBannerScroller.setScrollerDuration(duration);
    }

    //解决内存泄漏
    @Override
    protected void onDetachedFromWindow() {
        isAttached = false;
        if (mHandler != null) {
            // 销毁Handler的生命周期
            mHandler.removeMessages(SCROLL_MSG);
            // 解除绑定
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(callbacks);
            mHandler = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
        if (mAdapter != null) {
            initHandler();
            startScroll();
            // 管理Activity的生命周期
            mActivity.getApplication().registerActivityLifecycleCallbacks(callbacks);
        }
    }

    private class BannerPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mAdapter.getView(position % mAdapter.getCount(), getConvertView());
            container.addView(itemView);
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onClick(position % mAdapter.getCount());
                    }
                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            mConvertViews.add((View) object);
        }
    }

    private View getConvertView() {

        int count = mConvertViews.size();
        for (int i = 0; i < count; i++){
            View view = mConvertViews.get(i);
            if(view.getParent() == null){
                return  view;
            }
        }
        return null;
    }

    //activity生命周期的管理
    Application.ActivityLifecycleCallbacks callbacks = new DefaultActivityLifeCycleCallBacks() {

        @Override
        public void onActivityResumed(Activity activity) {
            super.onActivityResumed(activity);
            if (mActivity == activity)
                startScroll();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            super.onActivityPaused(activity);
            if (mActivity == activity)
                stopScroll();
        }
    };

    public BannerItemClickListener listener;

    public void setOnBannerItemClickListener(BannerItemClickListener listener) {
        this.listener = listener;
    }

    public interface BannerItemClickListener{

        void onClick(int position);
    }
}
