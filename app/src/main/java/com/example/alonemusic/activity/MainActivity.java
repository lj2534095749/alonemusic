package com.example.alonemusic.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alonemusic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.lang.reflect.Method;

public class MainActivity extends BaseActivity {

    Toolbar toolbar;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar_project);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_contacts, R.id.navigation_notifications, R.id.navigation_home)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
//        toolbar.inflateMenu(R.menu.main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_map:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0, 0"));
                startActivity(intent);
                break;
            case R.id.menu_telephone:
                call("1008611");
                break;
            case R.id.menu_browser:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
                startActivity(intent);
                break;
            case R.id.menu_email:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:qst@163.com"));
                startActivity(intent);
                break;
            case R.id.menu_insertMusic:
                if(Environment.isExternalStorageEmulated()){
                    Log.d("1111111", "onCreate: " + Environment.getExternalStorageDirectory());
                    Log.d("1111111", "onCreate: " + getExternalFilesDir(Environment.DIRECTORY_MUSIC));
                    Log.d("1111111", "onCreate: " + Environment.getDownloadCacheDirectory());
                }
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){}
                catch(Exception e){}
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK)
            return true;
        return super.onKeyDown(keyCode, event);
    }
}
