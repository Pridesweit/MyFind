package com.xiaonuo.find.utils;

import android.os.Handler;
import android.os.Message;

import com.xiaonuo.find.HomeFragment;
import com.xiaonuo.find.MainActivity;

import java.lang.ref.WeakReference;

public class HomeFragmentHandler extends Handler {
    WeakReference<HomeFragment> mainActivityWeakReference;

    public HomeFragmentHandler(HomeFragment fragment) {
        mainActivityWeakReference = new WeakReference<HomeFragment>(fragment);
    }

    public void handleMessage(Message msg) {

        HomeFragment fragment = mainActivityWeakReference.get();

        switch (msg.what) {
            case Constant.WHAT_UPDATE_WEATHER_TextView_DATA:
                //更新TextView
                fragment.updateTextView();
                break;
        }

    }
}
