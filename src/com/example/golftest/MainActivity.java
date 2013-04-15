package com.example.golftest;

import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	public TextView t;
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

    // This gets executed in a non-UI thread:
    public void receiveMyMessage() {
        final String str = Integer.toString(random.nextInt());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // This gets executed on the UI thread so it can safely modify Views
                t.setText(str);
            }
        });
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		t=new TextView(this);
	    t=(TextView)findViewById(R.id.textId); 
	    t.setText("Step One: blast egg");
	    
	    performOnBackgroundThread(mUpdateUITimerTask);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
