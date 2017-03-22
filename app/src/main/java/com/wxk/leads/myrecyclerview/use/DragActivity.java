package com.wxk.leads.myrecyclerview.use;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wxk.leads.myrecyclerview.R;
import com.wxk.leads.myrecyclerview.common.CommonRecyclerAdapter;
import com.wxk.leads.myrecyclerview.common.CommonViewHolder;
import com.wxk.leads.myrecyclerview.decotation.CommonItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21
 */

public class DragActivity extends AppCompatActivity{

    private List<String> datas;
    private RecyclerView recycler_view;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        inflater = LayoutInflater.from(this);

        datas = new ArrayList<>();
        for (int i = 'A'; i <= 'Z'; i++) {
            datas.add((char) i + "");
        }

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new CommonItemDecoration(this, R.drawable.decoration));
        final MyAdapter adapter = new MyAdapter(this, R.layout.basic_item, datas);
        recycler_view.setAdapter(adapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                //触摸时的响应方向

                int swipeFlags = ItemTouchHelper.LEFT;
                int drawFlags;

                if(recyclerView.getLayoutManager() instanceof GridLayoutManager){

                    drawFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
                            | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }else {

                    drawFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                }

                return makeMovementFlags(drawFlags, swipeFlags);//拖动的flags,侧滑的flags
            }

            @Override  //拖动过程中的回调
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                //得到的原来位置
                int fromPosition = viewHolder.getAdapterPosition();
                //得到的目标位置
                int targetPosition = target.getAdapterPosition();

                adapter.notifyItemMoved(fromPosition, targetPosition);

                if(fromPosition > targetPosition){

                    for (int i = fromPosition; i > targetPosition; i--) {
                        Collections.swap(datas, i, i - 1); // 改变实际的数据集
                    }
                }else {
                    for (int i = fromPosition; i < targetPosition; i++){

                        Collections.swap(datas, i, i + 1); // 改变实际的数据集
                    }
                }

                //可保存至服务器火sp,以后进来时数据就是拖动之后的排序了
                Log.e("数据源:",datas.toString());
                return true;
            }

            //状态发生改变
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

                //拖动状态 侧滑状态 普通状态
                if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
                    viewHolder.itemView.setBackgroundColor(Color.GRAY);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                //动画执行完毕
                viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(DragActivity.this, R.color.item_default));

                //查看源码才解决的问题,侧滑删除后item复用的的问题
                ViewCompat.setTranslationX(viewHolder.itemView, 0);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //侧滑删除执行完毕后的 移除更新adapter
                int currentPos = viewHolder.getAdapterPosition();
                adapter.notifyItemRemoved(currentPos);
                datas.remove(currentPos);
            }
        });

        itemTouchHelper.attachToRecyclerView(recycler_view);
    }

    private class MyAdapter extends CommonRecyclerAdapter<String> {

        public MyAdapter(Context context, int layoutId, List<String> data) {
            super(context, layoutId, data);
        }

        @Override
        protected void covert(CommonViewHolder holder, String s, int position) {

            holder.setText(R.id.tv_text, s);
            holder.setOnClickListener(R.id.tv_text, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DragActivity.this, "点到我了哦", Toast.LENGTH_SHORT).show();
                }
            });
//                    .setImagePath(R.id.image, new CommonImageLoader(imgPath))
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_grid:

                recycler_view.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.action_linear:

                recycler_view.setLayoutManager(new LinearLayoutManager(this));
                break;
        }
        return true;
    }
}
