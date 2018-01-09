package com.explame.AutoPicture;

import android.app.Application;
import android.content.Context;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import org.androidannotations.annotations.EApplication;
@EApplication
public class CPApplication extends Application {

    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //初始化图片加载类
        initImageLoader(this);
    }

    public static Context getContext() {
        return context;
    }


    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create ic_default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)//降低线程的优先级保证主UI线程不受太大影响
                .tasksProcessingOrder(QueueProcessingType.FIFO) // ic_default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new FIFOLimitedMemoryCache(10 * 1024 * 1024))//建议内存设在5-10M,可以有比较好的表现
                .memoryCacheSize(10 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .diskCache(new LimitedAgeDiskCache(StorageUtils.getCacheDirectory(context), System.currentTimeMillis()))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO);
        // .writeDebugLogs(); // Remove for release app

//        config.threadPoolSize(3);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
//        config.build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}
