package com.xiaonuo.find;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.xiaonuo.find.utils.BaseActivity;
import com.xiaonuo.find.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;


public class MainActivity extends BaseActivity {

    /**
     * 日志标签
     */
    public String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar隐藏，透明化任务栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        //初始化底部TabLayout
        initTabLayoyt(savedInstanceState);
    }


    /**
     * 初始化底部TabLayout
     *
     * @param savedInstanceState :界面意外onDestroy后，onCreate数据重载
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initTabLayoyt(Bundle savedInstanceState) {
        List<Fragment> fragmentList = new ArrayList<>();
        //主页
        fragmentList.add(new HomeFragment());
        //好友
        fragmentList.add(new FriendFragment());
        //我的
        fragmentList.add(new MyFragment());

        //获取viewPager和tabLayout进行tabLayout的初始化
        ViewPager viewPager = findViewById(R.id.Main_viewPager_viewpager);

        //设置缓存页数
        viewPager.setOffscreenPageLimit(5);

        //消费ViewPager的触摸事件，使得ViewPager不可滑动
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        //tabLayout的初始化
        SpaceTabLayout tabLayout = findViewById(R.id.main_spaceTabLayout_tabLayout);
        tabLayout.initialize(viewPager, getSupportFragmentManager(), fragmentList, savedInstanceState);
    }
}
