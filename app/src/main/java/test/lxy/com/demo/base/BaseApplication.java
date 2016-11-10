package test.lxy.com.demo.base;

import android.app.Application;

import test.lxy.com.demo.crash.CrashHandler;

/**
 * Created by lxy on 16/11/9.
 */

public class BaseApplication extends Application {

    private static BaseApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        //初始化异常日志收集类
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

    }

    public static BaseApplication getInstance() {
        return sInstance;
    }
}
