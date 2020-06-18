package com.example.alonemusic.activity;

import android.content.Intent;
import android.view.KeyEvent;

import com.example.alonemusic.R;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.xuexiang.xui.widget.activity.BaseSplashActivity;

public class SplashActivity extends BaseSplashActivity {

    @Override
    protected long getSplashDurationMillis() {
        return 500;
    }

    @Override
    public void onCreateActivity() {
        initSplashView(R.drawable.bg_splash);
        startSplash(true);
    }

    @Override
    public void onSplashFinished() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event);
    }
}
