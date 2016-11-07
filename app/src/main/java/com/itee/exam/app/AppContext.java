package com.itee.exam.app;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itee.exam.R;
import com.itee.exam.app.ui.vo.Apply;
import com.itee.exam.core.crash.CrashHandler;
import com.itee.exam.core.utils.DateTypeAdapter;
import com.itee.exam.core.utils.NetworkUtils;
import com.itee.exam.core.utils.Toasts;
import com.itee.exam.rest.HttpService;
import com.itee.exam.utils.PreferenceUtil;
import com.itee.exam.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.URLConnectionNetworkExecutor;
import com.yolanda.nohttp.cache.DBCacheStore;
import com.yolanda.nohttp.cookie.DBCookieStore;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static com.itee.exam.core.utils.ContextUtils.isDebug;

/**
 * Created by xin on 2015-05-29.
 */
public class AppContext extends MultiDexApplication {

    /**
     * Intent参数，跳转到订单列表界面
     */
    public static final String TO_ORDER_ACTIVITY = "toOrdersActivity";
    public static final String TO_EXPERT_ORDER_ACTIVITY = "toExpertOrdersActivity";

    public static String SERVER_URL;
    public static String SERVER_IMG_URL;

    /**
     * 应用缓存根目录
     */
    public static String CACHE_DIR;

    private HttpService httpService;

    private NotificationManager notificationManager;
    /**
     * 后台运行的消息
     */
    private int newMsgNum = 0;
    private int recentNum = 0;

    private String sessionId;

    private int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
    private CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
    private int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided
    public static Apply apply;
    private static AppContext instance;

    public static AppContext getInstance() {
        return instance;
    }

    private void initStatic() {
        SERVER_URL = "http://" + Constants.SERVER_IP + ":" + Constants.SERVER_PORT + "/" + Constants.SERVER_HOME;
        SERVER_IMG_URL = "http://" + Constants.SERVER_IP + ":" + Constants.SERVER_PORT;
        CACHE_DIR = Environment.getExternalStorageDirectory() + "/exam";
        instance = this;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Utils.enableStrictMode();

        initStatic();
        PreferenceUtil.getInstance().init(this);
        // 注册未捕获异常处理器
        CrashHandler.getInstance().init(this);

        initImageLoader(this);

        CookieHandler.setDefault(new CookieManager() {

            @Override
            public void put(URI uri, Map<String, List<String>> stringListMap) throws IOException {
                super.put(uri, stringListMap);
                if (stringListMap.get("Set-Cookie") != null) {
                    for (String string : stringListMap.get("Set-Cookie")) {
                        if (string.contains("JSESSIONID")) {
                            //save your cookie here
                            sessionId = string;
                        }
                    }
                }
            }
        });
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SERVER_URL)
                .setConverter(new GsonConverter(gson))
                .setLogLevel(isDebug(this) ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.BASIC)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        if (sessionId != null) {
                            request.addHeader("Set-Cookie", "sessionId=" + sessionId);
                        }
                    }
                })
                .build();
        httpService = restAdapter.create(HttpService.class);
//        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        initNoHttp();
    }

    private void initNoHttp() {
        //初始化NoHttp
        NoHttp.initialize(this);
        NoHttp.initialize(this, new NoHttp.Config()
                // 设置全局连接超时时间，单位毫秒，默认10s。
                .setConnectTimeout(30 * 1000)
                // 设置全局服务器响应超时时间，单位毫秒，默认10s。
                .setReadTimeout(30 * 1000)
                // 配置缓存，默认保存数据库DBCacheStore，保存到SD卡使用DiskCacheStore。
                .setCacheStore(
                        new DBCacheStore(this).setEnable(true) // 如果不使用缓存，设置false禁用。
                )
                // 配置Cookie，默认保存数据库DBCookieStore，开发者可以自己实现。
                .setCookieStore(
                        new DBCookieStore(this).setEnable(false) // 如果不维护cookie，设置false禁用。
                )
                // 配置网络层，默认使用URLConnection，如果想用OkHttp：OkHttpNetworkExecutor。
                .setNetworkExecutor(new URLConnectionNetworkExecutor())
        );

    }

    /**
     * 初始化并配置ImageLoad
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, Constants.APP_NAME + "/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache()) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .writeDebugLogs() // Remove for release app
                .build();//开始构建


        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void showToastLong(@StringRes int resId) {
        Toasts.showToastInfoLong(this, resId);
    }

    public void showToastLong(@Nullable CharSequence text) {
        Toasts.showToastInfoLong(this, text);
    }

    public void showToastShort(@StringRes int resId) {
        Toasts.showToastInfoShort(this, resId);
    }

    public void showToastShort(@Nullable CharSequence text) {
        Toasts.showToastInfoShort(this, text);
    }

    public HttpService getHttpService() {
        return httpService;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public void setNotificationManager(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public boolean checkNetwork(boolean showToast) {
        boolean connected = NetworkUtils.isNetworkConnected(this);
        if (showToast && !connected) {
            showToastShort(R.string.toast_no_network);
        }
        return connected;
    }


    public int getNewMsgNum() {
        return newMsgNum;
    }

    public void setNewMsgNum(int newMsgNum) {
        this.newMsgNum = newMsgNum;
    }

    public int getRecentNum() {
        return recentNum;
    }

    public void setRecentNum(int recentNum) {
        this.recentNum = recentNum;
    }

    /**
     * 欺骗系统对话框是否关闭
     *
     * @param dialog
     * @param showing
     */
    public static void cheatSystem(DialogInterface dialog, boolean showing) {
        try {
            Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
            field.setAccessible(true);
            field.set(dialog, showing);
            dialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统打电话
     *
     * @param tel
     */
    public void callUp(String tel) {
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * 检查相机是否可用
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean checkCamera() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int numberOfCameras = Camera.getNumberOfCameras();
            return numberOfCameras > 0;
        } else {
            CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String[] cameraIdList = manager.getCameraIdList();
                return cameraIdList.length > 0;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static String getImgPath(String path) {
        if (path == null) return null;
        String temp = path.replace("\\", "/");
        return SERVER_IMG_URL + "/" + temp;
    }

    /**
     * 获取用户头像路径
     *
     * @param customerId
     * @return
     */
    public static String getCustomerFacePath(int customerId) {
        return SERVER_URL + "/data/customer/" + customerId + "/face.jpg";
    }

    /**
     * 获取服务端的需求目录
     *
     * @param demandId
     * @return
     */
    public static String getServerDemandDir(int demandId) {
        return SERVER_URL + "/data/demand/" + demandId + "/";
    }

    /**
     * 获取服务端服务报告音频和图片文件的访问路径
     *
     * @param orderId
     * @return
     */
    public static String getServerReportDir(int orderId) {
        return SERVER_URL + "/data/order/" + orderId + "/report/";
    }
}
