package com.libopenmw.openmw;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

	private SurfaceHolder mSurfaceHolder;
	private Handler mHandler;
	private Context mContext;
	private Paint mLinePaint;
	private Paint blackPaint;

	//for consistent rendering
	private long sleepTime;
	//amount of time to sleep for (in milliseconds)
	private long delay=70;
	//state of game (Running or Paused).
	int state = 1;
	public final static int RUNNING = 1;
	public final static int PAUSED = 2;
	public final static int STOPED = 3;

	GameSurface gEngine;

	public GameThread(SurfaceHolder surfaceHolder, Context context, Handler handler,GameSurface gEngineS){

		//data about the screen
		mSurfaceHolder = surfaceHolder;
		mHandler = handler;
		mContext = context;
		gEngine=gEngineS;
	}

	//This is the most important part of the code. It is invoked when the call to start() is
	//made from the SurfaceView class. It loops continuously until the game is finished or
	//the application is suspended.
	private long beforeTime;
	@Override
	public void run() {

		//UPDATE
		while (state==RUNNING) {
			Log.d("State","Thread is runnig");
			//time before update
			beforeTime = System.nanoTime();
			//This is where we update the game engine
			gEngine.Update();

			//DRAW
			Canvas c = null;
			try {
				//lock canvas so nothing else can use it
				c = mSurfaceHolder.lockCanvas(null);
				synchronized (mSurfaceHolder) {
					//clear the screen with the black painter.
					//reset the canvas
				//	c.drawColor(Color.WHITE);
					//This is where we draw the game engine.
					gEngine.doDraw(c);
				}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}



			//SLEEP
			//Sleep time. Time required to sleep to keep game consistent
			//This starts with the specified delay time (in milliseconds) then subtracts from that the
			//actual time it took to update and render the game. This allows our game to render smoothly.
			this.sleepTime = delay-((System.nanoTime()-beforeTime)/1000000L);

			try {
				//actual sleep code
				if(sleepTime>0){
					this.sleep(sleepTime);
				}
			} catch (InterruptedException ex) {
				Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
			}
			
			while (state==PAUSED){
				Log.d("State","Thread is pausing");
				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}}
