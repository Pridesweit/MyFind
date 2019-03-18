package com.xiaonuo.find;

import android.annotation.SuppressLint;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.xiaonuo.find.dynamicweather.BaseDrawer;
import com.xiaonuo.find.dynamicweather.DynamicWeatherView;
import com.xiaonuo.find.utils.BaseActivity;
import com.xiaonuo.find.utils.Constant;
import com.xiaonuo.find.utils.HandlerCollection;
import com.xiaonuo.find.utils.MainActivityHandler;
import com.xiaonuo.find.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;


public class MainActivity extends BaseActivity {


    /**
     * 日志标签
     */
    public String TAG = "MainActivity";


    /**
     * 刷新控件
     */
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏透明化
        StatusBarUtil.setTransparent(this);

        MainActivityHandler myHandler = new MainActivityHandler(this);
        HandlerCollection.add(Constant.MAINACTIVITY_HANDLER_KEY, myHandler);

        //和风天气初始化
        HeConfig.init("HE1903161719101836", "d4ba6f275d6a450e8f619e94f8e9f785");

        //设定免费端口
        HeConfig.switchToFreeServerNode();

        //初始化底部TabLayout
        initTabLayoyt(savedInstanceState);

        //刷新控件的初始化
        initSwipeRefreshLayout();

        //动态背景的初始化
        initDynamicWeatherView();
    }

    private void initDynamicWeatherView() {
        updateDynamicWeatherView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DynamicWeatherView weatherView = findViewById(R.id.main_dynamicWeatherView_dynamicWeatherView);
        weatherView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DynamicWeatherView weatherView = findViewById(R.id.main_dynamicWeatherView_dynamicWeatherView);
        weatherView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DynamicWeatherView weatherView = findViewById(R.id.main_dynamicWeatherView_dynamicWeatherView);
        weatherView.onDestroy();
    }

    /**
     * 刷新控件的初始化
     * 设置颜色
     * 设置刷新内容
     * （1.获取和风天气的数据，Json存入SP）
     */
    private void initSwipeRefreshLayout() {
        //获取刷新控件
        refreshLayout = findViewById(R.id.main_swipeRefreshLayout_refreshLayout);
        //设定控件颜色（主题色）
        refreshLayout.setColorSchemeResources(R.color.theme);

        //设定刷新事件
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //更新sp天气数据
                updateWeatherSP();
            }
        });


    }

    /**
     * 获取和风天气数据
     * 存入sp
     * 更新sp天气数据
     */
    public void updateWeatherSP() {

        //获取刷新控件
        refreshLayout = findViewById(R.id.main_swipeRefreshLayout_refreshLayout);
        refreshLayout.setRefreshing(true);

        //天气监听器
        HeWeather.OnResultWeatherNowBeanListener onResultWeatherNowBeanListener = new HeWeather.OnResultWeatherNowBeanListener() {
            /**
             * 获取失败
             * @param e :错误信息
             */
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: ", e);

                //开启UI更新线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //关闭刷新
                        refreshLayout.setRefreshing(false);

                        //吐司提示
                        Toast.makeText(MainActivity.this, "刷新失败", Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 获取成功
             * @param dataObject :获取的天气数据集合
             */
            @Override
            public void onSuccess(List<Now> dataObject) {
                final String json = new Gson().toJson(dataObject);
                Log.d(TAG, "onSuccess: " + json);

                //子线程操作大任务
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //开启UI更新线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //关闭刷新
                                refreshLayout.setRefreshing(false);


                                //吐司提示
                                Toast.makeText(MainActivity.this, "刷新成功", Toast.LENGTH_LONG).show();
                            }
                        });

                        //存入天气json
                        Utils.putString(MainActivity.this, Constant.WEATHER_JSON_DATA, json);


                        Message msg = Message.obtain();
                        msg.what = Constant.WHAT_UPDATE_WEATHER_TextView_DATA;
                        HandlerCollection.get(Constant.HOMEFRAGMENT_HANDLER_KEY).sendMessage(msg);

                        updateDynamicWeatherView();

                    }
                }).start();

            }
        };

        //天气具体地址（区+城市）如雨湖,湘潭
        //获取SP中的地区，不存在则使用默认地区
        String district = Utils.getString(this, Constant.LOCAL_DISTRICT, Constant.DEFAULT_DISTRICT);

        //获取SP中的城市，不存在则使用默认城市
        String city = Utils.getString(this, Constant.LOCAL_CITY, Constant.DEFAULT_CITY);

        String specificAddress = district + "," + city;

        //获取天气
        //指定区域城市，语言，默认单位（米，英尺）
        HeWeather.getWeatherNow(this, specificAddress, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, onResultWeatherNowBeanListener);
    }

    public void updateDynamicWeatherView() {
        DynamicWeatherView weatherView = findViewById(R.id.main_dynamicWeatherView_dynamicWeatherView);

        String json = Utils.getString(MainActivity.this, Constant.WEATHER_JSON_DATA, null);
        if (json != null) {
            //获取天气描述
            int x = json.indexOf("cond_txt");
            int y = json.indexOf(":", x);
            int j = json.indexOf(",", x);
            String temp = json.subSequence(y + 2, j - 1).toString();
            if (temp.contains("雨") && temp.contains("雪")) {
                weatherView.setDrawerType(BaseDrawer.Type.RAIN_SNOW_D);
            } else if (temp.contains("雨")) {
                weatherView.setDrawerType(BaseDrawer.Type.RAIN_D);
            } else if (temp.contains("雪")) {
                weatherView.setDrawerType(BaseDrawer.Type.SNOW_D);
            } else if (temp.contains("晴")) {
                weatherView.setDrawerType(BaseDrawer.Type.CLEAR_D);
            } else if (temp.contains("云")) {
                weatherView.setDrawerType(BaseDrawer.Type.CLOUDY_D);
            } else if (temp.contains("阴")) {
                weatherView.setDrawerType(BaseDrawer.Type.OVERCAST_D);
            } else if (temp.contains("雾")) {
                weatherView.setDrawerType(BaseDrawer.Type.FOG_D);
            } else if (temp.contains("霾")) {
                weatherView.setDrawerType(BaseDrawer.Type.HAZE_D);
            } else if (temp.contains("沙") || temp.contains("尘")) {
                weatherView.setDrawerType(BaseDrawer.Type.SAND_D);
            } else if (temp.contains("风")) {
                weatherView.setDrawerType(BaseDrawer.Type.WIND_D);
            } else {
                weatherView.setDrawerType(BaseDrawer.Type.UNKNOWN_D);
            }
        } else {
            weatherView.setDrawerType(BaseDrawer.Type.UNKNOWN_D);
        }

        weatherView.onResume();
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
