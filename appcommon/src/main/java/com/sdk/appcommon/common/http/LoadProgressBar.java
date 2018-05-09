package com.sdk.appcommon.common.http;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sdk.appcommon.R;


/**
 * 加载框
 * @author Fanjianchen
 * @date 2018/4/3
 */
public class LoadProgressBar extends Dialog {
    private TextView loadMessage;
    public LoadProgressBar(Context context) {
        super(context, R.style.transparent_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_progressbar);
        loadMessage = findViewById(R.id.load_message);
    }

    /**
     * 当没有消息时，只展示动画效果
     */
    public void showMessage(String message){
        try {
            super.show();
            if (!TextUtils.isEmpty(message)){
                loadMessage.setText(message);
                loadMessage.setVisibility(View.VISIBLE);
            }else{
                loadMessage.setText("");
                loadMessage.setVisibility(View.GONE);
            }
        }catch (Exception e){
           e.printStackTrace();
        }
    }
}
