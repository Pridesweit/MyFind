package com.xiaonuo.find.utils;

/**
 * Created by Administrator on 2018/1/29.
 */

public class Constant {


    /**
     * 定位默认区
     */
    public static final String DEFAULT_DISTRICT = "朝阳";


    /**
     * 定位默认城市
     */
    public static final String DEFAULT_CITY = "北京";


    /**
     * SP中存取天气json的key
     */
    public static final String WEATHER_JSON_DATA = "weather_json_data";


    /**
     * SP中存取城市信息（省份）的key
     */
    public static final String LOCAL_PROVINCE = "Local_province";


    /**
     * SP中存取城市信息（城市）的key
     */
    public static final String LOCAL_CITY = "Local_city";


    /**
     * SP中存取城市信息（地区，区）的key
     */
    public static final String LOCAL_DISTRICT = "Local_district";


    /**
     * message.what的值：MainActivity handler进行天气数据的更新
     */
    public static final int WHAT_UPDATE_WEATHER_DATA = 100;


    /**
     * message.what的值：HomeFragment handler进行天气TextView的更新
     */
    public static final int WHAT_UPDATE_WEATHER_TextView_DATA = 101;


    /**
     * HandlerCollection中MainActivityHandler的Key
     */
    public static final int MAINACTIVITY_HANDLER_KEY = 1000;


    /**
     * HandlerCollection中HomeFragmentHandler的Key
     */
    public static final int HOMEFRAGMENT_HANDLER_KEY = 1001;


}
