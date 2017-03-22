package com.wxk.leads.myrecyclerview.use;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wxk.leads.library.BannerAdapter;
import com.wxk.leads.library.BannerView;
import com.wxk.leads.myrecyclerview.R;
import com.wxk.leads.myrecyclerview.common.CommonRecyclerAdapter;
import com.wxk.leads.myrecyclerview.common.CommonViewHolder;
import com.wxk.leads.myrecyclerview.common.WrapRecyclerView;
import com.wxk.leads.myrecyclerview.decotation.CommonItemDecoration;
import com.wxk.leads.myrecyclerview.entity.BannerEntity;
import com.wxk.leads.myrecyclerview.listener.OnItemClickListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21
 */

public class HeaderFooterActivity extends AppCompatActivity{

    private View footer;
    private List<String> datas;
    private List<BannerEntity> entities;
    private WrapRecyclerView recycler_view;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap);

        inflater = LayoutInflater.from(this);

        entities = new ArrayList<>();
        datas = new ArrayList<>();
        for (int i = 'A'; i <= 'Z'; i++) {
            datas.add((char) i + "");
        }

        entities.add(new BannerEntity(R.drawable.bg_first, "一一一一一一一"));
        entities.add(new BannerEntity(R.drawable.bg_second, "二二二二二二二二二"));
        entities.add(new BannerEntity(R.drawable.bg_third, "三三三三三三三三三"));
        entities.add(new BannerEntity(R.drawable.bg_fourth, "四四四四四四四四四四"));
        entities.add(new BannerEntity(R.drawable.bg_fifth, "五五五五五五五五五五"));
        entities.add(new BannerEntity(R.drawable.bg_monkey_king, "六六六六六六六六六"));
        entities.add(new BannerEntity(R.drawable.nav_header_bg, "七七七七七七七七七七七七"));

        recycler_view = (WrapRecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new CommonItemDecoration(this, R.drawable.decoration));
        recycler_view.setAdapter(new MyAdapter(this, R.layout.basic_item, datas));

        footer = inflater.inflate(R.layout.header_layout, recycler_view, false);

        BannerView banner = (BannerView) inflater.inflate(R.layout.layout_banner_view, recycler_view, false);

        banner.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                if (convertView == null) {
                    convertView = new ImageView(HeaderFooterActivity.this);
                }
                ((ImageView) convertView).setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(HeaderFooterActivity.this).load(entities.get(position).getImg()).into((ImageView) convertView);
                return convertView;
            }

            @Override
            public int getCount() {
                return entities.size();
            }

            @Override
            public String getBannerDesc(int position) {
                return entities.get(position).getDesc();
            }
        }, new WeakReference<Activity>(this));

        recycler_view.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(HeaderFooterActivity.this, "点击脚部删除脚部"+ datas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        recycler_view.addHeaderView(banner);
        recycler_view.addFooterView(footer);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recycler_view.removeFooterView(footer);
            }
        });
    }

    private class MyAdapter extends CommonRecyclerAdapter<String> {

        public MyAdapter(Context context, int layoutId, List<String> data) {
            super(context, layoutId, data);
        }

        @Override
        protected void covert(CommonViewHolder holder, String s, int position) {
            holder.setText(R.id.tv_text, s);
        }
    }
}
