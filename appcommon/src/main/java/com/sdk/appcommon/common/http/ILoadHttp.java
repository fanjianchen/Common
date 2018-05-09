package com.sdk.appcommon.common.http;

import android.app.Activity;

import java.io.File;
import java.util.HashMap;

/**
 * @author Fanjianchen
 * @date 2018/3/30
 */
public interface ILoadHttp {
    //get请求
    void onGet(Activity activity, boolean isShowDialog, String loadMessage, String url, Class cls, ICallBack callBack);

    //post请求，无参数
    void onPost(Activity activity, boolean isShowDialog, String loadMessage, String url, Class cls, ICallBack callBack);
    //post请求，参数String（JSON字符串）格式
    void onPost(Activity activity, boolean isShowDialog, String loadMessage, String url, String string, Class cls, ICallBack callBack);
    //post请求，参数Map格式
    void onPost(Activity activity, boolean isShowDialog, String loadMessage, String url, HashMap<String, String> map, Class cls, ICallBack callBack);
    //post请求，参数File格式
    void onPost(Activity activity, boolean isShowDialog, String loadMessage, String url, File file, Class cls, ICallBack callBack);
}
