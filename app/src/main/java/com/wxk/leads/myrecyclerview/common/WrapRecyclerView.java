package com.wxk.leads.myrecyclerview.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.wxk.leads.myrecyclerview.listener.OnItemClickListener;
import com.wxk.leads.myrecyclerview.listener.OnItemLongClickListener;

/**
 * Created by Administrator on 2017/3/20
 */

public class WrapRecyclerView extends RecyclerView{

    private WrapRecyclerAdapter mWrapRecyclerAdapter;
    private RecyclerView.Adapter mAdapter;

    private View mEmptyView, mLoadingView;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if(mAdapter != null){

            //防止多次设置
            mAdapter.unregisterAdapterDataObserver(mDataObserver);
            mAdapter = null;
        }

        this.mAdapter = adapter;

        if(mAdapter instanceof WrapRecyclerAdapter){

            mWrapRecyclerAdapter = (WrapRecyclerAdapter) adapter;
        }else {

            mWrapRecyclerAdapter = new WrapRecyclerAdapter(mAdapter);
        }

        super.setAdapter(mWrapRecyclerAdapter);

        // 注册一个观察者
        mAdapter.registerAdapterDataObserver(mDataObserver);

        mWrapRecyclerAdapter.adjustSpanSize(this);

        // 加载数据页面
        if (mLoadingView != null && mLoadingView.getVisibility() == View.VISIBLE) {
            mLoadingView.setVisibility(View.GONE);
        }

        if (mItemClickListener != null) {
            mWrapRecyclerAdapter.setOnItemClickListener(mItemClickListener);
        }

        if (mLongClickListener != null) {
            mWrapRecyclerAdapter.setOnLongClickListener(mLongClickListener);
        }
    }

    // 添加头部
    public void addHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addHeaderView(view);
        }
    }

    // 添加底部
    public void addFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.addFooterView(view);
        }
    }

    // 移除头部
    public void removeHeaderView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeHeaderView(view);
        }
    }

    // 移除底部
    public void removeFooterView(View view) {
        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.removeFooterView(view);
        }
    }

    //添加一个空列表数据页面
    public void addEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    //添加一个正在加载数据的页面
    public void addLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void dataChanged() {
        if (mAdapter.getItemCount() == 0) {
            // 没有数据
            if (mEmptyView != null) {
                mEmptyView.setVisibility(VISIBLE);
            }
        }else {
            if (mEmptyView != null) {
                mEmptyView.setVisibility(GONE);
            }
        }
    }

    //因为列表adapter数据更新,包裹的adapter也需要更新
    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            if(mAdapter == null) return;
            if(mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyDataSetChanged();
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemChanged(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            if (mAdapter == null) return;
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemChanged(positionStart, payload);
            dataChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemInserted(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemRemoved(positionStart);
            dataChanged();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            if (mWrapRecyclerAdapter != mAdapter)
                mWrapRecyclerAdapter.notifyItemMoved(fromPosition, toPosition);
            dataChanged();
        }
    };

    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;

        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.setOnItemClickListener(mItemClickListener);
        }
    }

    public void setOnLongClickListener(OnItemLongClickListener longClickListener) {
        this.mLongClickListener = longClickListener;

        if (mWrapRecyclerAdapter != null) {
            mWrapRecyclerAdapter.setOnLongClickListener(mLongClickListener);
        }
    }
}
