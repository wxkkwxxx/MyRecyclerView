package com.wxk.leads.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/3/17
 */

public class BannerView extends RelativeLayout{

    private BannerViewPager banner_vp;

    private RelativeLayout banner_bottom_layout;

    private TextView banner_desc_tv;

    private LinearLayout banner_ll_layout;

    private BannerAdapter mAdapter;

    private Context mContext;

    //drawable用法: http://blog.csdn.net/lmj623565791/article/details/43752383
    //选中的指示器
    private Drawable mSelectedDrawable;
    //常态的指示器
    private Drawable mNormalDrawable;
    //当前选中的点
    private int mCurrentPos;

    //位置:默认右边
    private int mDotGravity = 1;
    //点的大小,默认8dp
    private int mDotSize = 8;
    //点的间距,默认8dp
    private int mDotDistance = 8;
    //底部容器颜色,默认透明
    private int mBottomColor = Color.TRANSPARENT;

    private float mWidthProportion = 2.0f;
    private float mHeightProportion = 1.0f;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        inflate(context, R.layout.banner_view, this);

        initAttribute(attrs);
    }

    private void initAttribute(AttributeSet attrs) {

        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.BannerView);

        mSelectedDrawable = typedArray.getDrawable(R.styleable.BannerView_indicatorFocus);
        if(mSelectedDrawable == null){
            mSelectedDrawable = new ColorDrawable(Color.RED);
        }
        mNormalDrawable = typedArray.getDrawable(R.styleable.BannerView_indicatorNormal);
        if(mNormalDrawable == null){
            mNormalDrawable = new ColorDrawable(Color.WHITE);
        }

        mDotSize = (int) typedArray.getDimension(R.styleable.BannerView_dotSize, dip2px(mDotSize));
        mDotDistance = (int) typedArray.getDimension(R.styleable.BannerView_dotDistance, dip2px(mDotDistance));
        mBottomColor = typedArray.getColor(R.styleable.BannerView_bottomBg, mBottomColor);
        mDotGravity = typedArray.getInt(R.styleable.BannerView_dotGravity, mDotGravity);

        mWidthProportion = typedArray.getFloat(R.styleable.BannerView_widthProportion, mWidthProportion);
        mHeightProportion = typedArray.getFloat(R.styleable.BannerView_heightProportion, mHeightProportion);

        typedArray.recycle();
        initBanner();
    }

    private void initBanner() {

        banner_vp = (BannerViewPager) findViewById(R.id.banner_vp);
        banner_bottom_layout = (RelativeLayout) findViewById(R.id.banner_bottom_layout);
        banner_desc_tv = (TextView) findViewById(R.id.banner_desc_tv);
        banner_ll_layout = (LinearLayout) findViewById(R.id.banner_ll_container);
        banner_bottom_layout.setBackgroundColor(mBottomColor);
    }

    //设置适配器
    public void setAdapter(BannerAdapter adapter, WeakReference<Activity> mActivityReference){

        this.mAdapter = adapter;
        banner_vp.setAdapter(adapter, mActivityReference);

        initIndicator();

        banner_vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {

                pagerSelected(position);
            }
        });

        //初始化数据
        String bannerDesc = mAdapter.getBannerDesc(0);
        banner_desc_tv.setText(bannerDesc);

        if(mWidthProportion == 0 || mHeightProportion == 0){
            return;
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int width = getMeasuredWidth();

                int height = (int) ((width * mHeightProportion) / mWidthProportion);
                getLayoutParams().height = height;
                requestLayout();
            }
        });
    }

    //初始化圆点
    private void initIndicator() {

        banner_ll_layout.setGravity(getGravity());
//        RelativeLayout.LayoutParams layoutParams = (LayoutParams) banner_ll_layout.getLayoutParams();
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        banner_ll_layout.setLayoutParams(layoutParams);
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++){

            IndicatorView view = new IndicatorView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            params.leftMargin = mDotDistance;
            view.setLayoutParams(params);
            //默认一个为选中状态
            if(i == 0){
                view.setDrawable(mSelectedDrawable);
            }else {
                view.setDrawable(mNormalDrawable);
            }
            banner_ll_layout.addView(view);
        }
    }

    public int getGravity() {
        switch (mDotGravity){
            case 0:
                return Gravity.CENTER;
            case 1:
                return Gravity.RIGHT;
            case -1:
                return Gravity.LEFT;
        }
        return Gravity.RIGHT;
    }

    private void pagerSelected(int position) {

        IndicatorView oldView = (IndicatorView) banner_ll_layout.getChildAt(mCurrentPos);
        oldView.setDrawable(mNormalDrawable);

        mCurrentPos = position % mAdapter.getCount();
        IndicatorView selectedView = (IndicatorView) banner_ll_layout.getChildAt(mCurrentPos);
        selectedView.setDrawable(mSelectedDrawable);

        String bannerDesc = mAdapter.getBannerDesc(mCurrentPos);
        banner_desc_tv.setText(bannerDesc);
    }

    //开始滚动
    public void startScroll(){

        banner_vp.startScroll();
    }

    //停止滚动
    public void stopScroll(){

        banner_vp.stopScroll();
    }

    //色织翻页速度
    public void setDuration(int duration){

        banner_vp.setDuration(duration);
    }

    //设置滚动速度
    public void setScrollerDuration(int duration){

        banner_vp.setScrollerDuration(duration);
    }

    public void setOnBannerItemClickListener(BannerViewPager.BannerItemClickListener listener){

        banner_vp.setOnBannerItemClickListener(listener);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        switch (action){

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                stopScroll();
                break;
            case MotionEvent.ACTION_UP:

                startScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int dip2px(int dip){

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }
}
