package com.xiaonuo.find;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MyFragment extends android.support.v4.app.Fragment {

    private View layout;
    private ImageView mIvAvatar;
    private Uri resultUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_my, container, false);

        layout.findViewById(R.id.aaaaaaaaaaaaaaaaa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PersonalCenterActivity.class);
                startActivity(intent);
            }
        });


        return layout;
    }










}
