
package com.univ.chat.push.lib.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.univ.chat.push.lib.util.LogUtil;
import com.univ.chat.push.lib.util.NetworkUtil;

/**
 * 后台服务类
 * @author DexYang
 *
 */
public class PushService extends Service {

	private Client mClient=null;
	private String uuid = null ;

    private BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.v("NetWork", "NetWorkChange") ;
            if (intent.getAction() == ConnectivityManager.CONNECTIVITY_ACTION) {
                if(NetworkUtil.isNetworkAvailable(getApplicationContext())) {
                    LogUtil.v("PushService", "NetWorkAvaliable") ;
                    mClient.reconn();
                }
            }
        }
    };

	@Override
	public void onCreate() {
		super.onCreate();
LogUtil.v("PushService", "onCreate() , Registed Receiver");
		//android.os.Debug.waitForDebugger();   你个SB
		mClient=new Client(this);
		IntentFilter filter = new IntentFilter() ;
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectivityReceiver, filter);
    }
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handleCommand(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	private void handleCommand(Intent intent)
	{
		if(null==intent)
		{
			LogUtil.v("PushService","handleCommand() pid:"+(android.os.Process.myPid())+" tid:"+(android.os.Process.myTid()));
			return;
		}
		if(intent.getExtras().get("uuid") != null) {
			uuid = (String) intent.getExtras().get("uuid");
		}
		if(NetworkUtil.isNetworkAvailable(getApplicationContext()))
		{
			if(uuid == null)
				mClient.open(mSocketResponseListener);
			else
				mClient.open(mSocketResponseListener , uuid);
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
LogUtil.v("PushService", "onBind()");
		//if(PushService.class.getName().equals(arg0.getAction()))
		{
			handleCommand(arg0);
			return mBinder;
		}
		//return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
LogUtil.v("PushService", "onUnbind()");
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
LogUtil.v("PushService", "onDestroy()");
        unregisterReceiver(mConnectivityReceiver);
		mClient.close();
		mCallbacks.kill();

		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}
	
	/**
	 * socket数据回调
	 */
	private ISocketResponse mSocketResponseListener=new ISocketResponse() {
		

		@Override
		public void onSocketResponse(int code ,Object retObject) {
			
    		Bundle mBundle=new Bundle();
    		mBundle.putByteArray("data", (byte[])retObject);
			final int callbacksNum = mCallbacks.beginBroadcast();
	        for (int i=callbacksNum-1; i>=0; i--) 
	        {
	            try {
   					mCallbacks.getBroadcastItem(i).response(mBundle);;
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	        }
	        mCallbacks.finishBroadcast();
		}
	};
	
	private final CusRemoteCallbackList<ISocketServiceCallback> mCallbacks= new CusRemoteCallbackList<ISocketServiceCallback>();
	private ISocketService.Stub mBinder=new ISocketService.Stub() {

		///使用AIDL来定义接口,然后.stub()会自动生成一个类,我们需要继承然后
		///override这个接口.
		///stub()也是IBindle类型.
		@Override
		public int request(Bundle mBundle) throws RemoteException {
			
			byte[] data=mBundle.getByteArray("data");
			if(null!=data)
			{
				return mClient.send(new ClientPacket(data));
			}
			return 0;
		}

		@Override
		public void registerCallback(Bundle mBundle, ISocketServiceCallback cb)
				throws RemoteException {
		     LogUtil.v("PushService" , "go in the registerCallback") ;
			 if (cb != null)
			 {
				 boolean isRegistered = mCallbacks.register(cb);
LogUtil.v("PushService","registerCallback isRegistered"+isRegistered);
			 } 
		}

		@Override
		public void unregisterCallback(Bundle mBundle, ISocketServiceCallback cb)
				throws RemoteException {
			if (cb != null) 
			{
				boolean isUnregistered=mCallbacks.unregister(cb);
LogUtil.v("PushService","registerCallback isUnregistered"+isUnregistered);
			}
		}

	};
	
	/**
	 * 经过测试onCallbackDied()方法，只有在bindService()，没有调用unbind()方法process就挂了的情况下才会执行
	 * @author Administrator
	 *
	 * @param <E>
	 */
	private class CusRemoteCallbackList<E extends IInterface> extends RemoteCallbackList<E>
	{
		@Override
		public void onCallbackDied(E callback) {
LogUtil.v("PushService", "CusRemoteCallbackList onCallbackDied 1");
			super.onCallbackDied(callback);

		}

		@Override
		public void onCallbackDied(E callback, Object cookie) {
LogUtil.v("PushService", "CusRemoteCallbackList onCallbackDied 2");
			super.onCallbackDied(callback, cookie);
		}
	}
}
