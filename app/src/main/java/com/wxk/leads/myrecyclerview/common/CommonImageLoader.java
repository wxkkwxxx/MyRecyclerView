package com.wxk.leads.myrecyclerview.common;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wxk.leads.myrecyclerview.R;

/**
 * Created by Administrator on 2017/3/19
 */

/**
 * 解耦图片加载，因为考虑加载图片的第三方可能不太一样
 */
public class CommonImageLoader extends CommonViewHolder.HolderImageLoader{

    public CommonImageLoader(String path) {
        super(path);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String path) {
        Glide.with(context).load(path)
                .placeholder(R.mipmap.ic_launcher).into(imageView);
    }
}
