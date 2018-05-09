package com.sdk.appcommon.common.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 *  自定义GridView，为了解决与scroview的嵌套问题
 * @author fanjianchen 
 * @date 2018/4/3
 */
public class CustomGridView extends GridView{
	
	public CustomGridView(Context context ) {
		super(context );
		// TODO Auto-generated constructor stub
	}
	
	public CustomGridView(Context context, AttributeSet attrs ) {
		super(context, attrs );
		// TODO Auto-generated constructor stub
	}

	public CustomGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//为了解决与scroview的嵌套 
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
