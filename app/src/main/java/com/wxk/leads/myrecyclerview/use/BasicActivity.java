package com.wxk.leads.myrecyclerview.use;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wxk.leads.myrecyclerview.R;
import com.wxk.leads.myrecyclerview.decotation.CommonItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/19
 */
public class BasicActivity extends AppCompatActivity{

    private List<String> datas;
    private RecyclerView recycler_view;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);

        inflater = LayoutInflater.from(this);

        datas = new ArrayList<>();
        for (int i = 'A'; i <= 'z'; i++) {
            datas.add((char) i + "");
        }

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new GridLayoutManager(this, 3));
        recycler_view.addItemDecoration(new CommonItemDecoration(this, R.drawable.decoration));
        recycler_view.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.basic_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.tv.setText(datas.get(position));
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            private TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);

                tv = (TextView) itemView.findViewById(R.id.tv_text);
            }
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
