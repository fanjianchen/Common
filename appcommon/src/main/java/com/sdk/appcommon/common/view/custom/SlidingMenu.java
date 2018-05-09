package com.sdk.appcommon.common.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * 主界面侧滑的自定义View
 * @author Fanjianchen
 * @date 2018/4/28
 */
public class SlidingMenu extends HorizontalScrollView {
    public SlidingMenu(Context context) {
        super(context);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.);
    }
}
