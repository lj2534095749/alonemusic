package com.example.alonemusic.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.alonemusic.R;

public class ServiceActivity extends AppCompatActivity {

    private Button start;
    private Button startClose;
    private Button bind;
    private Button operate;
    private Button bindClose;
    private MyService myService;
    private ServiceConnection serviceConnection;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        initAttribute();
        initAttributeListener();
    }

    private void initAttribute(){
        isConnected = false;
        start = findViewById(R.id.start);
        startClose = findViewById(R.id.startClose);
        bind = findViewById(R.id.bind);
        operate = findViewById(R.id.operate);
        bindClose = findViewById(R.id.bindClose);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.i("myService",
                        "ServiceConnection.onServiceConnected():name = " + name);
                myService = ((MyService.MyBinder) service).getService();
                isConnected = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("myService",
                        "ServiceConnection.onServiceDisconnected():name = " + name);
                myService = null;
                isConnected = false;
            }
        };
    }

    private void initAttributeListener(){
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, MyService.class);
                startService(intent);
            }
        });
        startClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, MyService.class);
                stopService(intent);
            }
        });
        bind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, MyService.class);
                bindService(intent, serviceConnection,
                        Context.BIND_AUTO_CREATE);
            }
        });
        operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myService == null){
                    Log.d("myService", "myService = null");
                    return;
                }
                String returnValue = myService.doSomeOperation("test");
                Log.i("myService", "myService.doSomeOperation: "
                        + returnValue);
            }
        });
        bindClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected == false){
                    Log.d("myService", "isConnected == false");
                    return;
                }
                unbindService(serviceConnection);
                isConnected = false;
            }
        });
    }
}
