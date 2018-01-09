package com.explame.AutoPicture.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.explame.AutoPicture.R;
import com.explame.AutoPicture.adapter.holder.SelectImgHolder;

import java.util.List;

/**
 * Created by zd on 2017/2/21.
 */

public class SelectImgsAdapter extends BaseRecycleAdapter<SelectImgHolder> implements SelectImgHolder.LongPressListener{

    private Callback callback;
    private List<String> mImg;
    private Context mContext;

    public SelectImgsAdapter(List<String> imgs, Context context) {
        this.mImg = imgs;
        this.mContext = context;
    }

    @Override
    public SelectImgHolder onCreateViewHolder(ViewGroup parent, int viewType, int notuse) {
        return new SelectImgHolder(View.inflate(mContext, R.layout.view_review_add_img,null),mContext).setLister(this);
    }

    @Override
    public void onBindViewHolder(SelectImgHolder holder, int position, int notuse) {
        holder.bind(mImg.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mImg.size();
    }

    @Override
    public void longPress(SelectImgHolder holder) {
        if(this.callback != null)
            this.callback.startDrag(holder);
    }

    @Override
    public void delPicture(String url,int position) {
        if(this.callback != null)
            this.callback.delPicture(url,position);
    }

    @Override
    public void addPicture() {
        if(this.callback != null)
            this.callback.addPicture();
    }

    public interface Callback {
        void startDrag(SelectImgHolder holder);
        void delPicture(String url,int position);
        void addPicture();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
