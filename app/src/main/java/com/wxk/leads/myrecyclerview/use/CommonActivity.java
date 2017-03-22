package com.wxk.leads.myrecyclerview.use;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.wxk.leads.myrecyclerview.R;
import com.wxk.leads.myrecyclerview.common.CommonRecyclerAdapter;
import com.wxk.leads.myrecyclerview.common.CommonViewHolder;
import com.wxk.leads.myrecyclerview.decotation.CommonItemDecoration;
import com.wxk.leads.myrecyclerview.listener.OnItemClickListener;
import com.wxk.leads.myrecyclerview.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/19
 */

public class CommonActivity extends AppCompatActivity{

    private List<String> datas;
    private RecyclerView recycler_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        datas = new ArrayList<>();
        for (int i = 'A'; i <= 'z'; i++) {
            datas.add((char) i + "");
        }

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new CommonItemDecoration(this, R.drawable.decoration));
        MyAdapter myAdapter = new MyAdapter(this, R.layout.basic_item, datas);
        recycler_view.setAdapter(myAdapter);
        myAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Toast.makeText(CommonActivity.this, datas.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        myAdapter.setOnLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onLongClick(int position) {
                Toast.makeText(CommonActivity.this, datas.get(position)+position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private class MyAdapter extends CommonRecyclerAdapter<String>{

        public MyAdapter(Context context, int layoutId, List<String> data) {
            super(context, layoutId, data);
        }

        @Override
        protected void covert(CommonViewHolder holder, String s, int position) {

            holder.setText(R.id.tv_text, s);
            holder.setOnClickListener(R.id.tv_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CommonActivity.this, "点到我了哦", Toast.LENGTH_SHORT).show();
                }
            });
//                    .setImagePath(R.id.image, new CommonImageLoader(imgPath))
        }
    }
}
