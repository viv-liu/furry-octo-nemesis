package com.example.demoopengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class OpenGLES20 extends Activity {
	
	private GLSurfaceView mGLView;
	private SwipeGestureListener gListener;
	private GestureDetectorCompat mDetector;
	private float angle;
	private int axis;
	private final int X = 0, Y = 1, Z = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);
		
		//mDetector = new GestureDetectorCompat(this,this);
		
		
		//mDetector = new GestureDetectorCompat(this, new SwipeGestureListener(OpenGLES20.this, (MyGLSurfaceView)mGLView));
		//mGLView.setOnTouchListener(gListener);
	}

	/*@Override 
    public boolean onTouchEvent(MotionEvent event){ 
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }*/
	
	@Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class MyGLSurfaceView extends GLSurfaceView {
		
		private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	    private float mPreviousX = 0;
	    private float mPreviousY = 0;
	    private final MyGL20Renderer mRenderer;
	    
		public MyGLSurfaceView(Context context) {
			super(context);
			
			setEGLContextClientVersion(2);
			mRenderer = new MyGL20Renderer();
			setRenderer(mRenderer);
			
			// Render the view only when there is a change in the drawing data
			//setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent e) {
		    // MotionEvent reports input details from the touch screen
		    // and other input controls. In this case, you are only
		    // interested in events where the touch position changed.
			
			if(e != null) {
				float x = e.getX();
				float y = e.getY();
				
				if(e.getAction() == MotionEvent.ACTION_MOVE) {
					if(mRenderer != null) {
						float deltaX = (x - mPreviousX) * TOUCH_SCALE_FACTOR; 
						float deltaY = (y - mPreviousY) * TOUCH_SCALE_FACTOR;
						
						mRenderer.mDeltaX += deltaX;
						mRenderer.mDeltaY += deltaY;
					}
				}
				
				mPreviousX = x;
				mPreviousY = y;
				
				return true;
			} else {
				return super.onTouchEvent(e);
			}
			/*if(mPreviousAngle != angle) {
		    
				mRenderer.axisIndex = axis;
				mRenderer.mAngle = angle;
				Log.d("Debug purposes", "mRenderer mAngle: " + String.valueOf(mRenderer.mAngle));
				requestRender();
				mPreviousAngle = angle;
		    }
		    return true;*/
		}
	}
	
	class SwipeGestureListener extends SimpleOnGestureListener{
		Context context;
		MyGLSurfaceView mGLView;
		  GestureDetector gDetector;
		  private final float TOUCH_SCALE_FACTOR = 100.0f / 320;
		  static final int SWIPE_MIN_DISTANCE = 10;
		  static final int SWIPE_MAX_OFF_PATH = 250;
		  static final int SWIPE_THRESHOLD_VELOCITY = 200;
		  
		  // THREE Constructors
		  public SwipeGestureListener() {
			  super();
		  }
			 
		  public SwipeGestureListener(Context context, MyGLSurfaceView mGLView) {
			  this.context = context;
			  this.mGLView = mGLView;
		  }
			 
		  public SwipeGestureListener(Context context, GestureDetector gDetector) {
			 
			   if (gDetector == null)
			    gDetector = new GestureDetector(context, this);
			 
			   this.context = context;
			   this.gDetector = gDetector;
		  }
		  
		  @Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				float x1 = e1.getX();
				float x2 = e2.getX();
				float y1 = e1.getY();
				float y2 = e2.getY();
				//Log.d("Debug purposes", "onScroll: " + String.valueOf(distanceX) + " " + String.valueOf(distanceY));
				
				if(Math.abs(distanceX) > SWIPE_MIN_DISTANCE) {
					
					float dx = x1 - x2;
					axis = Y;
					
					if(dx > 0) {
						//Log.d("Debug purposes", "onScroll: Right To Left: ");
					} else if (dx < 0) {
						//Log.d("Debug purposes", "onScroll: Left To Right: ");
					}
					
					angle = dx * TOUCH_SCALE_FACTOR;
					//Log.d("Debug purposes", "angle: " + String.valueOf(angle));
					
				} else if (Math.abs(distanceY) > SWIPE_MIN_DISTANCE) {
					
					float dy = y1 - y2;
					axis = X;
					if (dy > 0) {
						//Log.d("Debug purposes", "onScroll: Down To Up: ");
					} else if (dy < 0) {
						//Log.d("Debug purposes", "onScroll: Up To Down: ");
					}
					
					angle = dy * TOUCH_SCALE_FACTOR;
					//Log.d("Debug purposes", "angle: " + String.valueOf(angle));
				}	
				//mGLView.refresh();
				return super.onScroll(e1, e2, distanceX, distanceY);
			}
		
		@Override 
		public boolean onDown(MotionEvent event) {
			return true;
		}
    }
}
