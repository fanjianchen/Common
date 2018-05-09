package com.sdk.appcommon.common.http;

/**
 * 回调接口
 *
 * @author Fanjianchen
 * @date 2018/3/30
 */
public interface ICallBack {
    void onDataCallBack(Object result);

    void onError(Object error);
}
