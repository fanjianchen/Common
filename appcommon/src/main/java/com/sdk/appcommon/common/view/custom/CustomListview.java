package com.sdk.appcommon.common.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * 自定义ListView控件 重新计算ListView的高解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
 * @author fanjianchen
 * @date 2018/4/3
 */
public class CustomListview extends ListView {
	public CustomListview(Context context) {
		super(context);
	}

	public CustomListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	// TODO Auto-generated method stub
		return super.onTouchEvent(ev);
//		return false;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
