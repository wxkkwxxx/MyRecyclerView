package com.wxk.leads.myrecyclerview.common;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/19
 */
public class CommonViewHolder extends RecyclerView.ViewHolder{

    private SparseArrayCompat<View> mViews;

    public CommonViewHolder(View itemView) {
        super(itemView);

        mViews = new SparseArrayCompat<>();
    }

    //获取view
    public <T extends View>T getView(int viewId){

        View view = mViews.get(viewId);
        if(view == null){
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 链式调用
     */
    //设置文本
    public CommonViewHolder setText(int viewId, String text){

        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    //设置View的Visibility
    public CommonViewHolder setViewVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
        return this;
    }

    //本地图片
    public CommonViewHolder setImageResource(int viewId, int resId){

        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    //网络图片
    public CommonViewHolder setImagePath(int viewId, HolderImageLoader imageLoader){

        ImageView imageView = getView(viewId);
        if (imageLoader == null) {
            throw new NullPointerException("imageLoader is null!");
        }
        imageLoader.loadImage(imageView.getContext(), imageView, imageLoader.getPath());
        return this;
    }

    public CommonViewHolder setOnClickListener(int viewId, View.OnClickListener listener){

        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public CommonViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener){

        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public abstract static class HolderImageLoader{

        private String path;

        public HolderImageLoader(String path) {
            this.path = path;
        }

        public abstract void loadImage(Context context, ImageView imageView, String path);

        public String getPath() {
            return path;
        }
    }
}
