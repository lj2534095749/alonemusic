package com.example.alonemusic.test;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private MyBinder myBinder = new MyBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("myService", "onBind");
        return myBinder;
    }

    public String doSomeOperation(String param) {
        Log.i("myService", "doSomeOperation: param = " + param);
        return "return value";
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i("myService", "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("myService", "onUnbind");
        //return super.onUnbind(intent);
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("myService", "onStartCommand: " + ",flags:" + flags
                + ",startId:" + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("myService", "onDestroy");
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }
}
