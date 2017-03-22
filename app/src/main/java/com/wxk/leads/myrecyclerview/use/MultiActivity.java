package com.wxk.leads.myrecyclerview.use;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.wxk.leads.myrecyclerview.R;
import com.wxk.leads.myrecyclerview.common.CommonRecyclerAdapter;
import com.wxk.leads.myrecyclerview.common.CommonViewHolder;
import com.wxk.leads.myrecyclerview.common.MultiTypeSupport;
import com.wxk.leads.myrecyclerview.decotation.CommonItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/19
 */

public class MultiActivity extends AppCompatActivity{

    private List<ChatData> datas;
    private RecyclerView recycler_view;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);

        inflater = LayoutInflater.from(this);
        datas = new ArrayList<>();

        for (int i = 0; i < 100; i++) {

            if(i == 0){
                datas.add(new ChatData(-1, "开始"));
                continue;
            }

            if(i % 3 == 0){
                datas.add(new ChatData(1, "朋友的内容"));
            }else {
                datas.add(new ChatData(0, "我的内容"));
            }
        }

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.addItemDecoration(new CommonItemDecoration(this, R.drawable.decoration));
        recycler_view.setAdapter(new MultiAdapter(this, datas, new MultiTypeSupport<ChatData>() {
            @Override
            public int getLayoutId(ChatData data, int position) {
                if(position == 0){

                    return R.layout.basic_item;
                }
                if(data.isMe == 0){

                    return R.layout.me_item;
                }else {

                    return R.layout.other_item;
                }
            }
        }));
    }

    private class MultiAdapter extends CommonRecyclerAdapter<ChatData>{

        public MultiAdapter(Context context, List<ChatData> data, MultiTypeSupport<ChatData> multiTypeSupport) {
            super(context, data, multiTypeSupport);
        }

        @Override
        protected void covert(CommonViewHolder holder, ChatData chatData, int position) {

            if(position == 0){
                holder.setText(R.id.tv_text, chatData.getMsg());
            }
            if(chatData.getIsMe() == 0){

                holder.setText(R.id.msg_tv, chatData.getMsg());
            }
            if(chatData.getIsMe() == 1){

                holder.setText(R.id.other_tv, chatData.getMsg());
            }
        }
    }

    private class ChatData{

        private String msg;
        private int isMe;

        public ChatData(int isMe, String msg) {
            this.isMe = isMe;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getIsMe() {
            return isMe;
        }

        public void setIsMe(int isMe) {
            this.isMe = isMe;
        }

        @Override
        public String toString() {
            return "ChatData{" +
                    "msg='" + msg + '\'' +
                    ", isMe=" + isMe +
                    '}';
        }
    }
}
