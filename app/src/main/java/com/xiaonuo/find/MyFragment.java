package com.xiaonuo.find;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaonuo.find.dynamicweather.BaseDrawer;
import com.xiaonuo.find.dynamicweather.DynamicWeatherView;

public class MyFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ccc, container, false);


        return inflate;
    }
}
