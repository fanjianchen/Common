package com.sdk.appcommon.common.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 自定义加载圆形图片
 * @author fanjianchen
 * @date 2018/4/3
 */
public class CustomImageViewCircle extends BitmapTransformation {
    public static boolean isTransparent = false;

    /**
     * 自定义加载圆形图片
     * @param context 上下文
     * @param isTransparent 是否有透明边框
     */
    public CustomImageViewCircle(Context context, boolean isTransparent) {
        super(context);
        this.isTransparent = isTransparent;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int otWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        /**
         * 这里的操作可以自己绘制任何你想要的图形,例如带边框,边框带色等等
         */
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);//设置画笔为无锯
        float r = size / 2f;
        if (isTransparent) {
            canvas.scale(1.2f,1.2f,400,200);
            canvas.drawCircle(r, r, (float)(r/1.4), paint);//圆形X轴、圆心Y轴、半径、画笔
        } else {
            canvas.drawCircle(r, r, r, paint);//圆形X轴、圆心Y轴、半径、画笔
        }
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}