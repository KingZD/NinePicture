package com.explame.AutoPicture.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


/**
 * Created by zd on 2016/11/8.
 */

public abstract class BaseRecycleAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{



    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent, viewType, 0);
    }

    @Override
    public void onBindViewHolder(T holder, final int position) {
        onBindViewHolder(holder, position, 0);

    }

    /**
     * @param parent
     * @param viewType
     * @param notuse   无任何作用 只为了区分函数而已 可以填任意数
     * @return
     */
    public abstract T onCreateViewHolder(ViewGroup parent, int viewType, int notuse);

    /**
     * @param holder
     * @param position
     * @param notuse   无任何作用 只为了区分函数而已 可以填任意数
     */
    public abstract void onBindViewHolder(T holder, int position, int notuse);
}
