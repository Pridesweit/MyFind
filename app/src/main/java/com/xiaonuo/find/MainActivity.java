package com.xiaonuo.find;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.xiaonuo.find.ui.ScaleTransitionPagerTitleView;
import com.xiaonuo.find.utils.BaseActivity;
import com.xiaonuo.find.utils.Constant;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;


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

        //viewPager设置适配器
        viewPager.setAdapter(new MMAdapt(getSupportFragmentManager(), fragmentList));

        //tabLayout的初始化
        initMagicIndicator(fragmentList, viewPager);
    }

    /**
     * 初始化TabLayout
     *
     * @param fragmentList :Fragment集合
     * @param viewPager    :viewPager
     */
    private void initMagicIndicator(final List<Fragment> fragmentList, final ViewPager viewPager) {
        //获取底栏
        MagicIndicator magicIndicator = findViewById(R.id.Main_Tablayout_mainButtom);

        //设置颜色
//        magicIndicator.setBackgroundColor(Color.WHITE);

        //字体类获取
        CommonNavigator commonNavigator = new CommonNavigator(this);

        //设置适配器
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            /**
             * 设置页面数量
             * @return
             */
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            /**
             * 设置字体
             * @param context
             * @param index
             * @return
             */
            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(Constant.TITLES[index]);
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            /**
             * 设置字体被选中
             * @param context
             * @return
             */
            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"), Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"));
                return indicator;
            }
        });

        //设置适配器
        magicIndicator.setNavigator(commonNavigator);

        //绑定viewPager和magicIndicator
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    private class MMAdapt extends FragmentPagerAdapter {
        //内置fragments
        List<Fragment> fragments;

        public MMAdapt(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        /**
         * 获取某个fragment
         *
         * @param i
         * @return
         */
        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        /**
         * 获取总数量
         *
         * @return
         */
        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
