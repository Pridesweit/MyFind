package com.xiaonuo.find.dynamicweather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.xiaonuo.find.dynamicweather.CloudyDrawer.CircleHolder;

import java.util.ArrayList;

/**
 * 阴天
 * @author Mixiaoxiao
 *
 */
public class OvercastDrawer extends BaseDrawer{
	
//	final ArrayList<CloudHolder> holders = new ArrayList<CloudHolder>();
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	final ArrayList<CircleHolder> holders = new ArrayList<CircleHolder>();
	public OvercastDrawer(Context context, boolean isNight) {
		super(context, isNight);
		
	}
	@Override
	protected void setSize(int width, int height) {
		super.setSize(width, height);
		if(holders.size() == 0) {
			holders.add(new CircleHolder( 0.20f * width , -0.30f * width , 0.06f * width, 0.022f *width, 0.56f * width, 0.0015f  ,isNight ? 0x44374d5c :0x4495a2ab));
			holders.add(new CircleHolder( 0.59f * width , -0.35f * width , -0.18f * width, 0.032f *width, 0.6f * width, 0.00125f  ,isNight ? 0x55374d5c :0x335a6c78));
			holders.add(new CircleHolder( 0.9f * width , -0.18f * width , 0.08f * width, -0.015f *width, 0.42f * width, 0.0025f ,isNight ? 0x5a374d5c :0x556f8a8d));
		}
	}

	@Override
	public boolean drawWeather(Canvas canvas, float alpha) {
			for(CircleHolder holder :this.holders){
				holder.updateAndDraw(canvas, paint, alpha);
			}
		return true;
	}
	@Override
	protected int[] getSkyBackgroundGradient() {
		return isNight ? SkyBackground.OVERCAST_N : SkyBackground.OVERCAST_D;
	}
}
