package com.julius.worksubmit;

import android.app.Application;
import android.content.Context;

import org.xutils.x;

/**
 * author  julius
 * date    2019/3/19.
 * insert  describe this
 */

public class MyApplication extends Application {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        context = getApplicationContext();
    }
}
