package com.sdk.appcommon.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.sdk.appcommon.R;
import com.sdk.appcommon.common.application.MyApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;

/**
 * 工具类
 * @author Fanjianchen
 * @date 2018/4/2
 */
public class Tools {
    /** 全局统一Toast类 */
    private static Toast mToast;
    private static final String TAG = "Tools";
    /**
     * 小提示
     *
     * @param content
     *            提示内容
     * @param longTime
     *            是否长时间提醒
     * @param logLevel
     *            填d, w, e分别代表debug, warn, error; 默认是debug
     */
    public static void showToast(String content, boolean longTime, String logLevel) {
        Context context = MyApplication.getInstances();
        if (!TextUtils.isEmpty(content) && context != null) {
            int timer = Toast.LENGTH_SHORT;
            if (longTime) {
                timer = Toast.LENGTH_LONG;
            }
            if ("w".equals(logLevel)) {
                Log.w(TAG, content);
            } else if ("e".equals(logLevel)) {
                Log.e(TAG, content);
            } else {
                Log.d(TAG, content);
            }
            LayoutInflater factory = LayoutInflater.from(context);
            View view = factory.inflate(R.layout.toast_layout, null);
            TextView tv = view.findViewById(R.id.toast_layout_tv);
            tv.setText(content);
            if (mToast == null) {
                Toast toast = new Toast(context);
                toast.setView(view);
                toast.setDuration(timer);
                mToast = toast;
            } else {
                mToast.setView(view);
                mToast.setDuration(timer);
            }
            mToast.show();
        }
    }

    public static void showToast(String content, boolean longTime) {
        showToast(content, longTime, "d");
    }

    /**
     * 小提示
     *
     * @param contentId
     *            提示内容id
     * @param longTime
     *            是否长时间提醒
     */
    public static void showToast(int contentId, boolean longTime) {
        String content = getString(contentId);
        showToast(content, longTime, "d");
    }

    /**
     * 获取字符串
     *
     * @return
     */
    public static String getString(int id) {
        return MyApplication.getInstances().getString(id);
    }

    /**
     * 保留小数
     * @param value
     * @param number 保留小数几位：1-1位、2-2位
     * @return String
     */
    public static String formatFloat(double value, int number) {
        DecimalFormat df = null;
        switch (number){
            case 1:
                df= new DecimalFormat("#0.0");
                break;
            case 2:
                df = new DecimalFormat("#0.00");
                break;
        }
        return df!=null?df.format(value):null;
    }

    /**
     * 文本加密
     *
     * @return
     */
    public static String getURLEncoder(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 文本解密
     *
     * @return
     */
    public static String getURLDecoder(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 加密字符串
     *
     * @param str 字符串
     * @param encryptionMode 加密模式（SHA1、MD5）
     * @return
     */
    public static String encryption(String str, String encryptionMode) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance(encryptionMode);
            mdTemp.update(str.getBytes());
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            String string = new String(buf);
            if (encryptionMode.equalsIgnoreCase("MD5")) {
                return string.toUpperCase();
            }else{
                return string;
            }
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        System.out.println("屏幕宽度：" + width);
        return width;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int height = outMetrics.heightPixels;
        System.out.println("屏幕高度：" + height);
        return height;
    }

    /**
     * 设置文本图片资源和图片大小
     * @param textView
     * @param drawable
     */
    public static void setTextDrawable(TextView textView, int drawable){
        Drawable d = MyApplication.getInstances().getResources().getDrawable(drawable);
        d.setBounds(0,0,40,40);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        textView.setCompoundDrawables(d,null,null,null);
    }
}
