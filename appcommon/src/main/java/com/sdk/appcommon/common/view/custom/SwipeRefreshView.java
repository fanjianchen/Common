package com.sdk.appcommon.common.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdk.appcommon.R;


/**
 * 自定义View继承SwipeRefreshLayout，添加下拉刷新和上拉加载更多的布局属性
 * @author fanjianchen
 * @date 2018/4/3
 */
public class SwipeRefreshView extends SwipeRefreshLayout {

    private final int mScaledTouchSlop;
    private final View mFooterView;
    private ListView mListView;
    /**上拉加载接口*/
    private OnLoadListener mOnLoadListener;
    private ProgressBar progressBar;
    private TextView textView;
    private View titleView,title,back,device = null;
    private int headerHeight = 0;

    /**
     * 正在加载状态
     */
    private boolean isLoading;

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 填充底部加载布局
        mFooterView = View.inflate(context, R.layout.view_footer, null);
        progressBar= (ProgressBar) mFooterView.findViewById(R.id.load_progress);        
        textView= (TextView) mFooterView.findViewById(R.id.load_tv);        
        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        // 获取ListView,设置ListView的布局位置
        if (mListView == null) {
            // 判断容器有多少个孩子
            if (getChildCount() > 0) {
                // 判断第一个孩子是不是ListView
                if (getChildAt(0) instanceof ListView) {
                    // 创建ListView对象
                    mListView = (ListView) getChildAt(0);
                    // 设置ListView的滑动监听
                    setListViewOnScroll();
                    // 加入底部布局并隐藏
                    mListView.addFooterView(mFooterView);
                    mFooterView.setVisibility(View.GONE);
                }
            }
        }
    }

    /**
     * 在分发事件的时候处理子控件的触摸事件
     *
     * @param ev
     * @return
     */
    private float mDownY, mUpY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                System.out.println("getY:ACTION_DOWN:"+mDownY);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                // 移动的终点
                mUpY = ev.getY();
             // 触摸事件完成后判断是否加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
                System.out.println("getY:ACTION_UP:"+mDownY);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断是否满足加载更多条件
     *
     * @return
     */
    private boolean canLoadMore() {
        // 1. 是上拉状态
        boolean condition1 = (mDownY - mUpY) >= mScaledTouchSlop;
        if (condition1) {
            System.out.println("是上拉状态");
        }

        // 2. 当前页面可见的item是最后一个条目
        boolean condition2 = false;
        if (mListView != null && mListView.getAdapter() != null) {
            condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
        }

        if (condition2) {
            System.out.println("是最后一个条目");
        }
        // 3. 正在加载状态
        boolean condition3 = !isLoading;
        if (condition3) {
            System.out.println("不是正在加载状态");
        }
        System.out.println("downY-upY："+mDownY+"-"+mUpY+"="+(mDownY-mUpY));
        return condition1 && condition2 && condition3;
    }
    /**
     * 设置底部布局展示
     * @param isShowLoad 是否显示加载布局
     */
    public void setFooterViewData(boolean isShowLoad){
    	if (isShowLoad){
    		progressBar.setVisibility(View.VISIBLE);
        	textView.setText("正在努力加载中...");
		}else{
			progressBar.setVisibility(View.GONE);
	    	textView.setText("亲，我也是有底线的");
		}
    }

    /**
     * 处理加载数据的逻辑
     */
    private void loadData() {
        System.out.println("加载数据...");
        if (mOnLoadListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true);
            mOnLoadListener.onLoad();
        }

    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        // 修改当前的状态
        isLoading = loading;
        if (isLoading) {
        	//显示布局
            mFooterView.setVisibility(View.VISIBLE);
        } else {
        	//隐藏布局
        	mFooterView.setVisibility(View.GONE);
            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;
        }
    }


    @Override
	public void setRefreshing(boolean arg0) {
		// TODO Auto-generated method stub
		super.setRefreshing(arg0);
		//隐藏布局
		mFooterView.setVisibility(View.GONE);
	}

	/**
     * 设置ListView的滑动监听
     */
    private void setListViewOnScroll() {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            	if (titleView!=null&&title!=null&&back!=null&&device!=null) {
            		// 判断当前最上面显示的是不是头布局，因为Xlistview有刷新控件，所以头布局的位置是1，即第二个
                    if (firstVisibleItem == 0) {
                        // 获取头布局
                        View headerView = mListView.getChildAt(0);
                        if (view != null) {
                            // 获取头布局现在的最上部的位置的相反数
                            int top = -headerView.getTop();
                            // 获取头布局的高度
                            headerHeight = headerView.getHeight();
                            // 满足这个条件的时候，是头布局在XListview的最上面第一个控件的时候，只有这个时候，我们才调整透明度
                            if (top <= headerHeight && top >= 0) {
                                // 获取当前位置占头布局高度的百分比
                                float f = (float) top / (float) headerHeight;
                                int alpha = (int)(f * 255);
                                //view.getBackground().mutate()让这个drawable可变，
                                //这个操作是不可逆的。一个可变Drawable可以保证不与其它的Drawable分享一个状态。
                                //当你需要修改资源中的Drawable的属性时这个方法是非常有用的，
                                //因为默认情况下加载相同资源的所有Drawable实例拥有同一个状态，如果你在一个地方改变了状态，其它的实例也会跟着改变。
                                titleView.getBackground().mutate().setAlpha(alpha);
                                ((TextView)title).setTextColor(Color.argb(alpha,0,0,0));
                                ((TextView)back).setTextColor(Color.argb(255,255,255,255));
                                device.getBackground().mutate().setAlpha((int) (f * 255));
                                // 通知标题栏刷新显示
                                titleView.invalidate();
                            }
                        }
                    } else {
                    	titleView.getBackground().mutate().setAlpha(255);
                    	((TextView)title).setTextColor(Color.argb(255,0,0,0));
                    	((TextView)back).setTextColor(Color.argb(255,0,0,0));
                        device.getBackground().mutate().setAlpha(255);
                    }
            	}
            }
        });
    }

    /**
     * 设置title布局,用于设置title布局渐变透明
     * @param titleView 标题布局
     * @param title 标题文字
     * @param back 返回键
     * @param device 分割线
     */
    public void setView(View titleView,View title,View back,View device){
    	this.titleView = titleView;
    	this.title = title;
    	this.device = device;
    	this.back = back;
    }

    /**
     * 上拉加载的接口回调
     */

    public interface OnLoadListener {
        void onLoad();
    }
    /**
     * 设置上拉加载更多的逻辑处理
     * @param listener
     */
    public void setOnLoadListener(OnLoadListener listener) {
        this.mOnLoadListener = listener;
    }
}
