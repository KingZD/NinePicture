package com.explame.AutoPicture;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.explame.AutoPicture.adapter.SelectImgsAdapter;
import com.explame.AutoPicture.adapter.holder.SelectImgHolder;
import com.explame.AutoPicture.gui.CPDialog;
import com.explame.AutoPicture.help.DefaultItemTouchHelpCallback;
import com.explame.AutoPicture.help.DefaultItemTouchHelpCallback.OnItemTouchCallbackListener;
import com.explame.AutoPicture.help.DefaultItemTouchHelper;
import com.explame.AutoPicture.util.SystemUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import rx.functions.Action1;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SelectImgsAdapter.Callback{

    private static final int REQUEST_IMAGE = 1001;

    /**
     * 默认最多选择6张
     */
    int maxImg = 6;

    @ViewById
    TextView tvUrl;

    @ViewById
    RecyclerView recycleView;

    @StringRes(R.string.cp_open_review_add)
    String add;

    List<String> mImgs;

    SelectImgsAdapter adapter;

    DefaultItemTouchHelper helper;

    @AfterViews
    void initView(){
        mImgs = new ArrayList<>();
        mImgs.add(add);
        adapter = new SelectImgsAdapter(mImgs,this);
        adapter.setCallback(this);
        helper = new DefaultItemTouchHelper(new DefaultItemTouchHelpCallback(onItemTouchCallbackListener));
        helper.attachToRecyclerView(recycleView);
        recycleView.setLayoutManager(new GridLayoutManager(this,3));
        recycleView.addItemDecoration(new SpaceItemDecoration(3,11,false));
        recycleView.setAdapter(adapter);
    }

    private OnItemTouchCallbackListener onItemTouchCallbackListener = new OnItemTouchCallbackListener() {
        @Override
        public void onSwiped(int adapterPosition) {
//             滑动删除的时候，从数据源移除，并刷新这个Item。
            if (mImgs != null) {
                mImgs.remove(adapterPosition);
                adapter.notifyItemRemoved(adapterPosition);
            }
        }

        @Override
        public boolean onMove(int srcPosition, int targetPosition) {
            //除了+按钮不允许移动
            if (mImgs != null) {
                if(!mImgs.get(targetPosition).equals(add)) {
                    // 更换数据源中的数据Item的位置
                    Collections.swap(mImgs, srcPosition, targetPosition);
                    // 更新UI中的Item的位置，主要是给用户看到交互效果
                    adapter.notifyItemMoved(srcPosition, targetPosition);
                }
                return true;
            }
            //最后一个不允许移动
//            if (mImgs != null && targetPosition != mImgs.size() -1) {
//                // 更换数据源中的数据Item的位置
//                Collections.swap(mImgs, srcPosition, targetPosition);
//                // 更新UI中的Item的位置，主要是给用户看到交互效果
//                adapter.notifyItemMoved(srcPosition, targetPosition);
//                return true;
//            }
            return false;
        }

        @Override
        public void complete() {
            //如果完成之后不刷新会导致删除的时候下标错误的情况
            adapter.notifyDataSetChanged();
            showUrl();
        }
    };

    @Override
    public void startDrag(SelectImgHolder holder) {
        helper.startDrag(holder);
    }

    @Override
    public void delPicture(final String url, final int position) {
        CPDialog dialog = new CPDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("确认要删除?");
        dialog.setPositiveButton("取消", null);
        dialog.setNegativeButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除照片
                mImgs.remove(position);
                adapter.notifyItemRemoved(position);
                isNeedShowAdd();
            }
        }).show();
    }

    public void addImgs(String img){
        /**添加的图片需要在+之前**/
        int index = mImgs.size()-1;
        mImgs.add(index,img);
        adapter.notifyDataSetChanged();
        isNeedShowAdd();
    }

    /**是否显示添加图片的按钮**/
    void isNeedShowAdd(){
        /**满足6张图片则隐藏+**/
        if(mImgs.size() > maxImg)
            mImgs.remove(mImgs.size() -1);
        else if(!mImgs.contains(add))
            mImgs.add(add);
        showUrl();
    }

    public List<String> getImgs(){
        /**移除添加文字**/
        if(mImgs != null)
            mImgs.remove(add);
        return mImgs;
    }

    @Override
    public void addPicture() {
        RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity instance
        rxPermissions.request(Manifest.permission.CAMERA)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        /**选择拍照**/
                        Intent intent = new Intent(MainActivity.this, MultiImageSelectorActivity.class);
                        // whether show camera
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, granted);
                        // max select image amount
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
                        // select mode (MultiImageSelectorActivity.MODE_SINGLE OR MultiImageSelectorActivity.MODE_MULTI)
                        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
                        startActivityForResult(intent, REQUEST_IMAGE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                addImgs(path.get(0));
            }
        }
    }

    /**GridView间距**/
    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public SpaceItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = SystemUtil.dpToPx(MainActivity.this,spacing);
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }

    }

    /**
     * 打印路劲
     */
    void showUrl(){
        int count = mImgs.size();
        tvUrl.setText("");
        for (int i=0;i<count;i++){
            if(mImgs.get(i).equals(add)) continue;
            tvUrl.append(i+"->"+mImgs.get(i)+"\n");
        }
    }
}
