package com.sdk.appcommon.common.http;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.sdk.appcommon.common.application.MyApplication;
import com.sdk.appcommon.common.utils.NetUtils;
import com.sdk.appcommon.common.utils.Tools;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Fanjianchen
 * @date 2018/3/30
 */
public class LoadHttp implements ILoadHttp {
    @Override
    public void onGet(Activity activity, boolean isShowDialog, String loadMessage, String url, Class cls, ICallBack callBack) {
        getAsynHttp(activity, isShowDialog, loadMessage, url, cls,callBack);
    }
    @Override
    public void onPost(Activity activity, boolean isShowDialog, String loadMessage, String url, Class cls, ICallBack callBack) {
        postAsynHttp(activity, isShowDialog, loadMessage, url, null, null, null,cls, callBack);
    }
    @Override
    public void onPost(Activity activity, boolean isShowDialog, String loadMessage, String url, String string, Class cls, ICallBack callBack) {
        postAsynHttp(activity, isShowDialog, loadMessage, url, string, null, null,cls, callBack);
    }
    @Override
    public void onPost(Activity activity, boolean isShowDialog, String loadMessage, String url, HashMap<String,String> map, Class cls, ICallBack callBack) {
        postAsynHttp(activity, isShowDialog, loadMessage, url, null, map, null,cls, callBack);
    }
    @Override
    public void onPost(Activity activity, boolean isShowDialog, String loadMessage, String url, File file, Class cls, ICallBack callBack) {
        postAsynHttp(activity, isShowDialog, loadMessage, url, null, null, file,cls, callBack);
    }

