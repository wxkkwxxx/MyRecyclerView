package com.wxk.leads.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/3/17
 */

public class IndicatorView extends View{

    private Drawable mDrawable;

    public IndicatorView(Context context) {
        super(context);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap bitmap = drawable2bitmap(mDrawable);
        Bitmap circleBitmap = getCircleBitmap(bitmap);

        canvas.drawBitmap(circleBitmap, 0, 0, null);
//        mDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
//        mDrawable.draw(canvas);
    }

    //drawable转bitmap http://blog.csdn.net/hezhipin610039/article/details/7899248/
    private Bitmap drawable2bitmap(Drawable mDrawable) {
        if(mDrawable instanceof BitmapDrawable){
            return ((BitmapDrawable)mDrawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mDrawable.draw(canvas);
        return bitmap;
    }

    private Bitmap getCircleBitmap(Bitmap bitmap){

        // 创建一个Bitmap
        Bitmap circleBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        //防抖动
        paint.setDither(true);

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredWidth() / 2, paint);

       //取圆和bitmap的交警区域
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //绘制bitmap
        canvas.drawBitmap(bitmap, 0, 0, paint);

        //回收bitmap
        bitmap.recycle();
        bitmap = null;
        return circleBitmap;
    }

    public void setDrawable(Drawable drawable){
        this.mDrawable = drawable;
        invalidate();
    }
}
