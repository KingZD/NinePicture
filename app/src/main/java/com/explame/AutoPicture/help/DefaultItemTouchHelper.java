package com.explame.AutoPicture.help;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by zd on 2017/2/21.
 */

public class DefaultItemTouchHelper extends ItemTouchHelper {

    private DefaultItemTouchHelpCallback itemTouchHelpCallback;


    public DefaultItemTouchHelper(DefaultItemTouchHelpCallback callback) {
        super(callback);
        itemTouchHelpCallback = callback;
    }

    /**
     * 设置是否可以被拖拽
     *
     * @param canDrag 是true，否false
     */
    public void setDragEnable(boolean canDrag) {
        itemTouchHelpCallback.setDragEnable(canDrag);
    }

    /**
     * 设置是否可以被滑动
     *
     * @param canSwipe 是true，否false
     */
    public void setSwipeEnable(boolean canSwipe) {
        itemTouchHelpCallback.setSwipeEnable(canSwipe);
    }
}
