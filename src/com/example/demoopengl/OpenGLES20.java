package com.example.demoopengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.MotionEvent;

public class OpenGLES20 extends Activity {
	
	private GLSurfaceView mGLView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGLView = new MyGLSurfaceView(this);
		setContentView(mGLView);
	}
	
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
		}
	}
}