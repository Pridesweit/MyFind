package com.xiaonuo.find;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class MyFragment extends android.support.v4.app.Fragment {

    private View layout;
    private ImageView mIvAvatar;
    private Uri resultUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        Glide.with(this).load(R.drawable.circle_captcha)
//
//                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
//
//                .into(h_back);
//
//        //设置圆形图像
//
//        Glide.with(this).load(R.drawable.head)
//
//                .bitmapTransform(new CropCircleTransformation(this))
//
//                .into(mHHead);
//
//
//        layout = inflater.inflate(R.layout.fragment_my, container, false);


//        Glide.with(this)
//                .load(R.drawable.head)
//                .dontAnimate()
//                //加载过程中的图片显示
//                .placeholder(R.mipmap.arrow_right)
//                //加载失败中的图片显示
//                //如果重试3次（下载源代码可以根据需要修改）还是无法成功加载图片，则用错误占位符图片显示。
//                .error(R.mipmap.arrow_right)
//                //第二个参数是圆角半径，第三个是模糊程度，2-5之间个人感觉比较好。
//                .bitmapTransform(new BlurTransformation(PersonalActivity.this, 14, 1))
//                .into(h_head);
//        Glide.with(this).load("http://guolin.tech/book.png").into(layout.findViewById(R.id.h_head));




        return layout;
    }

    public static void alphaTask(Activity context) {
        context.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}











