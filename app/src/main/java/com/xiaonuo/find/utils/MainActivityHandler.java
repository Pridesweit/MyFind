package com.xiaonuo.find.utils;

import android.os.Handler;
import android.os.Message;

import com.xiaonuo.find.MainActivity;

import java.lang.ref.WeakReference;

public class MainActivityHandler extends Handler {
    WeakReference<MainActivity> mainActivityWeakReference;

    public MainActivityHandler(MainActivity activity) {
        mainActivityWeakReference = new WeakReference<MainActivity>(activity);
    }

    public void handleMessage(Message msg) {

        MainActivity mainActivity = mainActivityWeakReference.get();

        switch (msg.what) {
            case Constant.WHAT_UPDATE_WEATHER_DATA:
                //更新sp天气数据
                mainActivity.updateWeatherSP();
                break;
        }

    }
}
