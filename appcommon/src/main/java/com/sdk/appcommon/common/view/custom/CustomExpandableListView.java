package com.sdk.appcommon.common.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 自定义ExpandableListView控件
 * @Description 重新计算ExpandableListView的高解决ScrollView和ExpandableListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
 * @author fanjianchen
 * @data 2018/4/3
 */
public class CustomExpandableListView extends ExpandableListView  {
	public CustomExpandableListView(Context context) {
		super(context);
	}

	public CustomExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//重新计算高度
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		//将重新计算的高度传递回去
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
