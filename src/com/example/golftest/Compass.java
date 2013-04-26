package com.example.golftest;

import java.util.Random;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Compass extends Activity{ 

	public TextView t;
	public ImageView arrow;
	public ImageCompass imageCompass;
	

	
	@Override
	protected void onCreate(Bundle icicle) { 
		super.onCreate(icicle); 

		/*
		 * manually define the layout
		 */
		LinearLayout layout = new LinearLayout(this);
        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);

        /*
         * manually create the text view and position it
         */
		t = new TextView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 80);
		params.leftMargin = 50;
		params.topMargin = 60;
		t.setWidth(200);
		t.setHeight(100);
	    t.setText("Step One: blast egg");
	    layout.addView(t, params);

	    /*
	     * manually create the imageview with compass picture and position it
	     */
	    RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(100, 100);
		params.leftMargin = 150;
		params.topMargin = 160;
		imageCompass = new ImageCompass(this);
	    layout.addView(imageCompass, imageViewParams);

	    /*
	     * set the content view to this layout
	     */
	    setContentView(layout);
	    
        
        audioService = new AudioService();
	    
	    /*
	     * wake up the thread to keep updating values every second
	     */
		performOnBackgroundThread(mUpdateUITimerTask);
	} 
	
    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(),
     * for your activity to start interacting with the user.  This is a good
     * place to begin animations, open exclusive-access devices (such as the
     * camera), etc.
     * 
     * Derived classes must call through to the super class's implementation
     * of this method.  If they do not, an exception will be thrown.
     */
    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");

        /* start the render thread */
        //renderThread.start();

        super.onResume();

        

    }
    
    /**
     * Called as part of the activity lifecycle when an activity is going
     * into the background, but has not (yet) been killed.  The counterpart
     * to onResume(). 
     */
    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        
        super.onPause();
 
    }
    
    

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onStart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     */
    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();

        
        if(audioService!=null)
        {
        	//stopService(audioIntent);
        	audioService.onDestroy();
        	
        }
        
        /* big switch to shut off */
        //stopRun();


    }
    
//
//    /**
//     * Start the animation running.  All the conditions we need to
//     * run are present (surface, size, resumed).
//     */
//    private void startRun() {
//        synchronized (this) {
//            // Tell the subclass we're running.
//            try {
//                //animStart();
//            } catch (Exception e) {
//                //errorReporter.reportException(e);
//            }
//       
//            if (renderThread != null && renderThread.isAlive())
//            	renderThread.kill();
//            Log.i(TAG, "set running: start ticker");
//            renderThread = new RenderThread();
//        }
//    }
//	
//    /**
//     * Stop the animation running.  Our surface may have been destroyed, so
//     * stop all accesses to it.  If the caller is not the ticker thread,
//     * this method will only return when the ticker thread has died.
//     */
//    private void stopRun() {
//        // Kill the thread if it's running, and wait for it to die.
//        // This is important when the surface is destroyed, as we can't
//        // touch the surface after we return.  But if I am the ticker
//    	// thread, don't wait for myself to die.
//    	RenderThread thread = renderThread;
//        
//        if (thread != null && thread.isAlive()) {
//        	if (Thread.currentThread() == thread)
//        		thread.kill();
//        	else
//        		thread.killAndWait();
//        }
//        synchronized (this) {
//        	renderThread = null;
//        }
//        
//        // Tell the subclass we've stopped.
//        try {
//            //animStop();
//        } catch (Exception e) {
//        }
//    }
    
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

	private static Thread performOnBackgroundThread(final Runnable runnable) {
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

	

	private void receiveMyMessage() {
		//final float val = random.nextFloat();
		final float degree = random.nextFloat() * 180;
		
		

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				long now = System.currentTimeMillis();
				if(audioService!=null) {
					audioService.doUpdate(now);
					// This gets executed on the UI thread so it can safely modify Views
					t.setText(Float.toString(audioService.peakSpecRead()));
					/* set the degree of the compass */
					imageCompass.setDirection((int)degree);
				}
			}
		});
	}


	
    

    
    //the audio processing service
    private AudioService audioService;
    
    private Handler mHandler = new Handler();
    
    private static final String TAG = "Compass";
    
	private Random random = new Random();


    
}