    /**
     * get请求
     *
     * @param activity     活动页
     * @param url          接口路径
     * @param isShowDialog 是否显示加载条
     * @param loadMessage  展示的加载信息
     * @param callBack     回调函数
     */
    private void getAsynHttp(Activity activity, boolean isShowDialog, String loadMessage, String url, final Class cls, final ICallBack callBack) {
        //请求网络之前，检查连接是否畅通
        if (NetUtils.isConnnected(MyApplication.getInstances())) {
            final LoadProgressBar loadProgressBar;
            if (isShowDialog) {
                loadProgressBar = new LoadProgressBar(activity);
                loadProgressBar.showMessage(loadMessage);
            } else {
                loadProgressBar = null;
            }
            Request.Builder requestBuilder = new Request.Builder()
                    .url(url);
            Request request = requestBuilder.build();
            Call mCall = MyApplication.mOkHttpClient.newCall(request);
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (loadProgressBar != null) {
                        loadProgressBar.cancel();
                    }
                    callBack.onError("数据加载失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (loadProgressBar != null) {
                        loadProgressBar.cancel();
                    }
                    if (response.cacheResponse() != null) {
                        String string = response.body().string();
                        Object object = parseNetworkResponse(string,cls);
                        callBack.onDataCallBack(object);
                    } else {
                        String string = response.body().string();
                        Object object = parseNetworkResponse(string,cls);
                        callBack.onDataCallBack(object);
                    }
                }
            });
        } else {
            Tools.showToast("请检查网络连接", true);
        }

    }

    /**
     * post请求
     *
     * @param url        接口路径
     * @param mJsonStr   post提交形式：JSON字符串形式
     * @param mParamsMap post提交形式：键值对形式
     * @param file       post提交形式：File对象
     * @param callBack   回调函数
     */
    private void postAsynHttp(Activity activity, boolean isShowDialog, String loadMessage, String url, String mJsonStr, Map<String, String> mParamsMap, File file, final Class cls, final ICallBack callBack) {
        //请求网络之前，检查连接是否畅通
        if (NetUtils.isConnnected(MyApplication.getInstances())) {
            final LoadProgressBar loadProgressBar;
            if (isShowDialog) {
                loadProgressBar = new LoadProgressBar(activity);
                loadProgressBar.showMessage(loadMessage);
            } else {
                loadProgressBar = null;
            }
            Request.Builder requestBuilder = new Request.Builder()
                    .post(getRequestBody(mJsonStr, mParamsMap, file))
                    .url(url);
            Request request = requestBuilder.build();
            Call mCall = MyApplication.mOkHttpClient.newCall(request);
            mCall.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (loadProgressBar != null) {
                        loadProgressBar.cancel();
                    }
                    callBack.onError("数据加载失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (loadProgressBar != null) {
                        loadProgressBar.cancel();
                    }
                    if (response.cacheResponse() != null) {
                        String string = response.body().string();
                        Object object = parseNetworkResponse(string,cls);
                        callBack.onDataCallBack(object);
                    } else {
                        String string = response.body().string();
                        Object object = parseNetworkResponse(string,cls);
                        callBack.onDataCallBack(object);
                    }
                }
            });
        } else {
            Tools.showToast("请检查网络连接", true);
        }
    }

    /**
     * 得到body对象
     *
     * @param mJsonStr   使用RequestBody传递Json(可为空)
     * @param mParamsMap 使用RequestBody传递mParamsMap(可为空)
     * @param file       使用RequestBody传递File对象(可为空)
     * @return
     */
    private RequestBody getRequestBody(String mJsonStr, Map<String, String> mParamsMap, File file) {
        /**
         * 首先判断mJsonStr是否为空，由于mJsonStr与mParamsMap不可能同时存在，所以先判断mJsonStr
         */
        if (!TextUtils.isEmpty(mJsonStr)) {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
            return RequestBody.create(JSON, mJsonStr);//json数据
        }
        /**
         * 上传File对象
         */
        if (file != null && file.exists()) {
            MediaType fileType = MediaType.parse("File/*");//数据类型为json格式
            return RequestBody.create(fileType, file);
        }
        /**
         * post,put,delete都需要body，但也都有body等于空的情况，此时也应该有body对象，但body中的内容为空
         */
        FormBody.Builder formBody = new FormBody.Builder();
        if (mParamsMap != null) {
            for (String key : mParamsMap.keySet()) {
                formBody.add(key, mParamsMap.get(key));
            }
        }
        return formBody.build();
    }

    public Object parseNetworkResponse(String response, Class cls){
        Object object = null;
        try {
            object = JSON.parseObject(response,cls);
        }catch (Exception e){
            e.printStackTrace();
            Tools.showToast("数据解析出错",true);
        }
        return object;
    }

    /**
     * 组装头部
     *
     * @param params
     * @return
     */
    public static HashMap<String, String> getHttpHeaders(Map<String, String> params) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("usersid", "");//用户id
        headers.put("token", "");//用户令牌
        headers.put("apptype", "0");// 应用类型：0-android；1-ios；2-web
        headers.put("versionCode", "");//应用版本号
        headers.put("randomCode", System.currentTimeMillis() + "");// 时间截
        headers.put("imei", "");// 国际移动设备身份码
        headers.put("brand", android.os.Build.MODEL);// 获取手机品牌
        headers.put("firm", android.os.Build.MANUFACTURER);//获取手机厂家
        headers.put("ip", "");// 获取本机ip地址
        String sign = _MakeSign(params, headers);
        headers.put("sign", sign);// 签名（参加签名规则）
        return headers;
    }

    /**
     * 生成签名
     *
     * @param params
     * @param headers
     * @return
     */
    private static String _MakeSign(Map<String, String> params,
                                    HashMap<String, String> headers) {
        // 排序集合,TreeSet中的元素必须要实现Comparable接口
        TreeSet<String> treeSet = new TreeSet(new SignCompare());
        treeSet.addAll(params.keySet());
        treeSet.addAll(headers.keySet());
        headers.putAll(params);
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<String> iterator = treeSet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = headers.get(key);
            if (!TextUtils.isEmpty(value) && value.getBytes().length > 1024) {
                value = new String(value.getBytes(), 0, 1024);
            }
            if (!TextUtils.isEmpty(value)) {
                stringBuffer.append(key + "=" + value + "&");
            }
        }
        StringBuffer sb = stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return Tools.encryption(sb.toString(), "MD5");
    }

    private static class SignCompare implements Comparator<Object> {

        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            return s1.compareTo(s2);
        }
    }

}
