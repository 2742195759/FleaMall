package com.univ.chat.util;

import com.android.volley.Cache;
import com.univ.chat.push.PushSDK;
import android.app.Application;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.univ.chat.gcm.MyPushReceiver;
import com.univ.chat.helper.SharedPrefHelper;
import com.univ.chat.push.lib.util.LogUtil;


public class ChatApplication extends Application {

    public static final String TAG = ChatApplication.class
            .getSimpleName();

    private RequestQueue volleyQueue;

    private static ChatApplication application;

    private SharedPrefHelper pref;

    static public MyPushReceiver mypushreceiver = null ;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.v("ChatApplication" , "onCreate");
        application = this;
    }

    @Override
    public void onTerminate() {
        LogUtil.v("ChatApplication" , "Unregister");
        PushSDK.getInstance().unRegisterPushListener(mypushreceiver);
        PushSDK.getInstance().unRegisterApp();
        super.onTerminate();
    }

    public static synchronized ChatApplication getInstance() {
        return application;
    }

    public RequestQueue getRequestQueue() {
        if (volleyQueue == null) {
            volleyQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return volleyQueue;
    }

    public SharedPrefHelper getPrefManager() {
        if (pref == null) {
            pref = new SharedPrefHelper(this);
        }

        return pref;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
}
