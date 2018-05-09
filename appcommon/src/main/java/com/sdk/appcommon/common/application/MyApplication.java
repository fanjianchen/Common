package com.sdk.appcommon.common.application;

import android.app.Application;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * @author Fanjianchen
 * @date 2018/3/29
 */
public class MyApplication extends Application {
    /**
     * 静态单例
     */
    private static MyApplication instances;
    /**
     * 网络请求对象
     */
    public static OkHttpClient mOkHttpClient;
    /**
     * 获取本应用从操纵系统那里分配的最大内存，使用八分之一
     */
    private static int DISK_IMAGECACHE_SIZE = (int) Runtime.getRuntime().maxMemory() / 8;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        setHttp();
    }

    /**
     * 获取全局上下文
     * @return
     */
    public static MyApplication getInstances() {
        return instances;
    }


    //     connectTimeout：指http建立通道的时间，我们知道http底层是基于TCP/IP协议的，而TCP协议有个三次握手协议，所谓三次握手简单的理解为
    //     客户端问服务端：我要准备给你发数据了，你准备好了么。
    //     服务端向客户端回答：我准备好了，你可以发数据了。
    //     客户端回答服务端：我收到你的消息了，我要发数据了。
    //     然后巴拉巴拉一堆数据过去了。 这里就能看出来，只有这三次握手建立后，才能开始发送数据，
    //     否则数据是无法发送的，那么建立这个通道的时间就叫做connectTimeout，想一想，如果我们
    //     网络不好，平均建立这个通道就要10秒，结果我们代码中设定的这个时间是5秒，那么这个连接
    //     永远建立不起来，建立到一半，就中断了。
    //     writeTimeout：基于前面的通道建立完成后，客户端终于可以向服务端发送数据了，客户端发
    //     送数据是不是要把数据写出去啊，所以叫写入时间，突然，服务器挂了，客户端能知道服务器
    //     挂了么，不知道的，所以客户端还在继续傻傻的向服务端写数据，可是服务端能收到这个数据
    //     么，肯定收不到，服务端都挂了，怎么收，同样的，客户端这个数据其实是写不出去的，客户
    //     端又写不出去，他又不知道服务端不能接受数据了，难道要一直这么等着服务端缓过来？肯定
    //     是不可能的哈，这样会造成资源的极端浪费，所以这个时候就有个writeTimeout时间控制这个
    //     傻傻的客户端要等服务端多长时间。
    //     readTimeout：继续前面的，现在通道连接建立完成了，客户端也终于把数据发给服务端了，
    //     服务端巴拉巴拉一顿计算，把客户端需要的数据准备好了，准备返回给客户端。but，要搞事
    //     情了，网络不通或者客户端出了毛病，客户端无法接受到服务端的数据了，类比之前的分析，
    //     客户端要这么傻傻的等着服务端发数据么，就算你等着他也发不过来了是不，这时候就有了
    //     个readTimeout时间来控制这个过程，告诉客户端收不到服务端的数据时，要傻傻等多久。
    //     现在这三个时间我们都有了印象，他是控制了http进行数据交互的三个阶段的超时时间，
    //     试想一下，假如我们把这三个时间都设置为一分钟，那么最坏最巧合的时候，刚好connectTimeout
    //     要超时候，啪，连上了，然后刚好writeTimeout要超时的时候，啪，数据发出去了，然后又刚好
    //     readTimeout要超时的时候，啪，数据收到了，所以你等了三分钟，依然没有超时，数据还能正常
    //     收到。懂了么？只是这种情况实在太难遇到！
    /**
     * 配置OkHttp网络框架
     */
    private void setHttp() {
        //设置缓存文件地址
        File cacheFile = new File(getApplicationContext().getCacheDir().getAbsolutePath(), "cache_hc");//文件名：cache_hc
        int cacheSize = 100 * 1024 * 1024;//设置缓存文件大小—100M
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)//连接超时时间
                .writeTimeout(60, TimeUnit.SECONDS)//写的时间
                .readTimeout(60, TimeUnit.SECONDS)//读的时间
                .cache(new Cache(cacheFile, cacheSize));
        mOkHttpClient = builder.build();
    }
}
