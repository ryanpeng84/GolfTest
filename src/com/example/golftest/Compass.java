package com.example.golftest;

import java.util.List; 
import java.util.Random;

import android.app.ActionBar.LayoutParams;
import android.app.Activity; 
import android.content.Context; 
import android.graphics.Canvas; 
import android.graphics.Color; 
import android.graphics.Matrix;
import android.graphics.Paint; 
import android.graphics.Path; 
import android.hardware.Sensor; 
import android.hardware.SensorListener; 
import android.hardware.SensorManager; 
import android.os.Bundle; 
import android.os.Handler;
import android.util.Config; 
import android.util.Log; 
import android.view.Gravity;
import android.view.View; 
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Compass extends Activity{ 

	private static final String TAG = "Compass"; 

	public TextView t;
	public ImageView arrow;
	public ImageCompass imageCompass;
	
	private int pivX = 0;
	private int pivY = 0;
	
	private float lastDegree = 0;
	private SensorManager mSensorManager; 
	private SampleView mView; 
	private float[] mValues; 

	private Random random = new Random();
	private final Runnable mUpdateUITimerTask = new Runnable() {
		public void run() {
			while(true){
				receiveMyMessage();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public static Thread performOnBackgroundThread(final Runnable runnable) {
		final Thread t = new Thread() {
			@Override
			public void run() {
				try {
					runnable.run();
				} finally {

				}
			}
		};
		t.start();
		return t;
	}

	private Handler mHandler = new Handler();

	public void receiveMyMessage() {
//		final String str = Integer.toString(random.nextInt());
		final float val = random.nextFloat();
		final float degree = val * 180;
//		final RotateAnimation anim = new RotateAnimation(lastDegree, degree, pivX, pivY);
//		LinearInterpolator li = new LinearInterpolator();
//		anim.setInterpolator(li);
//		anim.setRepeatCount(0);
////		anim.setRepeatCount();
//		anim.setDuration(500);

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// This gets executed on the UI thread so it can safely modify Views
//				mValues = new float[3];
//				mValues[0] = val;
//				if (mView != null) { 
//					mView.invalidate(); 
//				} 

				t.setText(Float.toString(val));
//				arrow.startAnimation(anim);
				imageCompass.setDirection((int)degree);
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle icicle) { 
		super.onCreate(icicle); 
//		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE); 

//		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL); 
//		Log.d(TAG, "There are " + sensors.size() + " sensors."); 
//		for(Sensor sens : sensors) 
//		{ 
//			Log.d(TAG, "Sensor name: " + sens.getType()); 
//			Log.d(TAG, "Sensor name: " + sens.getName()); 
//		} 
//		setContentView(R.layout.activity_main);
//		mView = new SampleView(this); 
//		setContentView(mView); 

	    
	    
	    LinearLayout layout = new LinearLayout(this);
        // Define the LinearLayout's characteristics
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Set generic layout parameters
//        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		t = new TextView(this);
//	    t = (TextView)findViewById(R.id.textId);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 80);
		params.leftMargin = 50;
		params.topMargin = 60;
		t.setWidth(200);
		t.setHeight(100);
	    t.setText("Step One: blast egg");
	    layout.addView(t, params);

	    RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(100, 100);
		params.leftMargin = 150;
		params.topMargin = 160;
		imageCompass = new ImageCompass(this);
	    layout.addView(imageCompass, imageViewParams);
	    
	    setContentView(layout);
        
//	    arrow = new ImageView(this);
//	    arrow = (ImageView)findViewById(R.id.arrow);
//	    pivX = arrow.getLeft() + arrow.getWidth()/2;
//	    pivY = arrow.getTop() + arrow.getHeight()/2;
//	    pivY = (int) (arrow.getX() + arrow.getHeight()/2);
//	    pivX = (int) (arrow.getY() + arrow.getWidth()/2);
//	    Log.d(TAG, pivX + " " + pivY + " ");
		performOnBackgroundThread(mUpdateUITimerTask);
	} 

	@Override
	protected void onResume() 
	{ 
//		if (Config.LOGD) Log.d(TAG, "onResume"); 
		super.onResume(); 
//		mSensorManager.registerListener(mListener, 
//				SensorManager.SENSOR_ORIENTATION, 
//				SensorManager.SENSOR_DELAY_GAME); 
	} 

	@Override
	protected void onStop() 
	{ 
//		if (Config.LOGD) Log.d(TAG, "onStop"); 
//		mSensorManager.unregisterListener(mListener); 
		super.onStop(); 
	} 

	private class SampleView extends View { 
		private Paint mPaint = new Paint(); 
		private Path mPath = new Path(); 
		private boolean mAnimate; 
		private long mNextTime; 

		public SampleView(Context context) { 
			super(context); 

			// Construct a wedge-shaped path 
			mPath.moveTo(0, -50); 
			mPath.lineTo(-20, 60); 
			mPath.lineTo(0, 50); 
			mPath.lineTo(20, 60); 
			mPath.close(); 
		} 

		@Override protected void onDraw(Canvas canvas) { 
			Paint paint = mPaint; 

			canvas.drawColor(Color.WHITE); 

			paint.setAntiAlias(true); 
			paint.setColor(Color.BLUE); 
			paint.setStyle(Paint.Style.FILL); 

			int w = canvas.getWidth(); 
			int h = canvas.getHeight(); 
			int cx = w / 2; 
			int cy = h / 2; 

			canvas.translate(cx, cy); 
			if (mValues != null) { 
				canvas.rotate(-mValues[0]); 
			} 
			canvas.drawPath(mPath, mPaint); 
		} 

		@Override
		protected void onAttachedToWindow() { 
			mAnimate = true; 
			super.onAttachedToWindow(); 
		} 

		@Override
		protected void onDetachedFromWindow() { 
			mAnimate = false; 
			super.onDetachedFromWindow(); 
		} 
	}		 
}