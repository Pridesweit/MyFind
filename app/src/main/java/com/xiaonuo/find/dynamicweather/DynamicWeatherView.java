package com.xiaonuo.find.dynamicweather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.AnimationUtils;

import com.xiaonuo.find.dynamicweather.BaseDrawer.Type;

public class DynamicWeatherView extends SurfaceView implements SurfaceHolder.Callback {

	static final String TAG = DynamicWeatherView.class.getSimpleName();
	private DrawThread mDrawThread;

	public DynamicWeatherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private BaseDrawer preDrawer, curDrawer;
	private float curDrawerAlpha = 0f;
	private BaseDrawer.Type curType = BaseDrawer.Type.DEFAULT;
	private int mWidth, mHeight;

	private void init(Context context) {
		curDrawerAlpha = 0f;
		mDrawThread = new DrawThread();
		final SurfaceHolder surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setFormat(PixelFormat.RGBA_8888);
		mDrawThread.start();
	}

	private void setDrawer(BaseDrawer baseDrawer) {
		if (baseDrawer == null) {
			return;
		}
		curDrawerAlpha = 0f;
		if (this.curDrawer != null) {
			this.preDrawer = curDrawer;
		}
		this.curDrawer = baseDrawer;
		// updateDrawerSize(getWidth(), getHeight());
		// invalidate();
	}

	public void setDrawerType(Type type) {
		if (type == null) {
			return;
		}
		// UiUtil.toastDebug(getContext(), "setDrawerType->" + type.name());
		if (type != curType) {
			curType = type;
			setDrawer(BaseDrawer.makeDrawerByType(getContext(), curType));
		}

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// updateDrawerSize(w, h);
		mWidth = w;
		mHeight = h;
	}

	// private void updateDrawerSize(int w, int h) {
	// if (w == 0 || h == 0) {
	// return;
	// }// 这里必须加锁，因为在DrawThread.drawSurface的时候调用的是各种Drawer的绘制方法
	// // 绘制的时候会遍历内部的各种holder
	// // 然而那些个雨滴/星星的holder是在setSize的时候生成的
	// if (this.curDrawer != null) {
	// synchronized (curDrawer) {
	// if (this.curDrawer != null) {
	// curDrawer.setSize(w, h);
	// }
	// }
	// }
	// if (this.preDrawer != null) {
	// synchronized (preDrawer) {
	// if (this.preDrawer != null)
	// {//简直我就震惊了synchronized之前不是null，synchronized之后就有可能是null!
	// preDrawer.setSize(w, h);
	// }
	// }
	// }
	//
	// }

	private boolean drawSurface(Canvas canvas) {
		final int w = mWidth;
		final int h = mHeight;
		if (w == 0 || h == 0) {
			return true;
		}
		boolean needDrawNextFrame = false;
		// Log.d(TAG, "curDrawerAlpha->" + curDrawerAlpha);
		if (curDrawer != null) {
			curDrawer.setSize(w, h);
			needDrawNextFrame = curDrawer.draw(canvas, curDrawerAlpha);
		}
		if (preDrawer != null && curDrawerAlpha < 1f) {
			needDrawNextFrame = true;
			preDrawer.setSize(w, h);
			preDrawer.draw(canvas, 1f - curDrawerAlpha);
		}
		if (curDrawerAlpha < 1f) {
			curDrawerAlpha += 0.04f;
			if (curDrawerAlpha > 1) {
				curDrawerAlpha = 1f;
				preDrawer = null;
			}
		}
		// if (needDrawNextFrame) {
		// ViewCompat.postInvalidateOnAnimation(this);
		// }
		return needDrawNextFrame;
	}

	public void onResume() {
		// Let the drawing thread resume running.
		synchronized (mDrawThread) {
			mDrawThread.mRunning = true;
			mDrawThread.notify();
		}
		Log.i(TAG, "onResume");
	}

	public void onPause() {
		// Make sure the drawing thread is not running while we are paused.
		synchronized (mDrawThread) {
			mDrawThread.mRunning = false;
			mDrawThread.notify();
		}
		Log.i(TAG, "onPause");
	}

	public void onDestroy() {
		// Make sure the drawing thread goes away.
		synchronized (mDrawThread) {
			mDrawThread.mQuit = true;
			mDrawThread.notify();
		}
		Log.i(TAG, "onDestroy");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// Tell the drawing thread that gradient_colour surface is available.
		synchronized (mDrawThread) {
			mDrawThread.mSurface = holder;
			mDrawThread.notify();
		}
		Log.i(TAG, "surfaceCreated");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// We need to tell the drawing thread to stop, and block until
		// it has done so.
		synchronized (mDrawThread) {
			mDrawThread.mSurface = holder;
			mDrawThread.notify();
			while (mDrawThread.mActive) {
				try {
					mDrawThread.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		holder.removeCallback(this);
		Log.i(TAG, "surfaceDestroyed");
	}

	private class DrawThread extends Thread {
		// These are protected by the Thread's lock.
		SurfaceHolder mSurface;
		boolean mRunning;
		boolean mActive;
		boolean mQuit;

		@Override
		public void run() {
			while (true) {
				// Log.i(TAG, "DrawThread run..");
				// Synchronize with activity: block until the activity is ready
				// and we have gradient_colour surface; report whether we are active or
				// inactive
				// at this point; exit thread when asked to quit.
				synchronized (this) {
					while (mSurface == null || !mRunning) {
						if (mActive) {
							mActive = false;
							notify();
						}
						if (mQuit) {
							return;
						}
						try {
							wait();
						} catch (InterruptedException e) {
						}
					}

					if (!mActive) {
						mActive = true;
						notify();
					}
					final long startTime = AnimationUtils.currentAnimationTimeMillis();
					//TimingLogger logger = new TimingLogger("DrawThread");
					// Lock the canvas for drawing.
					Canvas canvas = mSurface.lockCanvas();
					//logger.addSplit("lockCanvas");
					
					if (canvas != null) {
						canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
						// Update graphics.
						
						drawSurface(canvas);
						//logger.addSplit("drawSurface");
						// All done!
						mSurface.unlockCanvasAndPost(canvas);
						//logger.addSplit("unlockCanvasAndPost");
						//logger.dumpToLog();
					} else {
						Log.i(TAG, "Failure locking canvas");
					}
					final long drawTime = AnimationUtils.currentAnimationTimeMillis() - startTime;
					final long needSleepTime = 16 - drawTime;
					//Log.i(TAG, "drawSurface drawTime->" + drawTime + " needSleepTime->" + Math.max(0, needSleepTime));// needSleepTime);
					if (needSleepTime > 0) {
						try {
							Thread.sleep(needSleepTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				}
			}
		}
	}

}
