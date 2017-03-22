package com.wxk.leads.myrecyclerview.decotation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/3/19
 */
public class CommonItemDecoration extends RecyclerView.ItemDecoration{

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable itemDrawable;

    //使用配置在style中默认的分割线样式
    public CommonItemDecoration(Context context){
        TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        itemDrawable = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    //图片 or xml
    public CommonItemDecoration(Context context, int drawableResId) {
        itemDrawable = ContextCompat.getDrawable(context, drawableResId);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        //源码中此方法获取item位置
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        int bottom = itemDrawable.getIntrinsicHeight();
        int right  = itemDrawable.getIntrinsicWidth();

        if(isLastColumn(parent, itemPosition)){

            right = 0;
        }

        if(isLastRow(parent, itemPosition)){

            bottom = 0;
        }

        outRect.set(0, 0, right, bottom);
    }

    //最后一列
    private boolean isLastColumn(RecyclerView parent, int position) {

        int spanCount = getSpanCount(parent);

        return (position + 1) % spanCount == 0;
    }

    //最后一行
    private boolean isLastRow(RecyclerView parent, int position) {

        int spanCount = getSpanCount(parent);

        //获取行数
        int row = parent.getAdapter().getItemCount() % spanCount == 0
                ? parent.getAdapter().getItemCount() / spanCount : (parent.getAdapter().getItemCount() % spanCount + 1);
        //当前位置 > (行数 - 1 * 列数)
        return (position + 1) > (row - 1) * spanCount;
    }

    //获取列数,默认为linearLayout返回1
    public int getSpanCount(RecyclerView parent){

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){

            GridLayoutManager Manager = (GridLayoutManager)layoutManager;
            return Manager.getSpanCount();
        }
        return 1;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int count = parent.getChildCount();
        drawHorizontal(c, parent, count);
        drawVertical(c, parent, count);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent, int count) {

        for (int i = 0; i < count; i++) {

            View child = parent.getChildAt(i);
            //处理item有margin时的bug
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            int right = child.getRight() + itemDrawable.getIntrinsicWidth() + params.rightMargin;
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + itemDrawable.getIntrinsicHeight();
            itemDrawable.setBounds(left, top, right, bottom);
            itemDrawable.draw(c);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent, int count) {

        for (int i = 0; i < count; i++) {

            View child = parent.getChildAt(i);
            //处理item有margin时的bug
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + itemDrawable.getIntrinsicWidth();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin ;
            itemDrawable.setBounds(left, top, right, bottom);
            itemDrawable.draw(c);
        }
    }
}
