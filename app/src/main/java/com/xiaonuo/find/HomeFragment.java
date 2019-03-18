package com.xiaonuo.find;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.xiaonuo.find.utils.Constant;
import com.xiaonuo.find.utils.HandlerCollection;
import com.xiaonuo.find.utils.HomeFragmentHandler;
import com.xiaonuo.find.utils.Utils;

public class HomeFragment extends android.support.v4.app.Fragment {

    /**
     * 获取城市
     */
    CityPickerView mPicker = new CityPickerView();

    /**
     * 主布局
     */
    View layout = null;


    /**
     * 标签
     */
    private String TAG = "HomeFragment";


    public void updateTextView() {


        String json = Utils.getString(getContext(), Constant.WEATHER_JSON_DATA, null);
        if (json != null) {

            //获取温度
            int x = json.indexOf("tmp");
            int y = json.indexOf(":", x);
            int j = json.indexOf(",", x);
            String temp = json.subSequence(y + 2, j - 1).toString();
            TextView temperature = layout.findViewById(R.id.homeFragment_textView_temperature);
            temperature.setText(temp);

            //天气描述+体感温度
            x = json.indexOf("cond_txt");
            y = json.indexOf(":", x);
            j = json.indexOf(",", x);
            temp = json.subSequence(y + 2, j - 1).toString();
            x = json.indexOf("fl");
            y = json.indexOf(":", x);
            j = json.indexOf(",", x);
            temp = temp + " | 体感 " + json.subSequence(y + 2, j - 1).toString() + " °";
            TextView typeAndRealFeel = layout.findViewById(R.id.homeFragment_textView_typeAndRealFeel);
            typeAndRealFeel.setText(temp);


            //获取刷新时间
            x = json.indexOf("loc\"");
            y = json.indexOf(":", x);
            j = json.indexOf(",", x);
            temp = json.subSequence(y + 2, j - 1).toString();
            Log.e("aa", temp);
            temp = temp.split(" ")[1];
            TextView visibility = layout.findViewById(R.id.homeFragment_textView_updateTime);
            visibility.setText(temp + " 数据");

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化城市选择
        initPickCity();

        HomeFragmentHandler handler = new HomeFragmentHandler(this);
        HandlerCollection.add(Constant.HOMEFRAGMENT_HANDLER_KEY, handler);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_home, container, false);

        updateTextView();
        updateRegionTextView();
        //初始化城市选择按钮
        initTouchPickCityButton();

        return layout;
    }

    /**
     * 初始化城市选择按钮
     */
    private void initTouchPickCityButton() {

        TextView region = layout.findViewById(R.id.homeFragment_textView_region);

        //设定点击监听
        region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //显示
                mPicker.showCityPicker();
            }
        });
    }

    /**
     * 初始化城市选择
     */
    private void initPickCity() {

        //预先加载仿iOS滚轮实现的全部数据
        mPicker.init(getContext());

        //配置选择城市信息
        CityConfig cityConfig = new CityConfig.Builder()
                //标题
                .title("选择城市")

                //标题文字大小
                .titleTextSize(18)

                //标题文字颜  色
                .titleTextColor("#585858")

                //标题栏背景色
                .titleBackgroundColor("#E9E9E9")

                //确认按钮文字颜色
                .confirTextColor("#31c27c")

                //确认按钮文字
                .confirmText("确定")

                //确认按钮文字大小
                .confirmTextSize(16)

                //取消按钮文字颜色
                .cancelTextColor("#585858")

                //取消按钮文字
                .cancelText("取消")

                //取消按钮文字大小
                .cancelTextSize(16)

                //显示类，只显示省份一级，显示省市两级还是显示省市区三级
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)

                //是否显示半透明背景
                .showBackground(true)

                //显示item的数量
                .visibleItemsCount(5)

                //默认显示的省份
                .province("湖南省")

                //默认显示省份下面的城市
                .city("湘潭市")

                //默认显示省市下面的区县数据
                .district("雨湖区")

                //省份滚轮是否可以循环滚动
                .provinceCyclic(true)

                //城市滚轮是否可以循环滚动
                .cityCyclic(true)

                //区县滚轮是否循环滚动
                .districtCyclic(true)

                //滚轮不显示模糊效果
                .drawShadows(true)

                //中间横线的颜色
                .setLineColor("#03a9f4")

                //中间横线的高度
                .setLineHeigh(5)

                //是否显示港澳台数据，默认不显示
                .setShowGAT(true)
                .build();

        //使设置生效
        mPicker.setConfig(cityConfig);

        //监听选择情况
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            /**
             * 成功选择城市
             * @param province：省份
             * @param city：城市
             * @param district：地区
             */
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                //存省份到SP（去除“省”用于和风天气查询）
                Utils.putString(getContext(), Constant.LOCAL_PROVINCE, province.getName().replaceAll("省", ""));

                //存市到SP（去除“市”用于和风天气查询）
                Utils.putString(getContext(), Constant.LOCAL_CITY, city.getName().replaceAll("市", ""));

                //存区到SP（去除“区”和“市”用于和风天气查询）
                Utils.putString(getContext(), Constant.LOCAL_DISTRICT, district.getName().replaceAll("市", "")
                        .replaceAll("区", ""));

                //吐司提示
                Toast.makeText(getContext(), "设定成功", Toast.LENGTH_SHORT).show();

                //更新显示的地址（数据来源SP）
                updateRegionTextView();

                //更新天气数据
                upDateWeatherData();
            }

            /**
             * 取消
             */
            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 更新天气数据
     */
    private void upDateWeatherData() {
        //拉取和风天气的数据存入SP
        upDateWeatherDataToSP();
    }

    /**
     * 拉取和风天气的数据存入SP
     */
    private void upDateWeatherDataToSP() {
        //获取实例信息
        Message msg = Message.obtain();

        //设置信息类型
        msg.what = Constant.WHAT_UPDATE_WEATHER_DATA;

        //发送消息至MainActivity
        HandlerCollection.get(Constant.MAINACTIVITY_HANDLER_KEY).sendMessage(msg);
    }

    /**
     * 更新显示的地址（数据来源SP）
     */
    private void updateRegionTextView() {
        TextView region = layout.findViewById(R.id.homeFragment_textView_region);

        //获取SP中的地区，不存在则使用默认地区
        String district = Utils.getString(getContext(), Constant.LOCAL_DISTRICT, Constant.DEFAULT_DISTRICT);

        //获取SP中的城市，不存在则使用默认城市
        String city = Utils.getString(getContext(), Constant.LOCAL_CITY, Constant.DEFAULT_CITY);

        //设定文本
        region.setText(district + "-" + city);
    }


}
