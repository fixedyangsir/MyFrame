package com.pmjyzy.android.frame.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageUtil {
    private DisplayImageOptions options;
    private ImageLoader loader;
    private static ImageUtil iu;
    private static Context tempContext;
    private List<File> files = new ArrayList<File>();

    private int loadingDrawableId = -1;
    private int errorDrawableId = -1;


    private ImageUtil(Context context) {
        // 设置配置参数
        setImageLoaderConfig(context);
    }

    private ImageUtil(Context context, int loadingDrawableId, int errorDrawableId) {
        // 设置配置参数
        this.loadingDrawableId = loadingDrawableId;
        this.errorDrawableId = errorDrawableId;
        setImageLoaderConfig(context);
    }


    /**
     * @param context
     * @param loadingDrawableId 下载时显示的图片，没有时传-1.
     * @param errorDrawableId   错误时显示的图片，没有时传-1.
     * @return
     */
    public static ImageUtil getImageUtil(Context context, int loadingDrawableId, int errorDrawableId) {
        if (iu == null || !tempContext.equals(context)) {
            synchronized (ImageUtil.class) {
                if (iu == null || !tempContext.equals(context)) {
                    iu = new ImageUtil(context, loadingDrawableId, errorDrawableId);
                }
            }
        }
        tempContext = context;
        return iu;
    }

    /**
     * 拿到单例imageUtil
     *
     * @param context
     * @return
     */
    public static ImageUtil getImageUtil(Context context) {
        if (iu == null || !tempContext.equals(context)) {
            synchronized (ImageUtil.class) {
                if (iu == null || !tempContext.equals(context)) {
                    iu = new ImageUtil(context);
                }
            }
        }
        tempContext = context;
        return iu;
    }

    /**
     * 异步获取网络图片
     *
     * @param url
     * @param view
     */

    public void setUrlImage(String url, ImageView view) {
        loader.displayImage(url, view, options);

    }


    public void setUrlImage(String url, ImageView imageView, ImageLoadingListener listener) {
        loader.displayImage(url, imageView, options, listener);
    }


    /**
     * 获得网络图片
     */

    public void loadImage(String url, final String savePath,
                          final Handler handler, final int what, final int size) {

        Log.i("result", "url==" + url);
        loader.loadImage(url, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String arg0, View arg1) {

            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
                headImage = getPhotoFileName();
                save(bitmap, savePath);
                Log.i("result", "headImage==" + headImage);
                files.add(getLoadImage(savePath));

                if (files.size() == size) {
                    Message message = handler.obtainMessage();
                    message.what = what;
                    message.obj = files;
                    handler.sendMessage(message);
                }

            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setImageLoaderConfig(Context context) {
        // 设置缓存的路径
        File file = null;
        if (SDCardUtils.isSDCardEnable()) {
            file = new File(Environment.getExternalStorageDirectory(),
                    "/file/Images");
            if (!file.exists()) {
                file.mkdirs();
            }
            loader = ImageLoader.getInstance();
            // 下载配置
            ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                    context).memoryCacheExtraOptions(720, 1280)
                    // 缓存图片最大长宽
                    .memoryCacheSize(2 * 1024 * 1024)
                            // 缓存到内存的最大数据
                    .discCacheSize(50 * 1024 * 1024). // 缓存到文件的最大数据
                            discCacheFileCount(100)
                    .threadPoolSize(3)// 加载的线程数
                    .diskCache(new UnlimitedDiskCache(file))
                    .build();
            // 初始化配置
            if (!loader.isInited()) {
                Log.i("result", "ImageLoader");
                loader.init(configuration);
            }


            // 显示设置
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                    // .showImageOnLoading(R.drawable.trade_prefer_item)//设置图片在下载期间显示的图片
                    .cacheOnDisc(true)
                            // 允许缓存放在sd卡
                            //.resetViewBeforeLoading(true)
                            // 设置图片在下载前是否重置，复位
                    .imageScaleType(ImageScaleType.EXACTLY)
                            // 设置图片以如何的编码方式显示
                            // 设置下载的图片是否缓存在内存中

                            // EXACTLY :图像将完全按比例缩小的目标大小

                            // EXACTLY_STRETCHED:图片会缩放到目标大小完全

                            // IN_SAMPLE_INT:图像将被二次采样的整数倍

                            // IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小

                            // NONE:图片不会调整

                            // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                    .bitmapConfig(Bitmap.Config.RGB_565);


            // .displayer(new FadeInBitmapDisplayer(1000))//
            // 是否图片加载好后渐入的动画时间;
            if (loadingDrawableId != -1) {

                builder.showImageOnLoading(tempContext.getResources().getDrawable(loadingDrawableId));

            }

            // 设置图片Uri为空或是错误的时候显示的图片
            if (errorDrawableId != -1) {
                builder.showImageForEmptyUri(tempContext.getResources().getDrawable(errorDrawableId));
            }
            options = builder.build();


        }
    }


    private String headImage = "";

    /**
     * 保存图片
     *
     * @param bitmap
     */
    private void save(Bitmap bitmap, String savePath) {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            try {
                File file = new File(Environment.getExternalStorageDirectory(),
                        savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                headImage = getPhotoFileName();
                file = new File(file, headImage);
                FileOutputStream stream = new FileOutputStream(file);
                bitmap.compress(CompressFormat.PNG, 0, stream);
                stream.flush();
                stream.close();
                Log.i("result", "图片保存成功");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            Toast.makeText(tempContext, "没有SD卡", Toast.LENGTH_SHORT).show();
        }

    }


    int num = 0;

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        String filename = "";
        Date date = new Date(System.currentTimeMillis());
        // 系统当前时间/
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        filename = dateFormat.format(date) + "_" + num + ".png";
        num++;
        return filename;
    }

    /**
     * 获取下载的图片
     */
    public File getLoadImage(String savePath) {

        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {
                File file = new File(Environment.getExternalStorageDirectory(),
                        savePath);
                if (file.exists()) {
                    file = new File(file, headImage);
                    return file;
                }
            }
        } catch (Exception e) {

            return null;
        }
        return null;
    }
}
