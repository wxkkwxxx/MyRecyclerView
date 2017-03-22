package com.wxk.leads.myrecyclerview.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wxk.leads.myrecyclerview.listener.OnItemClickListener;
import com.wxk.leads.myrecyclerview.listener.OnItemLongClickListener;

import java.util.List;

/**
 * Created by Administrator on 2017/3/19
 */

public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter<CommonViewHolder>{

    private List<T> data;
    private int layoutId;
    private Context context;
    private LayoutInflater inflater;
    private MultiTypeSupport<T> multiTypeSupport;

    public CommonRecyclerAdapter(Context context, int layoutId, List<T> data) {
        this.data = data;
        this.layoutId = layoutId;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public CommonRecyclerAdapter(Context context, List<T> data, MultiTypeSupport<T> multiTypeSupport){
        this(context, -1, data);
        this.multiTypeSupport = multiTypeSupport;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(multiTypeSupport != null){
            layoutId = viewType;
        }

        View itemView = inflater.inflate(layoutId, parent, false);
        return new CommonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommonViewHolder holder, int position) {

        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(holder.getAdapterPosition());
                }
            });
        }

        covert(holder, data.get(position), position);
    }

    protected abstract void covert(CommonViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(multiTypeSupport != null){

            return multiTypeSupport.getLayoutId(data.get(position), position);
        }
        return super.getItemViewType(position);
    }

    public OnItemClickListener mItemClickListener;
    public OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }
}
