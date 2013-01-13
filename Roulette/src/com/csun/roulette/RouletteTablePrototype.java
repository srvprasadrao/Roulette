package com.csun.roulette;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class RouletteTablePrototype extends View {
	private static final int TEXT_SIZE = 20;
	
	private Paint tablePaint;
	private Paint textPaint;
	private Paint betPaint;
	private Paint wheelPaint;
	private Paint borderPaint;
	private List<Chip> chips;
	private Rect rectangle;
	
	private static final int betW = 35;
	private static final int betH = 60;
	
	private final RouletteWheel wheel;
	
	private Chip currentChip;
	private BetArea currentArea;
	
	enum FingerState {
		NOT_READY,
		TOUCHING,
		DONE,
	}
	
	private FingerState fingerState;
	
	public RouletteTablePrototype(Context context) {
		super(context);
	    wheel = new RouletteWheel(120, 120, 100);
		initialize(context);
	}
	
	public RouletteTablePrototype(Context context, AttributeSet attrs) {
		super(context, attrs);
	    wheel = new RouletteWheel(120, 120, 100);
		initialize(context);
	}
	
	public RouletteTablePrototype(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	    wheel = new RouletteWheel(120, 120, 100);
		initialize(context);
	}
	
	private void initialize(Context context) {
		setBackgroundDrawable(getResources().getDrawable(R.drawable.table));
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(TEXT_SIZE);
		
		tablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    tablePaint.setStyle(Paint.Style.FILL);
	    
	    betPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    betPaint.setStyle(Paint.Style.FILL);
	    betPaint.setColor(Color.BLACK);
	    
	    wheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    wheelPaint.setStyle(Paint.Style.FILL);
	    wheelPaint.setColor(Color.YELLOW);
	    
	    borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    borderPaint.setStyle(Paint.Style.STROKE);
	    borderPaint.setColor(Color.WHITE);
	    
	    chips = new ArrayList<Chip>();
	    Chip.setBitmapResource(context);	
	    
	    rectangle = new Rect(0, 0, 0, 0);
	    currentChip = new Chip(ChipType._25_DOLLARS, 0, 0);
	    currentArea = new BetArea(0.0f, 0.0f, false);
	    
	    fingerState = FingerState.NOT_READY;
	} 
	
	private void drawTable(Canvas canvas) {
		int startX = 50;
		int startY = 50; 
		int w = 50;
		int h = 80;
		
		betPaint.setColor(Color.RED);
		for (int x = startX; x <= 8 * w; x += w) {
			for (int y = startY; y <= 3 * h; y += h) {
				canvas.drawRect(x, y, x + w, y + h, betPaint);
				if (betPaint.getColor() == Color.RED) {
					betPaint.setColor(Color.BLACK);
				} else {
					betPaint.setColor(Color.RED);
				}
			}
		}
	}
	
	private BetArea getIntersectBettingArea(Chip c) {
		int startX = 50;
		int startY = 50; 
		int w = 50;
		int h = 80;
		float centerX = 0.0f;
		float centerY = 0.0f;
		float minX = 10000.0f;
		float minY = 10000.0f;
		float distance;
		float minDistance = 1000000.0f;
		boolean inside = false;
		for (int x = startX; x <= 8 * w; x += w) {
			for (int y = startY; y <= 3 * h; y += h) {
				centerX = x + w/2.0f;
				centerY = y + h/2.0f; 
				
				distance = computeDistance(c.getX(), c.getY(), centerX, centerY);
				if (distance < minDistance) {
					minDistance = distance;
					minX = centerX;
					minY = centerY;
					inside = true;
				}
				
				distance = computeDistance(c.getX(), c.getY(), x, y);
				if (distance < minDistance) {
					minDistance = distance;
					minX = x;
					minY = y;
					inside = false;
				}
			}
		}
		return new BetArea(minX, minY, inside);
	}
	
	private float computeDistance(float x0, float y0, float x1, float y1) {
		return (float) Math.sqrt((x1 - x0)*(x1 - x0) + (y1 - y0) *(y1 - y0));
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTable(canvas);
		// wheel.draw(canvas);
		for (Chip c : chips) {
			c.draw(canvas);
		}
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Style.FILL);
		p.setColor(Color.MAGENTA);
		currentChip.draw(canvas);
		
		Paint fP = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		canvas.drawCircle(currentArea.getX(), currentArea.getY(), 20, p);
		if (fingerState == FingerState.TOUCHING) {
			
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		int action = event.getAction();
		currentChip.setX(x);
		currentChip.setY(y);
		invalidate();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				
			case MotionEvent.ACTION_UP:
				currentArea = getIntersectBettingArea(currentChip);
				
			case MotionEvent.ACTION_CANCEL:
				
			case MotionEvent.ACTION_MOVE:
				
			case MotionEvent.ACTION_OUTSIDE:
		}
		
		// chips.add(new Chip(ChipType._1_DOLLAR, x, y));
		// invalidate();
		return true;
	}
}
