package com.wxk.leads.myrecyclerview.common;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.wxk.leads.myrecyclerview.listener.OnItemClickListener;
import com.wxk.leads.myrecyclerview.listener.OnItemLongClickListener;

/**
 * Created by Administrator on 2017/3/20
 */
//根据listView添加header.footer源码采用装饰着模式
public class WrapRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private SparseArray<View> mHeaderViews;
    private SparseArray<View> mFooterViews;
    private RecyclerView.Adapter mAdapter;

    // 基本的头部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_HEADER = 10000000;
    // 基本的底部类型开始位置  用于viewType
    private static int BASE_ITEM_TYPE_FOOTER = 20000000;

    public WrapRecyclerAdapter(RecyclerView.Adapter adapter){
        this.mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(isHeaderViewType(viewType)){
            View view = mHeaderViews.get(viewType);
            return createHeaderFooterViewHolder(view);
        }

        if(isFooterViewType(viewType)){
            View view = mFooterViews.get(viewType);
            return createHeaderFooterViewHolder(view);
        }

        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //listview源码
//        Header (negative positions will throw an IndexOutOfBoundsException)
//        int numHeaders = getHeadersCount();
//        if (position < numHeaders) {
//            return mHeaderViewInfos.get(position).data;
//        }
//
//        // Adapter
//        final int adjPosition = position - numHeaders;
//        int adapterCount = 0;
//        if (mAdapter != null) {
//            adapterCount = mAdapter.getCount();
//            if (adjPosition < adapterCount) {
//                return mAdapter.getItem(adjPosition);
//            }
//        }
//
//        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
//        return mFooterViewInfos.get(adjPosition - adapterCount).data;

        if(isHeader(position) || isFooter(position)){

            return;
        }

        final int adjPosition = position - getHeaderCount();
        if(adjPosition < mAdapter.getItemCount()){
            mAdapter.onBindViewHolder(holder, adjPosition);
        }

        // 设置点击和长按事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(adjPosition);
                }
            });
        }
        if (mLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mLongClickListener.onLongClick(adjPosition);
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        //listView 源码
//    public int getCount() {
//        if (mAdapter != null) {
//            return getFootersCount() + getHeadersCount() + mAdapter.getCount();
//        } else {
//            return getFootersCount() + getHeadersCount();
//        }
//    }

        return getHeaderCount() + getFooterCount() + mAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
//    public boolean isEnabled(int position) {
//        // Header (negative positions will throw an IndexOutOfBoundsException)
//        int numHeaders = getHeadersCount();
//        if (position < numHeaders) {
//            return mHeaderViewInfos.get(position).isSelectable;
//        }
//
//        // Adapter
//        final int adjPosition = position - numHeaders;
//        int adapterCount = 0;
//        if (mAdapter != null) {
//            adapterCount = mAdapter.getCount();
//            if (adjPosition < adapterCount) {
//                return mAdapter.isEnabled(adjPosition);
//            }
//        }
//
//        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
//        return mFooterViewInfos.get(adjPosition - adapterCount).isSelectable;
//    }

        if(isHeader(position)){
            //直接返回position位置的key
            return mHeaderViews.keyAt(position);
        }
        if(isFooter(position)){

            return mFooterViews.keyAt(position - getHeaderCount() - mAdapter.getItemCount());
        }

        return mAdapter.getItemViewType(position - getHeaderCount());
    }

    //是不是头部类型
    private boolean isHeaderViewType(int viewType) {
        int position = mHeaderViews.indexOfKey(viewType);
        return position >= 0;
    }

    //是不是底部类型
    private boolean isFooterViewType(int viewType) {
        int position = mFooterViews.indexOfKey(viewType);
        return position >= 0;
    }

    //创建头部或者底部的ViewHolder
    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {

        };
    }

    //头部数量
    public int getHeaderCount(){
        return mHeaderViews.size();
    }

    //脚部数量
    public int getFooterCount(){
        return mFooterViews.size();
    }

    //是否是头部
    public boolean isHeader(int position){

        return position < getHeaderCount();
    }

    //是否是脚部
    public boolean isFooter(int position){

        return position >= getHeaderCount() + mAdapter.getItemCount();
    }

    //添加头部
    public void addHeaderView(View view){
        //判断集合中是否添加过
        int position = mHeaderViews.indexOfValue(view);
        if(position < 0){

            mHeaderViews.put(BASE_ITEM_TYPE_HEADER++, view);
        }
        notifyDataSetChanged();
    }

    //添加脚部
    public void addFooterView(View view){

        int position = mFooterViews.indexOfValue(view);
        if(position < 0){

            mFooterViews.put(BASE_ITEM_TYPE_FOOTER++, view);
        }
        notifyDataSetChanged();
    }

    // 移除头部
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index < 0) return;
        mHeaderViews.removeAt(index);
        notifyDataSetChanged();
    }

    // 移除底部
    public void removeFooterView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index < 0) return;
        mFooterViews.removeAt(index);
        notifyDataSetChanged();
    }

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
    }

    //解决GridLayoutManager添加头部和底部不占用一行的问题
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeader(position) || isFooter(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }
}
